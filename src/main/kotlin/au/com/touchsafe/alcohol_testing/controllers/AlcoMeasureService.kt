package au.com.touchsafe.alcohol_testing.controllers

import com.github.evanbennett.module.logError
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.routing.route
import io.ktor.websocket.webSocket

typealias AccessControllerId = Int
typealias PersonId = Int

// TODO: When an Access Controller disconnects/is not connected: Send a notification to TouchSafe; Send a notification/email to Client; regarding alcohol testing not being undertaken.
@Suppress("MemberVisibilityCanBePrivate")
open class AlcoMeasureService : com.github.evanbennett.core.controllers.Controller("/services/alcoMeasure/") {

	override val auditLogLocation = Messages.ALCO_MEASURE_SERVICE
	override val reverseRoutes by lazy { ReverseRoutes(this) }
	protected val accessControllersConnected = java.util.concurrent.ConcurrentHashMap<AccessControllerId, io.ktor.websocket.DefaultWebSocketServerSession>()

	override fun routes(route: io.ktor.routing.Route) {
		route {
			route(pathPrefix) {
				webSocket("connect") { connect() }
			}
		}
	}

	suspend fun requestAlcoMeasureTest(accessControllerId: AccessControllerId, personId: PersonId, firstName: String, surname: String, call: io.ktor.application.ApplicationCall) {
		val session = accessControllersConnected[accessControllerId]
		if (session == null) {
			call.logError("AlcoMeasureService.requestAlcoMeasureTest", "AlcoMeasure is not connected: [${accessControllerId}] [${personId}]")
		} else {
			session.send("${au.com.touchsafe.access_control_common.AlcoMeasure.TEST_START}$personId;$firstName;$surname")
		}
	}

	suspend fun io.ktor.websocket.DefaultWebSocketServerSession.connect() {
		val accessControllerFactory: au.com.touchsafe.access_control_common.models.generated.AccessControllerFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val accessControllerFactoryVersion: au.com.touchsafe.access_control_common.models.generated.AccessControllerVersionFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val fileFactory: com.github.evanbennett.module.models.generated.FileFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		var accessControllerId: AccessControllerId? = null
		try {
			val uniqueIdentifierStart = "${accessControllerFactory.COLUMNS.UNIQUE_IDENTIFIER.COLUMN_NAME}:"
			for (frame in incoming) {
				when (frame) {
					is io.ktor.http.cio.websocket.Frame.Text -> {
						val message = frame.readText()
						when {
							message.startsWith(uniqueIdentifierStart) -> {
								val uniqueIdentifierString = message.substring(uniqueIdentifierStart.length)
								val uniqueIdentifier = accessControllerFactory.COLUMNS.UNIQUE_IDENTIFIER.DATA_TYPE_SINGLETON(uniqueIdentifierString)
								val accessController = accessControllerFactory.loadWithUniqueUniqueIdentifier(accessControllerFactory.COLUMNS.UNIQUE_IDENTIFIER.field(uniqueIdentifier), call) ?: throw RuntimeException("Access Controller not found with `uniqueIdentifier`: [$uniqueIdentifierString]")
								accessControllerId = accessController.accessControllerId.value!!.integer
								accessControllersConnected[accessControllerId] = this
								val accessControllerVersion = accessControllerFactoryVersion.loadReferable(accessControllerId, call)
								val alcoMeasureSerial = accessControllerVersion.alcoMeasureSerial.value?.integer ?: throw RuntimeException("Referable Access Controller Version found with `accessControllerId` but it does not have an `alcoMeasureSerial`: [$accessControllerId]")
								send(au.com.touchsafe.access_control_common.AlcoMeasure.SERIAL_NUMBER_START + alcoMeasureSerial)
							}
							message.startsWith(au.com.touchsafe.access_control_common.AlcoMeasure.RESULT_START) -> {
								if (accessControllerId == null) throw RuntimeException("Trying to store a result without initialising the connection!!!")
								storeResult(message, accessControllerId, call)
							}
							else -> call.logError("AlcoMeasureService.connect", "Unrecognised Text Frame: [${message}]")
						}
					}
					is io.ktor.http.cio.websocket.Frame.Binary -> {
						val fileId = fileFactory.insert(frame.data, call).long
						send(au.com.touchsafe.access_control_common.AlcoMeasure.FILE_ID_START + fileId)
					}
				}
			}
		} catch (ex: Throwable) {
			call.logError("AlcoMeasureService.connect", "Error occurred: ${closeReason.await()}", ex)
		}

		// Connection closed:
		if (accessControllerId != null) accessControllersConnected.remove(accessControllerId)
	}

	protected suspend fun storeResult(message: String, _accessControllerId: AccessControllerId, call: io.ktor.application.ApplicationCall): Int {
		val messageParts = message.split(';')
		if (messageParts.size != 6) throw RuntimeException("Invalid store result frame: [$message]")
		val alcoMeasureResultFactory: au.com.touchsafe.alcohol_testing.models.generated.AlcoMeasureResultFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val alcoMeasureResultId = alcoMeasureResultFactory.COLUMNS.ALCO_MEASURE_RESULT_ID.field()
		val accessControllerId = alcoMeasureResultFactory.COLUMNS.ACCESS_CONTROLLER_ID.field(alcoMeasureResultFactory.COLUMNS.ACCESS_CONTROLLER_ID.DATA_TYPE_SINGLETON(_accessControllerId.toString()))
		val personId = alcoMeasureResultFactory.COLUMNS.PERSON_ID.field(alcoMeasureResultFactory.COLUMNS.PERSON_ID.DATA_TYPE_SINGLETON(messageParts[1]))
		val result = alcoMeasureResultFactory.COLUMNS.RESULT.field(alcoMeasureResultFactory.COLUMNS.RESULT.DATA_TYPE_SINGLETON(messageParts[2]))
		val photo1FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.DATA_TYPE_SINGLETON(messageParts[3]))
		val photo2FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.DATA_TYPE_SINGLETON(messageParts[4]))
		val photo3FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.DATA_TYPE_SINGLETON(messageParts[5]))
		val tested = alcoMeasureResultFactory.COLUMNS.TESTED.field()
		val alcoMeasureResult = alcoMeasureResultFactory(alcoMeasureResultId, accessControllerId, personId, result, photo1FileId, photo2FileId, photo3FileId, tested).insert(call)
		return alcoMeasureResult.alcoMeasureResultId.value!!.integer
	}

	@Suppress("unused")
	open class ReverseRoutes(override val controller: AlcoMeasureService) : com.github.evanbennett.core.controllers.ReverseRoutesI<AlcoMeasureService> {

		fun connect(): com.github.evanbennett.core.HttpAddress = com.github.evanbennett.core.HttpAddress(io.ktor.http.HttpMethod.Get, controller.pathPrefix + "connect")
	}
}
