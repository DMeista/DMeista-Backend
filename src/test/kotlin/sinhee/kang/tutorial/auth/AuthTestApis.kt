package sinhee.kang.tutorial.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.servlet.http.Cookie

import sinhee.kang.tutorial.TestApis
import sinhee.kang.tutorial.domain.auth.dto.request.SignInRequest

class AuthTestApis: TestApis() {

    @BeforeEach
    fun setup() {
        userRepository.save(user)
    }

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun signInTest() {
        requestBody(post("/auth"), SignInRequest(user.email, "1234"))
    }

    @Test
    fun extendTokenTest() {
        requestBody(put("/auth"), cookie = getRefreshToken(SignInRequest(user.email, "1234")))
    }

    private fun getRefreshToken(signInRequest: SignInRequest): Cookie? {
        return mvc.perform(
            post("/auth")
                .content(objectMapper
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .writeValueAsString(signInRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn().response.cookies.first { it.name == "_Refresh" }
    }

    private fun requestBody(method: MockHttpServletRequestBuilder, obj: Any? = null, cookie: Cookie?): String {
        return mvc.perform(
            method
                .content(
                    ObjectMapper()
                    .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                    .writeValueAsString(obj))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .cookie(cookie))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn().response.contentAsString
    }
}
