package org.shounak.localllm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LocalLlmApplication

fun main(args: Array<String>) {
    runApplication<LocalLlmApplication>(*args)
}
