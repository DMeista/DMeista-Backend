package sinhee.kang.tutorial.domain.post.dto.request

import javax.validation.constraints.NotBlank

data class PostRequest(
        var title: String,
        @NotBlank
        var content: String,

        var tags: String?
)