package sinhee.kang.tutorial.global.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import sinhee.kang.tutorial.domain.auth.controller.AuthController
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.builders.ResponseMessageBuilder
import springfox.documentation.schema.ModelRef
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ResponseMessage
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import kotlin.collections.ArrayList
import java.util.HashSet

@Configuration
@EnableSwagger2
class SwaggerConfig {
    private val title: String = "DMeista"

    @Bean
    fun authInfo(): Docket {
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
            .consumes(getConsumeContentTypes())
            .produces(getProduceContentTypes())
            .apiInfo(apiInfo(title))
            .select()
            .apis(RequestHandlerSelectors.basePackage("sinhee.kang.tutorial"))
            .paths(PathSelectors.ant("/**"))
            .build()
            .useDefaultResponseMessages(false)
            .globalResponseMessage(RequestMethod.GET, messageBuilder)
    }

    private fun getConsumeContentTypes(): Set<String> {
        val consumes: MutableSet<String> = HashSet()
        consumes.apply {
            add("application/json;charset=UTF-8")
            add("application/x-www-form-urlencoded")
        }
        return consumes
    }

    private fun getProduceContentTypes(): Set<String> {
        val produces: MutableSet<String> = HashSet()
        produces.add("application/json;charset=UTF-8")
        return produces
    }

    private fun apiInfo(title: String): ApiInfo {
        return ApiInfoBuilder()
                .title(title)
                .version("v1")
                .description("API docs with Swagger2")
                .build()
    }
}