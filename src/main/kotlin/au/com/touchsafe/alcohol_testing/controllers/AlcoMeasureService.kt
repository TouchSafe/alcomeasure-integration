package au.com.touchsafe.alcohol_testing.controllers

import com.github.evanbennett.module.logError
import com.github.evanbennett.module.sendEmail
import io.ktor.application.log
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.route
import io.ktor.websocket.webSocket
import java.lang.RuntimeException

typealias AccessControlBoardId = Int
typealias PersonId = Int

// TODO: When an Access Control Board disconnects/is not connected: Send a notification to TouchSafe; Send a notification/email to Client; regarding alcohol testing not being undertaken.
@Suppress("MemberVisibilityCanBePrivate")
open class AlcoMeasureService : com.github.evanbennett.core.controllers.Controller("/services/alcoMeasure/") {

	override val auditLogLocation = Messages.ALCO_MEASURE_SERVICE
	override val reverseRoutes by lazy { ReverseRoutes(this) }
	protected val accessControlBoardsConnected = java.util.concurrent.ConcurrentHashMap<AccessControlBoardId, io.ktor.websocket.DefaultWebSocketServerSession>()
	protected val accessControlBoardsOutstandingTests = java.util.concurrent.ConcurrentHashMap<AccessControlBoardId, MutableMap<PersonId, java.time.LocalDateTime>>()

	override fun routes(route: io.ktor.routing.Route) {
		route {
			//			TODO: authenticate {
			route(pathPrefix) {
				webSocket("connect") { connect() }
			}
//			}
		}
	}

	suspend fun requestAlcoMeasureTest(accessControlBoardId: AccessControlBoardId, personId: PersonId, firstName: String, surname: String, call: io.ktor.application.ApplicationCall) {
		val session = accessControlBoardsConnected[accessControlBoardId]
		if (session == null) {
			call.logError("AlcoMeasureService.requestAlcoMeasureTest", "AlcoMeasure is not connected: [${accessControlBoardId}] [${personId}]")
			call.sendEmail("TODO", "TouchSafe Notification - AlcoMeasure Offline", "TODO") // TODO
		} else {
			val accessControlBoardOutstandingTests = accessControlBoardsOutstandingTests[accessControlBoardId]!!
			if (!accessControlBoardOutstandingTests.keys.contains(personId)) {
				accessControlBoardOutstandingTests += personId to java.time.LocalDateTime.now()
				session.outgoing.send(io.ktor.http.cio.websocket.Frame.Text("$TEST_START$personId;$firstName;$surname"))
			}
		}
	}

	suspend fun io.ktor.websocket.DefaultWebSocketServerSession.connect() {
		var accessControlBoardId: AccessControlBoardId? = null

		try {
			for (frame in incoming) {
				when (frame) {
					is io.ktor.http.cio.websocket.Frame.Text -> {
						val message = frame.readText()
						when {
							message.startsWith(UNIQUE_IDENTIFIER_START) -> {
								accessControlBoardId = initialiseConnection(message, call, this)
								outgoing.send(io.ktor.http.cio.websocket.Frame.Text(SERIAL_NUMBER_START + loadSerialNumber(accessControlBoardId, call)))
							}
							message.startsWith(RESULT_START) -> {
								if (accessControlBoardId == null) throw RuntimeException("Trying to store a result without initialising the connection!!!")
								storeResult(message, accessControlBoardId, call)
							}
							else -> call.application.log.error("Unrecognised Text Frame: [${message}]")
						}
					}
					is io.ktor.http.cio.websocket.Frame.Binary -> {
						val fileFactory: com.github.evanbennett.module.models.generated.FileFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
						val fileId = fileFactory.insert(frame.data, call).long
						outgoing.send(io.ktor.http.cio.websocket.Frame.Text(FILE_ID_START + fileId))
					}
				}
			}
		} catch (ex: Throwable) {
			call.application.log.error("Error occurred: ${closeReason.await()}", ex)
		}

		// Connection closed:
		if (accessControlBoardId != null) accessControlBoardsConnected.remove(accessControlBoardId)
	}

