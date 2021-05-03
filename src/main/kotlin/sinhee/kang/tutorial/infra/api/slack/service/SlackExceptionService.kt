package sinhee.kang.tutorial.infra.api.slack.service

import javax.servlet.http.HttpServletRequest

interface SlackExceptionService {
    fun sendMessage(request: HttpServletRequest, exception: Exception)
}
