package sinhee.kang.tutorial.domain.post.dto.request

import javax.validation.constraints.NotEmpty

class CommentRequest(
        @NotEmpty
        var content: String
)