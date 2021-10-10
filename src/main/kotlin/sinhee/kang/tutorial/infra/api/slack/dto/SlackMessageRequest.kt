package sinhee.kang.tutorial.infra.api.slack.dto

data class SlackMessageRequest(
    val attachments: List<Attachment>
) {
    data class Attachment(
        val color: String = "#FF4444",

        val title: String = "[Server RuntimeError Report]",

        val ts: Long = System.currentTimeMillis(),

        val footer: String = "Bug Reporter",

        val fields: List<Field>
    )

    data class Field(
        val title: String,

        val value: String
    )
}
