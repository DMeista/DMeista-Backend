package sinhee.kang.tutorial.infra.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.*
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket =
        Docket(DocumentationType.SWAGGER_2)
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("sinhee.kang.tutorial.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo())

    private fun apiInfo(): ApiInfo =
        ApiInfoBuilder()
            .version("v1")
            .title("DMeista")
            .description("API docs with Swagger2")
            .build()
}
