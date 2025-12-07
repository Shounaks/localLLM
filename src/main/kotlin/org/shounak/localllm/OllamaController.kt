package org.shounak.localllm

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.collect
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OllamaController(
    chatModel: OllamaChatModel,
) {
    private val chatClient = ChatClient.create(chatModel)

    @PostMapping("/ollama/stream", produces = [TEXT_EVENT_STREAM_VALUE])
    fun stream(@RequestParam q: String): Flow<String> = channelFlow {

        // Heartbeat
        val heartbeatJob = launch {
            while (isActive) {
                send(": HEARTBEAT")
                delay(15_000)
            }
        }

        try {
            chatClient.prompt()
                .user(q)
                .stream()
                .content()
                .collect { token ->
                    send(token)
                }
        } finally {
            heartbeatJob.cancel()
        }

    }.onCompletion { cause ->
        if (cause == null) {
            emit("data: [DONE]\n\n")
        }
    }.catch { e ->
        if (e !is CancellationException) {
            emit("data: [ERROR] ${e.message?.take(200)}\n\n")
            emit("data: [DONE]\n\n")
        }
        throw e
    }
}