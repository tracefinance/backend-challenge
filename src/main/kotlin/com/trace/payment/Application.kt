package com.trace.payment

import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        jackson()
    }
    
    routing {
        get("/health") {
            call.respond(mapOf("status" to "OK"))
        }
        
        // TODO: Implementar rotas da API de pagamentos
        route("/wallets") {
            post {
                call.respond(mapOf("message" to "Criar carteira - TODO"))
            }
            
            get("/{walletId}/limits") {
                call.respond(mapOf("message" to "Consultar limites - TODO"))
            }
            
            put("/{walletId}/policy") {
                call.respond(mapOf("message" to "Associar política - TODO"))
            }
            
            post("/{walletId}/payments") {
                call.respond(mapOf("message" to "Realizar pagamento - TODO"))
            }
            
            get("/{walletId}/payments") {
                call.respond(mapOf("message" to "Listar pagamentos - TODO"))
            }
        }
        
        route("/policies") {
            post {
                call.respond(mapOf("message" to "Criar política - TODO"))
            }
            
            get {
                call.respond(mapOf("message" to "Listar políticas - TODO"))
            }
        }
    }
}
