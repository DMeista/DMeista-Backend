package sinhee.kang.tutorial.infra.api.slack.dto.request

data class Attachment(
    val color: String,
    val title: String,
    val ts: Long,
    val footer: String,
    val fields: MutableList<Field>
)
