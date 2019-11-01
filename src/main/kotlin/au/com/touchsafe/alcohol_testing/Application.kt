package au.com.touchsafe.alcohol_testing

import com.github.evanbennett.module.module
import io.ktor.application.install

@Suppress("unused")
fun io.ktor.application.Application.main() {
	com.github.evanbennett.module.components.Module.bindings()
	au.com.touchsafe.organisations_and_people.components.OrganisationsAndPeople.bindings()
	au.com.touchsafe.alcohol_testing.components.AlcoholTesting.bindings()
	install(io.ktor.websocket.WebSockets) {
		pingPeriod = java.time.Duration.ofSeconds(60)
	}
	module(au.com.touchsafe.alcohol_testing.components.AlcoholTesting::menu)
}
