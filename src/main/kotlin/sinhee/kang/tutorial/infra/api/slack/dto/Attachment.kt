package sinhee.kang.tutorial.infra.api.slack.dto

class Attachment(
        var color: String,
        var pretext: String,
        var tile: String,
        var text: String,
        var footer: String,
        var ts: Long,
        var fields: MutableList<Field>
)