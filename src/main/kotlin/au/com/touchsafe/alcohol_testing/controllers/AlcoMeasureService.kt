package au.com.touchsafe.alcohol_testing.controllers

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.websocket.webSocket

open class AlcoMeasureService : com.github.evanbennett.core.controllers.Controller("/services/alcoMeasure/") {

	override val auditLogLocation = Messages.ALCO_MEASURE_SERVICE
	override val reverseRoutes by lazy { ReverseRoutes(this) }
	protected val alcoMeasuresConnected = java.util.concurrent.ConcurrentHashMap<String, MutableList<io.ktor.http.cio.websocket.WebSocketSession>>() // TODO

	override fun routes(route: io.ktor.routing.Route) {
		route {
			//			TODO: authenticate {
			route(pathPrefix) {
				webSocket("connect") { connect() }
				post("uploadResult") { uploadResult(call) }
			}
//			}
		}
	}

	suspend fun io.ktor.websocket.DefaultWebSocketServerSession.connect() {
		try {
			for (frame in incoming) {
				when (frame) {
					is io.ktor.http.cio.websocket.Frame.Text -> {
						val text = frame.readText()
						outgoing.send(io.ktor.http.cio.websocket.Frame.Text("YOU SAID: $text"))
						if (text.equals("bye", ignoreCase = true)) {
							close(io.ktor.http.cio.websocket.CloseReason(io.ktor.http.cio.websocket.CloseReason.Codes.NORMAL, "Client said BYE"))
						}
					}
//		    		is io.ktor.http.cio.websocket.Frame.Binary -> {
//
//  				}
//  				is io.ktor.http.cio.websocket.Frame.Close -> {
//
//  				}
//  				is io.ktor.http.cio.websocket.Frame.Ping -> {
//
//	    			}
//	    			is io.ktor.http.cio.websocket.Frame.Pong -> {
//
//  				}
				}
			}
		} catch (ex: kotlinx.coroutines.channels.ClosedReceiveChannelException) {
			println("onClose ${closeReason.await()}")
		} catch (ex: Throwable) {
			println("onError ${closeReason.await()}")
			ex.printStackTrace()
		}
	}

	suspend fun uploadResult(call: io.ktor.application.ApplicationCall) {
		TODO()
	}

	open class ReverseRoutes(override val controller: AlcoMeasureService) : com.github.evanbennett.core.controllers.ReverseRoutesI<AlcoMeasureService> {

		fun connect(): com.github.evanbennett.core.HttpAddress = com.github.evanbennett.core.HttpAddress(io.ktor.http.HttpMethod.Get, controller.pathPrefix + "connect")
		fun uploadResult(): com.github.evanbennett.core.HttpAddress = com.github.evanbennett.core.HttpAddress(io.ktor.http.HttpMethod.Post, controller.pathPrefix + "uploadResult")
	}
}
