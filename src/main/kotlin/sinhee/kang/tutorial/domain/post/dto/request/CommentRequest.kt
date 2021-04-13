package sinhee.kang.tutorial.domain.post.dto.request

import javax.validation.constraints.NotBlank

data class CommentRequest(
        @NotBlank
        var content: String
)