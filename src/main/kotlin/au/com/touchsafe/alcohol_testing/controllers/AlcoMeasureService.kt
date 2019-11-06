package au.com.touchsafe.alcohol_testing.controllers

import com.github.evanbennett.module.logError
import com.github.evanbennett.module.sendEmail
import io.ktor.application.log
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.route
import io.ktor.websocket.webSocket

typealias AlcoMeasureId = Int
typealias PersonId = Int

// TODO: When Access Control Board disconnects/is not connected: Send notification to TouchSafe; Send notification/email to Client; regarding alcohol testing not being undertaken.
@Suppress("MemberVisibilityCanBePrivate")
open class AlcoMeasureService : com.github.evanbennett.core.controllers.Controller("/services/alcoMeasure/") {

	override val auditLogLocation = Messages.ALCO_MEASURE_SERVICE
	override val reverseRoutes by lazy { ReverseRoutes(this) }
	protected val alcoMeasuresConnected = java.util.concurrent.ConcurrentHashMap<AlcoMeasureId, io.ktor.websocket.DefaultWebSocketServerSession>() // TODO
	protected val alcoMeasuresOutstandingTests = java.util.concurrent.ConcurrentHashMap<AlcoMeasureId, MutableMap<PersonId, java.time.LocalDateTime>>() // TODO

	override fun routes(route: io.ktor.routing.Route) {
		route {
			//			TODO: authenticate {
			route(pathPrefix) {
				webSocket("connect") { connect() }
			}
//			}
		}
	}

	suspend fun requestAlcoMeasureTest(alcoMeasureId: AlcoMeasureId, personId: PersonId, firstName: String, surname: String, call: io.ktor.application.ApplicationCall) {
		val session = alcoMeasuresConnected[alcoMeasureId]
		if (session == null) {
			call.logError("AlcoMeasureService.requestAlcoMeasureTest", "AlcoMeasure is not connected: [${alcoMeasureId}] [${personId}]")
			call.sendEmail("TODO", "TouchSafe Notification - AlcoMeasure Offline", "TODO") // TODO
		} else {
			val alcoMeasureOutstandingTests = alcoMeasuresOutstandingTests[alcoMeasureId]!!
			if (!alcoMeasureOutstandingTests.keys.contains(personId)) {
				alcoMeasureOutstandingTests += personId to java.time.LocalDateTime.now()
				session.send(io.ktor.http.cio.websocket.Frame.Text("Test;PersonId:$personId;FirstName:$firstName;Surname:$surname;"))
			}
		}
	}

	suspend fun io.ktor.websocket.DefaultWebSocketServerSession.connect() {
		var alcoMeasureId: AlcoMeasureId? = null

		try {
			for (frame in incoming) {
				when (frame) {
					is io.ktor.http.cio.websocket.Frame.Text -> {
						val message = frame.readText()
						when {
							message.startsWith(SERIAL_START) -> alcoMeasureId = initialiseConnection(message, this)
							message.startsWith(RESULT_START) -> storeResult(message)
							else -> call.application.log.error("Unrecognised Text Frame: [${message}]")
						}
					}
					is io.ktor.http.cio.websocket.Frame.Binary -> {
						val fileId = storePhoto(frame.data)
						outgoing.send(io.ktor.http.cio.websocket.Frame.Text("fileId:${fileId};"))
					}
				}
			}
		} catch (ex: Throwable) {
			call.application.log.error("Error occurred: ${closeReason.await()}", ex)
		}

		// Connection closed:
		alcoMeasuresConnected.remove(alcoMeasureId)
	}

	protected suspend fun initialiseConnection(message: String, session: io.ktor.websocket.DefaultWebSocketServerSession): AlcoMeasureId {
		val alcoMeasureId: AlcoMeasureId = 1 // TODO
		alcoMeasuresConnected[alcoMeasureId] = session
		if (alcoMeasuresOutstandingTests[alcoMeasureId] == null)
//			alcoMeasuresOutstandingTests[alcoMeasureId] = mutableMapOf() // TODO: THIS FUCKING FAILS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		return alcoMeasureId
	}

	protected suspend fun storeResult(message: String): Int {
		TODO()
	}

	protected suspend fun storePhoto(data: ByteArray): Long {
		TODO()
	}

	companion object {

		const val RESULT_START = "Result:"
		const val SERIAL_START = "Serial:"
	}

	@Suppress("unused")
	open class ReverseRoutes(override val controller: AlcoMeasureService) : com.github.evanbennett.core.controllers.ReverseRoutesI<AlcoMeasureService> {

		fun connect(): com.github.evanbennett.core.HttpAddress = com.github.evanbennett.core.HttpAddress(io.ktor.http.HttpMethod.Get, controller.pathPrefix + "connect")
	}
}
