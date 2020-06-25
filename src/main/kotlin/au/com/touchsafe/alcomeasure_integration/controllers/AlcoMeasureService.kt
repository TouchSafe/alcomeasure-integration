package au.com.touchsafe.alcomeasure_integration.controllers

import com.github.evanbennett.module.logError
import io.ktor.application.call
import io.ktor.request.isMultipart
import io.ktor.request.receiveMultipart
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route

@Suppress("MemberVisibilityCanBePrivate")
open class AlcoMeasureService : com.github.evanbennett.core.controllers.Controller("/services/alcoMeasure/") {

	override val auditLogLocation = Messages.ALCO_MEASURE_SERVICE
	override val reverseRoutes by lazy { ReverseRoutes(this) }

	override fun routes(route: io.ktor.routing.Route) {
		route {
			route(pathPrefix) {
				post("uploadResult") { uploadResult(call) }
			}
		}
	}

	suspend fun requestAlcoMeasureTest(googleCloudDeviceId: String, alcoMeasureTest: au.com.touchsafe.access_control_common.models.access_controller.AlcoMeasureTest, call: io.ktor.application.ApplicationCall) {
		// FIXME: Send an IoT Core Command:
//		val session = accessControllersConnected[accessControllerId]
//		if (session == null) {
//			call.logError("AlcoMeasureService.requestAlcoMeasureTest", "AlcoMeasure is not connected: [${accessControllerId}] [${personId}]")
//		} else {
//			session.send("${au.com.touchsafe.access_control_common.AlcoMeasure.TEST_START}$personId;$firstName;$surname")
//		}
	}

	protected suspend fun uploadResult(call: io.ktor.application.ApplicationCall) {
		// TODO: This needs some sort of authentication.
		val multipart = call.receiveMultipart()
		if (!call.request.isMultipart()) {
			call.logError("AlcoMeasureService.uploadResult", "Request is NOT multipart.")
			call.respond(io.ktor.http.HttpStatusCode.UnsupportedMediaType)
		} else {
			val accessControllerFactory: au.com.touchsafe.access_control_common.models.generated.AccessControllerFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
			val alcoMeasureResultFactory: au.com.touchsafe.alcomeasure_integration.models.generated.AlcoMeasureResultFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
			var accessControllerId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>? = null
			var personId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>? = null
			var result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>? = null
			var tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>? = null
			var photo1FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>? = null
			var photo2FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>? = null
			var photo3FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>? = null
			var part = multipart.readPart()
			while (part != null) {
				when (part) {
					is io.ktor.http.content.PartData.FormItem -> when (part.name) {
						"googleCloudDeviceId" -> {
							val googleCloudDeviceId = accessControllerFactory.COLUMNS.GOOGLE_CLOUD_DEVICE_ID.field(accessControllerFactory.COLUMNS.GOOGLE_CLOUD_DEVICE_ID.DATA_TYPE_SINGLETON(part.value))
							accessControllerId = accessControllerFactory.loadWithUniqueGoogleCloudDevice(googleCloudDeviceId, call)?.accessControllerId
						}
						"personId" -> personId = alcoMeasureResultFactory.COLUMNS.PERSON_ID.field(alcoMeasureResultFactory.COLUMNS.PERSON_ID.DATA_TYPE_SINGLETON(part.value))
						"result" -> result = alcoMeasureResultFactory.COLUMNS.RESULT.field(alcoMeasureResultFactory.COLUMNS.RESULT.DATA_TYPE_SINGLETON(part.value))
						"tested" -> tested = alcoMeasureResultFactory.COLUMNS.TESTED.field(alcoMeasureResultFactory.COLUMNS.TESTED.DATA_TYPE_SINGLETON(part.value))
						else -> call.logError("AlcoMeasureService.uploadResult", "Request multipart form item not recognised. [${part.name}]")
					}
					is io.ktor.http.content.PartData.FileItem -> when (part.name) {
						"photo1" -> {
							// FIXME: Store in Google Cloud Storage and set `photo1FileId`:
							// val photo1InputStream = part.streamProvider()
							// photo1FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.DATA_TYPE_SINGLETON(???))
						}
						"photo2" -> {
							// FIXME: Store in Google Cloud Storage and set `photo2FileId`:
							// val photo2InputStream = part.streamProvider()
							// photo2FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.DATA_TYPE_SINGLETON(???))
						}
						"photo3" -> {
							// FIXME: Store in Google Cloud Storage and set `photo3FileId`:
							// val photo3InputStream = part.streamProvider()
							// photo3FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.field(alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.DATA_TYPE_SINGLETON(???))
						}
						else -> call.logError("AlcoMeasureService.uploadResult", "Request multipart file item not recognised. [${part.name}]")
					}
					is io.ktor.http.content.PartData.BinaryItem -> call.logError("AlcoMeasureService.uploadResult", "Request multipart binary item not supported. [${part.name}]")
				}
				part.dispose()
				part = multipart.readPart()
			}
			if (accessControllerId == null) call.logError("AlcoMeasureService.uploadResult", "Request was missing `googleCloudDeviceId`.")
			if (personId == null) call.logError("AlcoMeasureService.uploadResult", "Request was missing `personId`.")
			if (result == null) call.logError("AlcoMeasureService.uploadResult", "Request was missing `result`.")
			if (tested == null) call.logError("AlcoMeasureService.uploadResult", "Request was missing `tested`.")
			if (accessControllerId == null || personId == null || result == null || tested == null) {
				call.attributes.put(com.github.evanbennett.core.Result.KEY, com.github.evanbennett.core.Result(com.github.evanbennett.core.Response.BadRequest("Missing required items."), com.github.evanbennett.module.actors.DatabaseLogging.Message.AuditLog(this, Messages.UPLOAD_RESULT, call), call))
			} else {
				val alcoMeasureResultId = alcoMeasureResultFactory.COLUMNS.ALCO_MEASURE_RESULT_ID.field()
				if (photo1FileId == null) photo1FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_1_FILE_ID.field()
				if (photo2FileId == null) photo2FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_2_FILE_ID.field()
				if (photo3FileId == null) photo3FileId = alcoMeasureResultFactory.COLUMNS.PHOTO_3_FILE_ID.field()
				alcoMeasureResultFactory(alcoMeasureResultId, accessControllerId, personId, result, photo1FileId, photo2FileId, photo3FileId, tested).insert(call)
				call.attributes.put(com.github.evanbennett.core.Result.KEY, com.github.evanbennett.core.Result(com.github.evanbennett.core.Response.Success, com.github.evanbennett.module.actors.DatabaseLogging.Message.AuditLog(this, Messages.UPLOAD_RESULT, call), call))
			}
		}
	}

	@Suppress("unused")
	open class ReverseRoutes(override val controller: AlcoMeasureService) : com.github.evanbennett.core.controllers.ReverseRoutesI<AlcoMeasureService> {

		fun uploadResult(): com.github.evanbennett.core.HttpAddress = com.github.evanbennett.core.HttpAddress(io.ktor.http.HttpMethod.Post, controller.pathPrefix + "uploadResult")
	}
}
