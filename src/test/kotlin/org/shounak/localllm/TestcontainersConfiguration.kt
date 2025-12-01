package org.shounak.localllm

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.ollama.OllamaContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun ollamaContainer(): OllamaContainer {
        return OllamaContainer(DockerImageName.parse("ollama/ollama:latest"))
    }

}
