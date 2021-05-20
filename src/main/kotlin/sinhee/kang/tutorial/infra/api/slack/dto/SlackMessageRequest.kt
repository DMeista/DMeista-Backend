package sinhee.kang.tutorial.infra.api.slack.dto

data class SlackMessageRequest(
        val attachments: MutableList<Attachment>
)