	protected suspend fun initialiseConnection(message: String, call: io.ktor.application.ApplicationCall, session: io.ktor.websocket.DefaultWebSocketServerSession): AccessControlBoardId {
		val uniqueIdentifierString = message.substring(UNIQUE_IDENTIFIER_START.length)
		val accessControlBoardFactory: au.com.touchsafe.access_control_common.models.generated.AccessControlBoardFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val uniqueIdentifier = accessControlBoardFactory.COLUMNS.UNIQUE_IDENTIFIER.DATA_TYPE_SINGLETON(uniqueIdentifierString)
		val accessControlBoard = accessControlBoardFactory.loadWithUniqueUniqueIdentifier(accessControlBoardFactory.COLUMNS.UNIQUE_IDENTIFIER.field(uniqueIdentifier), call) ?: throw RuntimeException("Access Control Board not found with `uniqueIdentifier`: [$uniqueIdentifierString]")
		val accessControlBoardId = accessControlBoard.accessControlBoardId.value!!.integer
		accessControlBoardsConnected[accessControlBoardId] = session
		if (accessControlBoardsOutstandingTests[accessControlBoardId] == null) accessControlBoardsOutstandingTests[accessControlBoardId] = mutableMapOf<PersonId, java.time.LocalDateTime>()
		return accessControlBoardId
	}

	protected suspend fun loadSerialNumber(accessControlBoardId: AccessControlBoardId, call: io.ktor.application.ApplicationCall): Int {
		val accessControlBoardVersionFactory: au.com.touchsafe.access_control_common.models.generated.AccessControlBoardVersionFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val accessControlBoardIdColumn = accessControlBoardVersionFactory.COLUMNS.ACCESS_CONTROL_BOARD_ID
		val accessControlBoardVersions = accessControlBoardVersionFactory.loadReferableWithWhereCondition("${accessControlBoardIdColumn.FULL_NAME} = ?", null, listOf(accessControlBoardIdColumn.field(accessControlBoardIdColumn.DATA_TYPE_SINGLETON(accessControlBoardId.toString()))), call)
		val accessControlBoardVersion = accessControlBoardVersions.singleOrNull() ?: throw RuntimeException("Referable Access Control Board Version not found with `accessControlBoardId`: [$accessControlBoardId]")
		return accessControlBoardVersion.alcoMeasureSerial.value?.integer ?: throw RuntimeException("Referable Access Control Board Version found with `accessControlBoardId` but it does not have an `alcoMeasureSerial`: [$accessControlBoardId]")
	}

	protected suspend fun storeResult(message: String, _accessControlBoardId: AccessControlBoardId, call: io.ktor.application.ApplicationCall): Int {
		val messageParts = message.split(';')
		if (messageParts.size != 6) throw RuntimeException("Invalid store result frame: [$message]")
		val alcoMeasureResultFactory: au.com.touchsafe.alcohol_testing.models.generated.AlcoMeasureResultFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val alcoMeasureResultId = alcoMeasureResultFactory.COLUMNS.ALCO_MEASURE_RESULT_ID.field()
		val accessControlBoardId = alcoMeasureResultFactory.COLUMNS.ACCESS_CONTROL_BOARD_ID.field(alcoMeasureResultFactory.COLUMNS.ACCESS_CONTROL_BOARD_ID.DATA_TYPE_SINGLETON(_accessControlBoardId.toString()))
		val personId = alcoMeasureResultFactory.COLUMNS.PERSON_ID.field(alcoMeasureResultFactory.COLUMNS.PERSON_ID.DATA_TYPE_SINGLETON(messageParts[1]))
		val result = alcoMeasureResultFactory.COLUMNS.RESULT.field(alcoMeasureResultFactory.COLUMNS.RESULT.DATA_TYPE_SINGLETON(messageParts[2]))
		val photo1FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.DATA_TYPE_SINGLETON(messageParts[3]))
		val photo2FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.DATA_TYPE_SINGLETON(messageParts[4]))
		val photo3FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.DATA_TYPE_SINGLETON(messageParts[5]))
		val tested = alcoMeasureResultFactory.COLUMNS.TESTED.field()
		val alcoMeasureResult = alcoMeasureResultFactory(alcoMeasureResultId, accessControlBoardId, personId, result, photo1FileId, photo2FileId, photo3FileId, tested).insert(call)
		return alcoMeasureResult.alcoMeasureResultId.value!!.integer
	}

	companion object {

		const val FILE_ID_START = "fileId:"
		const val RESULT_START = "Result;"
		const val SERIAL_NUMBER_START = "serialNumber:"
		const val TEST_START = "Test;"
		const val UNIQUE_IDENTIFIER_START = "uniqueIdentifier:"
	}

	@Suppress("unused")
	open class ReverseRoutes(override val controller: AlcoMeasureService) : com.github.evanbennett.core.controllers.ReverseRoutesI<AlcoMeasureService> {

		fun connect(): com.github.evanbennett.core.HttpAddress = com.github.evanbennett.core.HttpAddress(io.ktor.http.HttpMethod.Get, controller.pathPrefix + "connect")
	}
}
