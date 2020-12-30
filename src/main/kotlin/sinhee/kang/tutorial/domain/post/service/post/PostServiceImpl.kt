package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
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
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException
import sinhee.kang.tutorial.infra.api.vision.VisionApi
import kotlin.collections.ArrayList

@Service
class PostServiceImpl(
        private var authService: AuthService,
        private var imageService: ImageService,
        private val visionApi: VisionApi,

        private var userRepository: UserRepository,
        private var postRepository: PostRepository,
        private var viewRepository: ViewRepository,
        private var imageFileRepository: ImageFileRepository

) : PostService {

    override fun getAllHashTagList(pageable: Pageable, tags: String?): PostListResponse {
        tags?: { throw ApplicationNotFoundException() }()
        return postRepository.findByTagsContainsOrderByCreatedAtDesc(pageable, tags)
                ?.let { getPostList(it) }
                ?: { throw ApplicationNotFoundException() }()
    }


    override fun getPostContent(postId: Int): PostContentResponse {
        lateinit var user: User
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
        try {
            user = authService.authValidate()
            viewRepository.findByUserAndPost(user, post)
                    ?: { viewRepository.save(View(user, post)) }()
        }
        catch (e: Exception) {
            user = User()
        }

        val commentList: MutableList<Comment> = post.commentList
        val commentsResponse: MutableList<PostCommentsResponse> = ArrayList()

        val nextPost = postRepository.findTop1ByPostIdAfterOrderByPostIdAsc(postId) ?: { Post() }()
        val prePost = postRepository.findTop1ByPostIdBeforeOrderByPostIdDesc(postId) ?: { Post() }()

        val imageNames: MutableList<String> = ArrayList()
        post.imageFileList.let { imageFile ->
            for (image in imageFile) {
                imageNames.add(image.fileName)
                }
            }

        for (comment in commentList) {
            val commentAuthor = userRepository.findByNickname(comment.author)
                    ?:{ throw UserNotFoundException() }()

            val subCommentList: MutableList<SubComment> = comment.subCommentList
            val subCommentsResponses: MutableList<PostSubCommentsResponse> = ArrayList()

            for (subComment in subCommentList) {
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
                view = post.viewList.count(),
                createdAt = post.createdAt,
                isMine = (post.author == user.nickname),

                nextPostTitle = nextPost.title,
                prePostTitle = prePost.title,

                nextPostId = nextPost.postId,
                prePostId = prePost.postId,

                images = imageNames,
                comments = commentsResponse
        )
    }


    override fun uploadPost(title: String, content: String, tags: String?, autoTags: Boolean, imageFile: Array<MultipartFile>?): Int? {
        val user = authService.authValidate()
        var request: MutableList<String> = ArrayList()

        if (imageFile != null && autoTags) {
            request = getTagsFromImage(imageFile)
        }

        tags?.run { request.add(this) }

        val post = postRepository.save(Post(
                user = user,
                author = user.nickname,
                title = title,
                content = content,
                tags =  request.joinToString()
        ))
        imageService.saveImageFile(post, imageFile)
        return post.postId
    }


    override fun changePost(postId: Int, title: String, content: String, tags: String?, image: Array<MultipartFile>?): Int? {
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
            deleteImageFile(post, imageFile)
            saveImageFile(post, image)
        }
        return post.postId
    }


    override fun deletePost(postId: Int) {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
                .takeIf { it.author == user.nickname || user.isRoles(AccountRole.ADMIN) }
                ?.also { postRepository.deleteById(it.postId) }
                ?: { throw PermissionDeniedException() }()
        post.imageFileList.let { imageFile ->
            imageService.deleteImageFile(post, imageFile) }
        imageFileRepository.deleteByPost(post)
    }


    fun getTagsFromImage(imageFile: Array<MultipartFile>): MutableList<String> {
        val request: MutableList<String> = ArrayList()
        for (image in imageFile) {
            try {
                val list = visionApi.getVisionApi(image)
                for (tag in list) { request.add(tag) }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return request
    }


    fun getPostList(postPage: Page<Post>): PostListResponse {
        val user = try { authService.authValidate() }
                catch (e: Exception) { null }
        val postResponse: MutableList<PostResponse> = ArrayList()

        for (post in postPage) {
            val checkedUser = viewRepository.findByPost(post)
            val checked: Boolean = user
                    ?.let { viewRepository.findByUserAndPost(user, post)
                            ?.let { true }
                            ?: { false }()
                    }
                    ?:{ false }()
            postResponse.add(PostResponse(
                    id = post.postId,
                    title = post.title,
                    content = post.content,
                    author = post.author,
                    tags = post.tags,
                    view = checkedUser.count(),
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