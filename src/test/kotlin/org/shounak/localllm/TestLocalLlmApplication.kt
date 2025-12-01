package org.shounak.localllm

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
    fromApplication<LocalLlmApplication>().with(TestcontainersConfiguration::class).run(*args)
}
