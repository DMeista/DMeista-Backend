package sinhee.kang.tutorial.domain.post.service.post

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import sinhee.kang.tutorial.domain.auth.service.auth.AuthService
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.post.Post
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.dto.response.*
import sinhee.kang.tutorial.domain.post.exception.ApplicationNotFoundException
import sinhee.kang.tutorial.domain.post.exception.PermissionDeniedException
import sinhee.kang.tutorial.domain.user.domain.user.enums.AccountRole
import sinhee.kang.tutorial.global.config.security.exception.UserNotFoundException

@Service
class PostServiceImpl(
        private var authService: AuthService,

        private var userRepository: UserRepository,
        private var postRepository: PostRepository
) : PostService {

    override fun getAllPostList(pageable: Pageable): PostListResponse {
        return getPostList(postRepository.findAllByOrderByCreatedAtDesc(pageable))
    }


    override fun getAllHashTagList(pageable: Pageable, tags: String?): PostListResponse {
        return postRepository.findAllByTagsContainsOrderByCreatedAtDesc(pageable, tags)
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

                comments = commentsResponse
        )
    }


    override fun uploadPost(title: String, content: String, tags: String?): Int? {
        val user = authService.authValidate()
        val post = postRepository.save(Post(
                user = user,
                author = user.nickname,
                title = title,
                content = content,
                tags = tags
        ))
        return post.postId
    }


    override fun changePost(postId: Int, title: String, content: String, tags: String?): Int? {
        val user = authService.authValidate()
        val post = postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also { post ->
                    post.title = title
                    post.content = content
                    post.tags = tags

                    postRepository.save(post)
                }
                ?: { throw PermissionDeniedException() }()
        return post.postId
    }


    override fun deletePost(postId: Int) {
        val user = authService.authValidate()
        postRepository.findById(postId)
                .orElseThrow { ApplicationNotFoundException() }
                .takeIf { it.author == user.nickname || user.roles == AccountRole.ADMIN }
                ?.also { postRepository.deleteById(it.postId!!) }
                ?: { throw PermissionDeniedException() }()
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