package sinhee.kang.tutorial.global.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*


@Configuration
@EnableSwagger2
class SwaggerConfig {
    private lateinit var group: String
    private lateinit var title: String

    @Bean
    fun authInfo(): Docket {
        group = "auth"
        title = "tut_kotlin_springboot $group"

        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(group)
                .select()
                .apis(RequestHandlerSelectors.basePackage("sinhee.kang.tutorial.domain.$group"))
                .paths(PathSelectors.ant("/$group/controller/**"))
                .build()
                .apiInfo(apiInfo(title, group))
    }

    @Bean
    fun fileInfo(): Docket {
        group = "file"
        title = "tut_kotlin_springboot $group"

        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(group)
                .select()
                .apis(RequestHandlerSelectors.basePackage("sinhee.kang.tutorial.domain.$group"))
                .paths(PathSelectors.ant("/$group/controller/**"))
                .build()
                .apiInfo(apiInfo(title, group))
    }

    @Bean
    fun postInfo(): Docket {
        group = "post"
        title = "tut_kotlin_springboot $group"

        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(group)
                .select()
                .apis(RequestHandlerSelectors.basePackage("sinhee.kang.tutorial.domain.$group"))
                .paths(PathSelectors.ant("/$group/controller/**"))
                .build()
                .apiInfo(apiInfo(title, group))
    }

    @Bean
    fun userInfo(): Docket {
        group = "user"
        title = "tut_kotlin_springboot $group"

        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(group)
                .select()
                .apis(RequestHandlerSelectors.basePackage("sinhee.kang.tutorial.domain.$group"))
                .paths(PathSelectors.ant("/$group/controller/**"))
                .build()
                .apiInfo(apiInfo(title, group))
    }

    private fun apiInfo(title: String, group: String): ApiInfo {
        return ApiInfo(
                title,
                "tut_kotlin_springboot API Docs made by Swagger2",
                group,
                "",
                Contact("", "", ""),
                "Licenses",
                "",
                ArrayList())
    }
}