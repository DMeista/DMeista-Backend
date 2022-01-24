package sinhee.kang.tutorial.application.service.post

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import sinhee.kang.tutorial.application.service.image.ImageService
import sinhee.kang.tutorial.application.dto.request.post.ChangePostRequest
import sinhee.kang.tutorial.application.dto.request.post.PostRequest
import sinhee.kang.tutorial.application.dto.response.post.PostContentResponse
import sinhee.kang.tutorial.application.dto.response.post.PostListResponse
import sinhee.kang.tutorial.domain.post.entity.Post
import sinhee.kang.tutorial.domain.post.entity.View
import sinhee.kang.tutorial.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.repository.ViewRepository
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.entity.user.enums.AccountRole
import sinhee.kang.tutorial.infra.util.authentication.bean.RequestAuthScope
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.notFound.ApplicationNotFoundException
import sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.UnAuthorizedException
import sinhee.kang.tutorial.infra.api.kakao.service.VisionService

@Service
class PostServiceImpl(
    private val requestAuthScope: RequestAuthScope,
    private val imageService: ImageService,
    private val visionService: VisionService,

    private val postRepository: PostRepository,
    private val viewRepository: ViewRepository
) : PostService {

    override fun getAllHashTagList(pageable: Pageable, tags: String): PostListResponse {
        val currentUser = requestAuthScope.user
        val posts = postRepository.findByTagsContainsOrderByCreatedAtDesc(pageable, tags)
            ?: throw ApplicationNotFoundException()

        return PostListResponse(currentUser, posts)
    }

    override fun getPostContent(postId: Int): PostContentResponse {
        val currentUser = requestAuthScope.user
        val post: Post = postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }

        val nextPost: Post? = postRepository.findTop1ByPostIdAfterOrderByPostIdAsc(post.postId)
        val prevPost: Post? = postRepository.findTop1ByPostIdBeforeOrderByPostIdDesc(post.postId)

        if (currentUser != null)
            savePostViewer(currentUser, post)

        return PostContentResponse(currentUser, post, nextPost, prevPost)
    }

    override fun generatePost(postRequest: PostRequest): Int {
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

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
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

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
        val currentUser = requestAuthScope.user
            ?: throw UnAuthorizedException()

        postRepository.findById(postId)
            .orElseThrow { ApplicationNotFoundException() }
            .checkUserPermission(currentUser)
            .apply {
                postRepository.deleteById(postId)
                imageService.removeImageFiles(this)
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
            ?: viewRepository.save(
                View(
                    user = user,
                    post = post
                )
            )
    }

    private fun Post.checkUserPermission(user: User): Post =
        takeIf { it.user == user || user.isRoles(AccountRole.ADMIN) }
            ?: throw sinhee.kang.tutorial.infra.util.exception.exceptions.exceptions.unAuthorized.PermissionDeniedException()
}
