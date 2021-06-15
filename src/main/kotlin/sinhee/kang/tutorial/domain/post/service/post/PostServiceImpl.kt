package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.ArrayList

import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.file.service.ImageService
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.view.View
import sinhee.kang.tutorial.domain.post.domain.view.repository.ViewRepository
import sinhee.kang.tutorial.domain.post.dto.response.*
import sinhee.kang.tutorial.global.exception.exceptions.notFound.ApplicationNotFoundException
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import sinhee.kang.tutorial.infra.api.kakao.adult.service.DetectService
import sinhee.kang.tutorial.infra.api.kakao.vision.service.LabelService

@Service
class PostServiceImpl(
    private val authService: AuthService,
    private val imageService: ImageService,

    private val visionLabelApi: LabelService,
    private val detectImageApi: DetectService,

    private val postRepository: PostRepository,
    private val viewRepository: ViewRepository
): PostService {

    override fun getAllHashTagList(pageable: Pageable, tags: String): PostListResponse {
        return postRepository.findByTagsContainsOrderByCreatedAtDesc(pageable, tags)
            ?.let { getPostList(it) }
            ?: throw ApplicationNotFoundException()
    }

    override fun getPostContent(postId: Int): PostContentResponse {
        var currentUser: User
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }

        try {
            currentUser = authService.verifyCurrentUser()
            viewRepository.findByUserAndPost(currentUser, post)
                ?: viewRepository.save(View(user = currentUser, post = post))
        }
        catch (e: Exception) {
            currentUser = User()
        }

        val commentsResponse: MutableList<PostCommentsResponse> = ArrayList()

        post.commentList.forEach { comment ->
            val subCommentsResponses: MutableList<PostSubCommentsResponse> = ArrayList()

            comment.subCommentList.forEach { subComment ->
                subCommentsResponses.add(PostSubCommentsResponse(
                    subCommentId = subComment.subCommentId,
                    content = subComment.content,
                    createdAt = subComment.createdAt,
                    author = subComment.user.nickname,
                    isMine = (subComment.user == currentUser)
                ))
            }

            commentsResponse.add(PostCommentsResponse(
                commentId = comment.commentId,
                content = comment.content,
                createdAt = comment.createdAt,
                author = comment.user.nickname,
                isMine = (comment.user == currentUser),
                subComments = subCommentsResponses
            ))
        }

        val nextPost = postRepository.findTop1ByPostIdAfterOrderByPostIdAsc(postId) ?: Post()
        val previousPost = postRepository.findTop1ByPostIdBeforeOrderByPostIdDesc(postId) ?: Post()

        return PostContentResponse(
            title = post.title,
            content = post.content,
            author = post.user.nickname,
            tags = post.tags,
            viewCount = post.viewList.count(),
            emojiCount = post.emojiList.count(),
            emoji = post.emojiList
                .filter { emoji -> emoji.user == currentUser }
                .map { it.status }.firstOrNull(),
            createdAt = post.createdAt,
            isMine = (post.user == currentUser),

            nextPostTitle = nextPost.title,
            prevPostTitle = previousPost.title,

            nextPostId = nextPost.postId,
            prevPostId = previousPost.postId,

            images = post.imageFileList
                .map { it.fileName }.toList(),
            comments = commentsResponse
        )
    }

    override fun uploadPost(title: String, content: String, tags: List<String>?, autoTags: Boolean, imageFiles: Array<MultipartFile>?): Int? {
        val user = authService.verifyCurrentUser()
        val tagsResponse: MutableSet<String> = mutableSetOf()

        tags?.let { tagsResponse.addAll(it) }

        imageFiles
            ?.forEach { detectImageApi.filterAdultImage(it) }
            ?.takeIf { autoTags }
            ?.also { tagsResponse.addAll(getTagsFromImage(imageFiles)) }

        if (!imageFiles.isNullOrEmpty() && autoTags) {
            tagsResponse.addAll(getTagsFromImage(imageFiles))
        }

        val post = postRepository.save(Post(
            user = user,
            title = title,
            content = content,
            tags = tagsResponse.joinToString { "#$it" }
        ))

        imageService.saveImageFiles(post, imageFiles)

        return post.postId
    }

    override fun changePost(postId: Int, title: String?, content: String?, tags: List<String>?, imageFiles: Array<MultipartFile>?): Int? {
        val user = authService.verifyCurrentUser()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
        if ( !(post.user == user || user.isRoles(AccountRole.ADMIN)) )
            throw PermissionDeniedException()

        title?.let { post.title = it }
        content?.let { post.content = it }
        post.tags = tags?.joinToString { "#$it" }

        postRepository.save(post)

        val imageFile = post.imageFileList

        imageService.run {
            deleteImageFiles(post, imageFile)
            saveImageFiles(post, imageFiles)
        }
        return post.postId
    }

    override fun deletePost(postId: Int) {
        val user = authService.verifyCurrentUser()
        val post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }
            .takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?.also { postRepository.deleteById(it.postId) }
            ?: throw PermissionDeniedException()
        post.imageFileList.let { imageFile ->
            imageService.deleteImageFiles(post, imageFile)
        }
    }

    private fun getTagsFromImage(imageFiles: Array<MultipartFile>): MutableSet<String> {
        val request: MutableSet<String> = mutableSetOf()
        imageFiles.forEach { image ->
            val tagsList = visionLabelApi.generateTagFromImage(image)
            tagsList.forEach { e -> request.add(e) }
        }
        return request
    }

    private fun getPostList(postPage: Page<Post>): PostListResponse {
        val user = try { authService.verifyCurrentUser() }
                catch (e: Exception) { null }
        val postResponse: MutableList<PostResponse> = ArrayList()

        for (post in postPage) {
            val checkedUser = viewRepository.findByPost(post)
            val checked: Boolean = user
                ?.let { viewRepository.findByUserAndPost(user, post)
                    ?.let { true }
                    ?: false
                }
                ?: false

            postResponse.add(PostResponse(
                id = post.postId,
                title = post.title,
                content = post.content,
                author = post.user.nickname,
                tags = post.tags,
                viewCount = checkedUser.count(),
                emojiCount = post.emojiList.count(),
                emoji = post.emojiList
                    .filter { emoji -> emoji.user == user }
                    .map { it.status }.firstOrNull(),
                checked = checked,
                createdAt = post.createdAt
            ))
        }
        return PostListResponse(
            postPage.totalElements.toInt(),
            postPage.totalPages,
            postResponse
        )
    }
}
