package sinhee.kang.tutorial.infra.api.slack.dto.request

data class SlackMessageRequest(
    val attachments: MutableList<Attachment>
)
