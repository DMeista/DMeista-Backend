package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.ArrayList

import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.file.domain.repository.ImageFileRepository
import sinhee.kang.tutorial.domain.file.service.ImageService
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.view.View
import sinhee.kang.tutorial.domain.post.domain.view.repository.ViewRepository
import sinhee.kang.tutorial.domain.post.dto.response.*
import sinhee.kang.tutorial.domain.post.exception.ApplicationNotFoundException
import sinhee.kang.tutorial.domain.post.exception.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.security.exception.UserNotFoundException
import sinhee.kang.tutorial.infra.api.vision.VisionApi

@Service
class PostServiceImpl(
        private val authService: AuthService,
        private val imageService: ImageService,
        private val visionApi: VisionApi,

        private val userRepository: UserRepository,
        private val postRepository: PostRepository,
        private val viewRepository: ViewRepository,
        private val imageFileRepository: ImageFileRepository

) : PostService {

    override fun getAllHashTagList(pageable: Pageable, tags: String?): PostListResponse {
        tags ?: throw ApplicationNotFoundException()
        return postRepository.findByTagsContainsOrderByCreatedAtDesc(pageable, tags)
                ?.let { getPostList(it) }
                ?: throw ApplicationNotFoundException()
    }

    override fun getPostContent(postId: Int): PostContentResponse {
        lateinit var user: User
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }

        try {
            user = authService.authValidate()
            viewRepository.findByUserAndPost(user, post)
                    ?: viewRepository.save(View(user = user, post = post))
        }
        catch (e: Exception) {
            user = User()
        }

        val nextPost = postRepository.findTop1ByPostIdAfterOrderByPostIdAsc(postId) ?: Post()
        val previousPost = postRepository.findTop1ByPostIdBeforeOrderByPostIdDesc(postId) ?: Post()

        val commentsList: MutableList<Comment> = post.commentList
        val commentsResponse: MutableList<PostCommentsResponse> = ArrayList()

        for (comment in commentsList) {
            val commentAuthor = userRepository.findByNickname(comment.author)
                    ?: throw UserNotFoundException()

            val subCommentsList: MutableList<SubComment> = comment.subCommentList
            val subCommentsResponses: MutableList<PostSubCommentsResponse> = ArrayList()

            for (subComment in subCommentsList) {
                subCommentsResponses.add(PostSubCommentsResponse(
                        subCommentId = subComment.subCommentId,
                        content = subComment.content,
                        createdAt = subComment.createdAt,
                        author = subComment.author,
                        isMine = (commentAuthor.nickname == user.nickname)
                ))
            }

            commentsResponse.add(PostCommentsResponse(
                    commentId = comment.commentId,
                    content = comment.content,
                    createdAt = comment.createdAt,
                    author = commentAuthor.nickname,
                    isMine = (commentAuthor.nickname == user.nickname),
                    subComments = subCommentsResponses
            ))
        }

        return PostContentResponse(
                title = post.title,
                content = post.content,
                author = post.author,
                tags = post.tags,
                viewCount = post.viewList.count(),
                emojiCount = post.emojiList.count(),
                emoji = post.emojiList
                    .filter { emoji -> emoji.user == user }
                    .map { it.status }.firstOrNull(),
                createdAt = post.createdAt,
                isMine = (post.author == user.nickname),

                nextPostTitle = nextPost.title,
                prevPostTitle = previousPost.title,

                nextPostId = nextPost.postId,
                prevPostId = previousPost.postId,

                images = post.imageFileList
                    .map { it.fileName }.toList(),
                comments = commentsResponse
        )
    }

    override fun uploadPost(title: String, content: String, tags: String?, autoTags: Boolean, imageFiles: Array<MultipartFile>?): Int? {
        val user = authService.authValidate()
        val request: MutableSet<String> = mutableSetOf()

        tags?.let {
            request.add(it)
        }

        if (!imageFiles.isNullOrEmpty() && autoTags) {
            request.addAll(getTagsFromImage(imageFiles))
        }

        val post = postRepository.save(Post(
            user = user,
            title = title,
            content = content,
            author = user.nickname,
            tags =  request.joinToString()
        ))

        if (!imageFiles.isNullOrEmpty()) {
            imageService.saveImageFiles(post, imageFiles)
        }

        return post.postId
    }

    override fun changePost(postId: Int, title: String, content: String, tags: String?, imageFiles: Array<MultipartFile>?): Int? {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
        if ( post.author == user.nickname || user.isRoles(AccountRole.ADMIN) ) {
                post.title = title
                post.content = content
                post.tags = tags

                postRepository.save(post)
        }

        val imageFile = post.imageFileList

        imageService.run {
            deleteImageFiles(post, imageFile)
            saveImageFiles(post, imageFiles)
        }
        return post.postId
    }

    override fun deletePost(postId: Int) {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
                .takeIf { it.author == user.nickname || user.isRoles(AccountRole.ADMIN) }
                ?.also { postRepository.deleteById(it.postId!!) }
                ?: throw PermissionDeniedException()
        post.imageFileList.let { imageFile ->
            imageService.deleteImageFiles(post, imageFile) }
        imageFileRepository.deleteByPost(post)
    }

    private fun getTagsFromImage(imageFiles: Array<MultipartFile>): MutableList<String> {
        val request: MutableList<String> = ArrayList()
        for (image in imageFiles) {
            try {
                val tagsList = visionApi.getVisionApi(image)
                for (tag in tagsList) { request.add("#$tag") }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return request
    }

    private fun getPostList(postPage: Page<Post>): PostListResponse {
        val user = try { authService.authValidate() }
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
                author = post.author,
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
