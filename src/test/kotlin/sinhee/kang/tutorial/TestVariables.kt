package sinhee.kang.tutorial

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.repository.requestLimiter.EmailRequestLimiterRepository
import sinhee.kang.tutorial.domain.auth.repository.verification.EmailVerificationRepository
import sinhee.kang.tutorial.domain.post.repository.comment.CommentRepository
import sinhee.kang.tutorial.domain.post.repository.emoji.EmojiRepository
import sinhee.kang.tutorial.domain.post.repository.post.PostRepository
import sinhee.kang.tutorial.domain.post.repository.subComment.SubCommentRepository
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.domain.user.repository.friend.FriendRepository
import sinhee.kang.tutorial.domain.user.repository.user.UserRepository

@Suppress("SpringJavaAutowiredMembersInspection")
@AutoConfigureMockMvc
open class TestVariables {

    @Autowired
    protected lateinit var mvc: MockMvc
    @Autowired
    protected lateinit var objectMapper: ObjectMapper
    @Autowired
    protected lateinit var userRepository: UserRepository
    @Autowired
    protected lateinit var friendRepository: FriendRepository
    @Autowired
    protected lateinit var emailVerificationRepository: EmailVerificationRepository
    @Autowired
    protected lateinit var emailRequestLimiterRepository: EmailRequestLimiterRepository
    @Autowired
    protected lateinit var postRepository: PostRepository
    @Autowired
    protected lateinit var commentRepository: CommentRepository
    @Autowired
    protected lateinit var subCommentRepository: SubCommentRepository
    @Autowired
    protected lateinit var emojiRepository: EmojiRepository

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    protected val password = "qweasd123!"

    protected val user: User = User(
        email = "rkdtlsgml40@dsm.hs.kr",
        nickname = "user",
        password = passwordEncoder.encode(password)
    )

    protected val user2: User = User(
        email = "rkdtlsgml50@dsm.hs.kr",
        nickname = "user2",
        password = passwordEncoder.encode(password)
    )

    protected val signInRequest = SignInRequest(user.email, password)
    protected val signInRequest2 = SignInRequest(user2.email, password)

    protected var currentUserToken: String = ""
    protected var targetUserToken: String = ""

    protected var postId: Int = 0
}
