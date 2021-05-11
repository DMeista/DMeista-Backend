package sinhee.kang.tutorial

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import sinhee.kang.tutorial.domain.auth.domain.emailLimiter.repository.EmailLimiterRepository

import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.post.domain.comment.repository.CommentRepository
import sinhee.kang.tutorial.domain.post.domain.emoji.repository.EmojiRepository
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository
import sinhee.kang.tutorial.domain.post.domain.subComment.repository.SubCommentRepository
import sinhee.kang.tutorial.domain.user.domain.friend.repository.FriendRepository
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository

@AutoConfigureMockMvc
open class DiVariables {

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
    protected lateinit var emailLimiterRepository: EmailLimiterRepository
    @Autowired
    protected lateinit var postRepository: PostRepository
    @Autowired
    protected lateinit var commentRepository: CommentRepository
    @Autowired
    protected lateinit var subCommentRepository: SubCommentRepository
    @Autowired
    protected lateinit var emojiRepository: EmojiRepository

    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    protected val user: User = User(
        email = "rkdtlsgml40@dsm.hs.kr",
        nickname = "user",
        password = passwordEncoder.encode("1234")
    )

    protected val user2: User = User(
        email = "rkdtlsgml50@dsm.hs.kr",
        nickname = "user2",
        password =  passwordEncoder.encode("1234")
    )

    protected val signInRequest = SignInRequest(user.email, "1234")
    protected val signInRequest2 = SignInRequest(user2.email, "1234")

    protected var currentUserToken: String = ""
    protected var targetUserToken: String = ""

    protected var postId: Int = 0
}
