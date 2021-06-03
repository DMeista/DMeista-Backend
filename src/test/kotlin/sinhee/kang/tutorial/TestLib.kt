package sinhee.kang.tutorial

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.MultiValueMap
import sinhee.kang.tutorial.domain.auth.domain.verification.SignUpVerification
import sinhee.kang.tutorial.domain.auth.domain.verification.enums.EmailVerificationStatus

import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.domain.user.domain.user.User
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig
import javax.servlet.http.Cookie

@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test", "local")
class TestLib: CombineVariables() {

    protected fun requestBody(
        method: MockHttpServletRequestBuilder,
        obj: Any? = null
    ): ResultActions =
        mvc.perform(method
            .content(objectMapper
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .writeValueAsString(obj))
            .contentType(MediaType.APPLICATION_JSON_VALUE))

    protected fun requestBody(
        method: MockHttpServletRequestBuilder,
        obj: Any? = null,
        token: String?
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
        token: String?
    ): ResultActions =
        mvc.perform(method
            .header("Authorization", token)
            .params(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE))

    protected fun requestBody1(method: MockHttpServletRequestBuilder, obj: Any): String {
        return mvc.perform(
            method
                .content(ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
    }

    protected fun requestBody1(method: MockHttpServletRequestBuilder, obj: Any? = null, token: String?): String {
        return mvc.perform(
            method
                .header("Authorization", token)
                .content(ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
    }

    protected fun getAccessToken(signInRequest: SignInRequest): String {
        val content = requestBody(post("/auth"), signInRequest)
            .andReturn().response.contentAsString
        val tokenResponse = mappingResponse(content, TokenResponse::class.java) as TokenResponse

        return "Bearer ${tokenResponse.accessToken}"
    }

    protected fun getRefreshToken(signInRequest: SignInRequest): Cookie? {
        return requestBody(post("/auth"), signInRequest)
            .andReturn().response.cookies.first { it.name == "_Refresh" }
    }

    protected fun emailVerify(user: User) {
        signUpVerificationRepository.save(
            SignUpVerification(
                email = user.email,
                authCode = "AUTH-CODE",
                emailStatus = EmailVerificationStatus.VERIFIED,
                nickname = user.nickname
            )
        )
    }

    protected fun generatePost(
        method: MockHttpServletRequestBuilder = post("/posts"),
        title: String = "title",
        content: String = "content",
        tags: String = "#tag",
        token: String?
    ): Int =
        mvc.perform(
            method
                .header("Authorization", token)
                .param("title", title)
                .param("content", content)
                .param("tags", tags))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString.toInt()

    protected fun mappingResponse(obj: String, cls: Class<*>): Any {
        return objectMapper
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .readValue(obj, cls)
    }
}
