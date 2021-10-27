package sinhee.kang.tutorial

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.auth.entity.verification.EmailVerification
import sinhee.kang.tutorial.domain.user.domain.friend.enums.FriendStatus
import sinhee.kang.tutorial.domain.user.entity.user.User
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig
import java.util.*
import javax.servlet.http.Cookie

@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test", "local")
class TestFactory: TestVariables() {

    protected fun requestBody(
        method: MockHttpServletRequestBuilder,
        obj: Any? = null,
        token: String = ""
    ): ResultActions =
        mvc.perform(method
            .header("Authorization", token)
            .content(objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .writeValueAsString(obj))
            .contentType(MediaType.APPLICATION_JSON_VALUE))

    protected fun requestBody(
        method: MockHttpServletRequestBuilder,
        obj: Any? = null,
        cookie: Cookie?
    ): ResultActions =
        mvc.perform(method
            .cookie(cookie)
            .content(objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .writeValueAsString(obj))
            .contentType(MediaType.APPLICATION_JSON_VALUE))

    protected fun requestParams(
        method: MockHttpServletRequestBuilder,
        params: MultiValueMap<String, String>,
        token: String = ""
    ): ResultActions =
        mvc.perform(method
            .header("Authorization", token)
            .params(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE))

    protected fun emailVerify(user: User) {
        emailVerificationRepository.save(
            EmailVerification(
                id = UUID.randomUUID(),
                email = user.email,
                "AUTH-CODE",
                expired = true
            ))
    }

    protected fun getAccessToken(signInRequest: SignInRequest): String {
        val content = requestBody(post("/auth/login"), signInRequest)
            .andReturn().response.contentAsString
        val tokenResponse = mappingResponse(content, TokenResponse::class.java) as TokenResponse

        return "Bearer ${tokenResponse.accessToken}"
    }

    protected fun getRefreshToken(signInRequest: SignInRequest): Cookie? {
        return requestBody(post("/auth/login"), signInRequest)
            .andReturn().response.cookies.first { it.name == "_Refresh" }
    }

    protected fun isCheckUserAndTargetUserExist(user: User, targetUser: User) =
        friendRepository.existsByUserAndTargetUser(user, targetUser)

    protected fun isConnect(user1: User, user2: User, status: FriendStatus): Boolean =
        friendRepository.existsByUserAndTargetUserAndStatus(user1, user2, status) ||
        friendRepository.existsByUserAndTargetUserAndStatus(user2, user1, status)

    protected fun mappingResponse(obj: String, cls: Class<*>): Any =
        objectMapper
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .readValue(obj, cls)
}
