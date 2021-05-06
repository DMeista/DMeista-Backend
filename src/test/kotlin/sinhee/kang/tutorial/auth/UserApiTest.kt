package sinhee.kang.tutorial.auth

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import sinhee.kang.tutorial.ApiTest
import sinhee.kang.tutorial.domain.auth.domain.verification.EmailVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus
import sinhee.kang.tutorial.domain.auth.domain.verification.repository.EmailVerificationRepository
import sinhee.kang.tutorial.domain.auth.dto.request.*
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.domain.user.domain.user.repository.UserRepository
import sinhee.kang.tutorial.domain.user.dto.response.UserInfoResponse

class UserApiTest: ApiTest() {
    @Autowired
    private lateinit var emailVerificationRepository: EmailVerificationRepository
    @Autowired
    private lateinit var userRepository: UserRepository

    private val user: User = User(
        email = "rkdtlsgml40@dsm.hs.kr",
        nickname = "user",
        password = passwordEncoder.encode("1234")
    )

    @BeforeEach
    fun setup() {
        emailVerify()
    }

    @AfterEach
    fun clean() {
        emailVerificationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun signUpTest() {
        requestBody(post("/users"), SignUpRequest(user.email, "1234", user.nickname))
        userRepository.findByEmail(user.email)
            ?.let { assert(it.nickname == user.nickname) }
            ?: throw Exception()
    }

    @Test
    fun changePasswordTest() {
        userRepository.save(user)
        requestBody(put("/users/password"), ChangePasswordRequest(user.email, "4321"))
    }

    @Test
    fun exitAccountTest() {
        userRepository.save(user)
        val cookie = login(SignInRequest(user.email, "1234"))
        val request = ChangePasswordRequest(user.email, "1234")
        requestBody(delete("/users"), request, cookie)
    }

    @Test
    fun userInfoTest() {
        userRepository.save(user)
        val cookie = login(SignInRequest(user.email, "1234"))
        val userInfo = requestBody(get("/users"), cookie = cookie)
        val response = mappingResponse(userInfo, UserInfoResponse::class.java) as UserInfoResponse
        assert(response.username == user.nickname)
    }

    private fun emailVerify() {
        emailVerificationRepository.save(EmailVerification(
                email = user.email,
                authCode = "AUTH_CODE",
                status = EmailVerificationStatus.UNVERIFID
        ))
        requestBody(put("/users/email/verify"), VerifyCodeRequest(user.email, "AUTH_CODE"))
    }
}
