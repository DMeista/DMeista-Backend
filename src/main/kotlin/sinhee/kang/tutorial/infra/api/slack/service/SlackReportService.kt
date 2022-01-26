package sinhee.kang.tutorial.infra.api.slack.service

interface SlackReportService {
    fun sendMessage(exception: Exception)
}
