package sinhee.kang.tutorial.global.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*
import kotlin.collections.ArrayList

@Configuration
@EnableSwagger2
class SwaggerConfig {
    private lateinit var title: String

    @Bean
    fun authInfo(): Docket {
        title = "tut_kotlin_springboot"

        val messageBuilder: MutableList<ResponseMessage> = ArrayList()
        messageBuilder.add(ResponseMessageBuilder()
                .code(500)
                .message("Internal Server Error")
                .responseModel(ModelRef("Error"))
                .build()
        )
        messageBuilder.add(ResponseMessageBuilder()
                .code(400)
                .message("Bad Request")
                .build()
        )
        messageBuilder.add(ResponseMessageBuilder()
                .code(404)
                .message("Not Found")
                .build()
        )
        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(title))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, messageBuilder)
    }

    private fun apiInfo(title: String): ApiInfo {
        return ApiInfoBuilder()
                .title(title)
                .version("v2")
                .description("tut_Kotlin_Springboot API docs with Swagger2")
                .build()
    }
}