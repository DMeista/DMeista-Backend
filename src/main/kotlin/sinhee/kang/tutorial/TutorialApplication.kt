package sinhee.kang.tutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class Tutorial2Application

fun main(args: Array<String>) {
    runApplication<Tutorial2Application>(*args)
}
