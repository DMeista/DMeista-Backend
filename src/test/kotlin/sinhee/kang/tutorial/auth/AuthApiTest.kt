package sinhee.kang.tutorial.auth

import org.springframework.boot.test.context.SpringBootTest
import sinhee.kang.tutorial.domain.post.domain.post.repository.PostRepository

@SpringBootTest
class AuthApiTest(
        private var postRepository: PostRepository
) {

}