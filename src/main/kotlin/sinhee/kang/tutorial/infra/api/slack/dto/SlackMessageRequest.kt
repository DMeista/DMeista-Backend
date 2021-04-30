package sinhee.kang.tutorial.infra.api.slack.dto

data class SlackMessageRequest (
    var attachments: MutableList<Attachment>
)
