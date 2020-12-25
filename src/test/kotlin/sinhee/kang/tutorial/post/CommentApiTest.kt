package sinhee.kang.tutorial.post

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import sinhee.kang.tutorial.TutorialApplication
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.post.domain.comment.Comment
import sinhee.kang.tutorial.domain.post.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.SubComment
import sinhee.kang.tutorial.domain.post.domain.subComment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.post.dto.request.CommentRequest
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CommentApiTest {
    @Autowired
    private lateinit var mvc: MockMvc
    @Autowired
    private lateinit var postRepository: PostRepository
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var commentRepository: CommentRepository
    @Autowired
    private lateinit var subCommentRepository: SubCommentRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var objectMapper: ObjectMapper

    val testMail = "rkdtlsgml50@naver.com"
    val passwd = "1234"
    val username = "user"

    @Before
    fun setup() {
        userRepository.save(User(
                email = testMail,
                password = passwordEncoder.encode(passwd),
                nickname = username
        ))
    }

    @After
    fun clean() {
        userRepository.findByNickname(username)
                ?.let { user -> userRepository.delete(user) }
    }


    @Test
    @Throws
    fun uploadCommentTest() {
        val postId = uploadPost()
        val comment: Comment = uploadComment(postId, "댓글")

        assert(comment.content == "댓글")

        commentRepository.delete(comment)
        postRepository.deleteById(postId)
    }


    @Test
    @Throws
    fun uploadSubCommentTest() {
        val postId = uploadPost()
        val comment: Comment = uploadComment(postId, "댓글")
        val subComment: SubComment = uploadSubComment(comment.commentId, "대댓글")

        assert(subComment.content == "대댓글")

        subCommentRepository.delete(subComment)
        commentRepository.delete(comment)
        postRepository.deleteById(postId)
    }


    @Test
    @Throws
    fun changeCommentTest() {
        val postId = uploadPost()
        var comment: Comment = uploadComment(postId, "댓글")
        comment = editComment(comment.commentId, "수정된 댓글")

        assert(comment.content == "수정된 댓글")

        commentRepository.delete(comment)
        postRepository.deleteById(postId)
    }


    @Test
    @Throws
    fun changeSubCommentTest() {
    }


    // TODO: Delete Comment, subComment
//    @Test
    @Throws
    fun deleteCommentTest() {
    }


//    @Test
    @Throws
    fun deleteSubCommentTest() {
    }


    private fun uploadPost(): Int {
        val accessToken = accessToken()
        return Integer.parseInt(mvc.perform(post("/posts")
                .header("Authorization", "Bearer $accessToken")
                .param("title", "title")
                .param("content", "content")
                .param("tags", "tags"))
                .andExpect(status().isOk).andDo(print())
                .andReturn().response.contentAsString)
    }


    private fun uploadComment(postId: Int, content: String): Comment {
        requestMvc(post("/comments/$postId"), CommentRequest(content), "Bearer ${accessToken()}")
        val post = postRepository.findById(postId)
                .orElseThrow { Exception() }
        return post.commentList[0]
    }


    private fun uploadSubComment(commentId: Int, content: String): SubComment {
        requestMvc(post("/comments/sub/$commentId"), CommentRequest(content), "Bearer ${accessToken()}")
        val comment = commentRepository.findById(commentId)
                .orElseThrow { Exception() }
        return comment.subCommentList[0]
    }


    private fun editComment(commentId: Int, content: String): Comment {
        requestMvc(patch("/comments/$commentId"), CommentRequest(content), "Bearer ${accessToken()}")
        return commentRepository.findById(commentId)
                .orElseThrow { Exception() }
    }


    private fun requestMvc(method: MockHttpServletRequestBuilder, obj: Any? = null, token: String? = ""): String {
        return mvc.perform(
                method
                        .header("Authorization", token)
                        .content(objectMapper
                                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                                .writeValueAsString(obj))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString
    }


    private fun accessToken(): String {
        val content = requestMvc(post("/auth"), SignInRequest(testMail, passwd))
        val response = mappingResponse(content, TokenResponse::class.java) as TokenResponse
        return response.accessToken
    }


    private fun mappingResponse(obj: String, cls: Class<*>): Any {
        return objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .readValue(obj, cls)
    }
}