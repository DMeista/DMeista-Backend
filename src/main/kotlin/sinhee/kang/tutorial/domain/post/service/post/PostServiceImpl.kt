package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.file.domain.repository.ImageFileRepository
import sinhee.kang.tutorial.domain.file.service.ImageService
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.dto.response.*
import sinhee.kang.tutorial.domain.post.exception.ApplicationNotFoundException
import sinhee.kang.tutorial.domain.post.exception.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException
import sinhee.kang.tutorial.infra.api.VisionApi
import kotlin.collections.ArrayList

@Service
class PostServiceImpl(
        private var authService: AuthService,
        private var imageService: ImageService,
        private val visionApi: VisionApi,

        private var imageFileRepository: ImageFileRepository,
        private var userRepository: UserRepository,
        private var postRepository: PostRepository

) : PostService {

    override fun getAllPostList(pageable: Pageable): PostListResponse {
        return getPostList(postRepository.findAllByOrderByCreatedAtDesc(pageable))
    }


    override fun getAllHashTagList(pageable: Pageable, tags: String?): PostListResponse {
        tags?.let { throw ApplicationNotFoundException() }
        return postRepository.findByTagsContainsOrderByCreatedAtDesc(pageable, tags)
                ?.let { getPostList(it) }
                ?: { throw ApplicationNotFoundException() }()
    }


    override fun getHitPost(pageable: Pageable): PostListResponse {
        return getPostList(postRepository.findAllByOrderByViewDesc(pageable))
    }


    override fun getPostContent(postId: Int): PostContentResponse {
        val user = try {
            authService.authValidate()
        } catch (e: Exception) {
            User()
        }
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }

        val commentList: MutableList<Comment> = post.commentList
        val commentsResponse: MutableList<PostCommentsResponse> = ArrayList()

        val nextPost = postRepository.findTop1ByPostIdAfterOrderByPostIdAsc(postId)
                ?: { Post() }()
        val prePost = postRepository.findTop1ByPostIdBeforeOrderByPostIdDesc(postId)
                ?: { Post() }()

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
                        type = subComment.authorType,
                        isMine = (commentAuthor.nickname == user.nickname)
                ))
            }

            commentsResponse.add(PostCommentsResponse(
                    commentId = comment.commentId,
                    content = comment.content,
                    createdAt = comment.createdAt,
                    author = commentAuthor.nickname,
                    authorType = commentAuthor.roles,
                    isMine = (commentAuthor.nickname == user.nickname),
                    subComments = subCommentsResponses
            ))
        }
        postRepository.save(post.view())

        return PostContentResponse(
                title = post.title,
                content = post.content,
                author = post.author,
                tags = post.tags,
                view = post.view,
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
        val request: MutableList<String> = ArrayList()

        if (imageFile != null && autoTags) {
            for (image in imageFile) {
                val list = visionApi.visionImage(image)
                for (tag in list) {
                    request.add(tag)
                }
            }
        }
        tags?.let {
            request.add(tags)
        }
        val post = postRepository.save(Post(
                user = user,
                author = user.nickname,
                title = title,
                content = content,
                tags = request.joinToString(", ")
        ))
        imageService.saveImageFile(post, imageFile)
        return post.postId
    }


    override fun changePost(postId: Int, title: String, content: String, tags: String?, image: Array<MultipartFile>?): Int? {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
        if ( post.author == user.nickname || user.roles == AccountRole.ADMIN ) {
                post.title = title
                post.content = content
                post.tags = tags

                postRepository.save(post)
        }

        val imageFile = post.imageFileList
        imageService.deleteImageFile(post, imageFile)
        imageService.saveImageFile(post, image)
        return post.postId
    }


    override fun deletePost(postId: Int) {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also { postRepository.deleteById(it.postId!!) }
                ?: { throw PermissionDeniedException() }()
        post.imageFileList.let { imageFile ->
            imageService.deleteImageFile(post, imageFile) }
        imageFileRepository.deleteByPost(post)
    }

    fun getPostList(postPage: Page<Post>): PostListResponse {
        val postResponse: MutableList<PostResponse> = ArrayList()
        for (post in postPage) {
            postResponse.add(PostResponse(
                    id = post.postId,
                    title = post.title,
                    content = post.content,
                    author = post.author,
                    tags = post.tags,
                    view = post.view,
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