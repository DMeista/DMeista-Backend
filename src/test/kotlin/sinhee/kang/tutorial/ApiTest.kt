package sinhee.kang.tutorial

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest
import sinhee.kang.tutorial.domain.auth.dto.response.TokenResponse
import sinhee.kang.tutorial.infra.redis.EmbeddedRedisConfig
import javax.transaction.Transactional

@SpringBootTest(classes = [TutorialApplication::class, EmbeddedRedisConfig::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test", "local")
class ApiTest() {

    @Autowired
    protected lateinit var mvc: MockMvc
    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    protected final val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()


    protected fun requestBody(method: MockHttpServletRequestBuilder, obj: Any? = null, token: String? = ""): String {
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


    protected fun getToken(tokenType: TokenType, email: String, password: String): String {
        val content = requestBody(post("/auth"), SignInRequest(email, password))
        val response = mappingResponse(content, TokenResponse::class.java) as TokenResponse

        return when (tokenType) {
            TokenType.ACCESS -> response.accessToken
            TokenType.REFRESH -> response.refreshToken
        }
    }


    protected fun generatePost(
        method: MockHttpServletRequestBuilder = post("/posts"),
        title: String = "title",
        content: String = "content",
        tags: String = "#tag",
        token: String
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