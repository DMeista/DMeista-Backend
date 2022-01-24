package sinhee.kang.tutorial.application.dto.request.comment

import javax.validation.constraints.NotBlank

data class CommentRequest(
    @field:NotBlank
    val content: String
)
