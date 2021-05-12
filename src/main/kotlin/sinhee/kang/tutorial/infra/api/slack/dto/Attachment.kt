package sinhee.kang.tutorial.infra.api.slack.dto

data class Attachment (
        val color: String,
        val pretext: String,
        val tile: String,
        val text: String,
        val footer: String,
        val ts: Long,
        val fields: MutableList<Field>
)
