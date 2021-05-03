package sinhee.kang.tutorial.infra.api.slack

import retrofit2.Call
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface SlackApi {
    @POST("T01HGFH8JEL/B01HGQ64BS4/YMlZwoU7D6szvY9D1CPbIRm7")
    fun sendMessage(
        @Body body: RequestBody
    ): Call<Void>
}
