package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.image.service.ImageService
import sinhee.kang.tutorial.domain.post.dto.request.ChangePostRequest
import sinhee.kang.tutorial.domain.post.entity.post.Post
import sinhee.kang.tutorial.domain.post.repository.post.PostRepository
import sinhee.kang.tutorial.domain.post.repository.view.ViewRepository
import sinhee.kang.tutorial.domain.post.dto.response.*
import sinhee.kang.tutorial.domain.post.entity.view.View
import sinhee.kang.tutorial.domain.post.dto.request.PostRequest
import sinhee.kang.tutorial.global.exception.exceptions.notFound.ApplicationNotFoundException
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.entity.user.enums.AccountRole
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.PermissionDeniedException
import sinhee.kang.tutorial.global.exception.exceptions.unAuthorized.UnAuthorizedException
import sinhee.kang.tutorial.infra.api.kakao.service.VisionService

@Service
class PostServiceImpl(
    private val authService: AuthService,
    private val imageService: ImageService,
    private val visionService: VisionService,

    private val postRepository: PostRepository,
    private val viewRepository: ViewRepository
): PostService {

    override fun getAllHashTagList(pageable: Pageable, tags: String): PostListResponse {
        val currentUser: User? = try {
            authService.getCurrentUser()
        } catch (e: UnAuthorizedException) { null }
        val posts = postRepository.findByTagsContainsOrderByCreatedAtDesc(pageable, tags)
            ?: throw ApplicationNotFoundException()

        return PostListResponse(posts, getPostsList(currentUser, posts))
    }

    override fun getPostContent(postId: Int): PostContentResponse {
        val currentUser: User? = try {
            authService.getCurrentUser()
        } catch (e: UnAuthorizedException) { null }
        val post: Post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        val nextPost: Post? = postRepository.findTop1ByPostIdAfterOrderByPostIdAsc(post.postId)
        val prevPost: Post? = postRepository.findTop1ByPostIdBeforeOrderByPostIdDesc(post.postId)

        if (currentUser != null)
            savePostViewer(currentUser, post)

        return PostContentResponse(currentUser, post, nextPost, prevPost)
    }

    override fun generatePost(postRequest: PostRequest): Int {
        val currentUser = authService.getCurrentUser()

        val tags = mutableSetOf<String>().apply {
            postRequest.tags?.takeIf { isNotEmpty() }
                ?.run { addAll(this) }

            postRequest.imageFiles?.takeIf { postRequest.autoTags }
                ?.run { addAll(getTagsFromImage(this)) }
        }
        val post = postRepository.save(
            postRequest.toEntity(currentUser, tags)
        )

        postRequest.imageFiles?.let {
            imageService.saveImageFiles(post, it)
        }

        return post.postId
    }

    override fun changePost(changePostRequest: ChangePostRequest): Int {
        val currentUser = authService.getCurrentUser()

        val post = postRepository.save(
            postRepository.findById(changePostRequest.postId)
                .orElseThrow { ApplicationNotFoundException() }
                .checkUserPermission(currentUser)
                .update(changePostRequest)
        )

        changePostRequest.imageFiles?.let {
            with(imageService) {
                removeImageFiles(post)
                saveImageFiles(post, it)
            }
        }

        return post.postId
    }

    override fun removePost(postId: Int) {
        val currentUser = authService.getCurrentUser()

        postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply {
                postRepository.deleteById(postId)
                imageService.removeImageFiles(this)
            }
    }

    private fun getPostsList(user: User?, postPage: Page<Post>): MutableList<PostPreviewResponse> =
        mutableListOf<PostPreviewResponse>().apply {
            postPage.forEach { post: Post ->
                add(PostPreviewResponse(user, post))
            }
        }

    private fun getTagsFromImage(imageFiles: List<MultipartFile>): MutableSet<String> {
        val request: MutableSet<String> = mutableSetOf()

        imageFiles.forEach { image ->
            val tagsList = visionService.multiTaggingImage(image)
            tagsList.forEach { e -> request.add(e) }
        }
        return request
    }

    private fun savePostViewer(user: User, post: Post) {
        viewRepository.findByUserAndPost(user, post)
            ?: viewRepository.save(View(
                user = user,
                post = post
            ))
    }

    private fun Post.checkUserPermission(user: User): Post =
        takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?: throw PermissionDeniedException()
}
