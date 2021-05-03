package sinhee.kang.tutorial

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class TutorialApplication

fun main(args: Array<String>) {
    runApplication<TutorialApplication>(*args)
}
