package sinhee.kang.tutorial.infra.api.slack.service

import javax.servlet.http.HttpServletRequest

interface SlackMessageService {
    fun sendMessage(request: HttpServletRequest, exception: Exception)
}
