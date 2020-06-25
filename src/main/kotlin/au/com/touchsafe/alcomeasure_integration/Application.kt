package au.com.touchsafe.alcomeasure_integration

import com.github.evanbennett.module.module
import io.ktor.application.install

@Suppress("unused")
fun io.ktor.application.Application.main() {
	com.github.evanbennett.module.components.Module.bindings()
	au.com.touchsafe.access_control_common.components.AccessControlCommon.bindings()
	au.com.touchsafe.organisations_and_people.components.OrganisationsAndPeople.bindings()
	au.com.touchsafe.alcomeasure_integration.components.AlcomeasureIntegration.bindings()
	module(au.com.touchsafe.alcomeasure_integration.components.AlcomeasureIntegration::menu)
}
