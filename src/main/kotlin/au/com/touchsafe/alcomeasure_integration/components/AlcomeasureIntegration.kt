package au.com.touchsafe.alcomeasure_integration.components

object AlcomeasureIntegration : au.com.touchsafe.alcomeasure_integration.components.managed.AlcomeasureIntegration() {

	override fun bindings() {
		super.bindings()
		com.github.evanbennett.core.ServiceLocator.bind { au.com.touchsafe.alcomeasure_integration.controllers.AlcoMeasureService() }.isController()
	}

	override fun menu(): com.github.evanbennett.core.ui.Menu {
		val accessControllersController: au.com.touchsafe.access_control_common.controllers.generated.AccessControllers by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val alcoMeasureResultReportController: au.com.touchsafe.alcomeasure_integration.controllers.generated.AlcoMeasureResultReport by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val alcoMeasureResultsController: au.com.touchsafe.alcomeasure_integration.controllers.generated.AlcoMeasureResults by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val applicationController: com.github.evanbennett.module.controllers.Application by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val auditLogController: com.github.evanbennett.module.controllers.generated.AuditLog by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val countriesController: au.com.touchsafe.organisations_and_people.controllers.generated.Countries by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val deploymentsController: com.github.evanbennett.module.controllers.generated.Deployments by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val groupCategoriesController: au.com.touchsafe.organisations_and_people.controllers.generated.GroupCategories by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val myPreferencesController: com.github.evanbennett.module.controllers.MyPreferences by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val organisationsController: au.com.touchsafe.organisations_and_people.controllers.generated.Organisations by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val peopleController: au.com.touchsafe.organisations_and_people.controllers.generated.People by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val peopleReportController: au.com.touchsafe.organisations_and_people.controllers.generated.PeopleReport by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val permissionsController: com.github.evanbennett.module.controllers.generated.Permissions by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val personGroupsController: au.com.touchsafe.organisations_and_people.controllers.generated.PersonGroups by com.github.evanbennett.core.ServiceLocator.lazyGet()
		val usersController: com.github.evanbennett.module.controllers.generated.Users by com.github.evanbennett.core.ServiceLocator.lazyGet()
		return com.github.evanbennett.core.ui.Menu(listOf(
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.alcomeasure_integration.models.generated.AlcoMeasureResults.ALCO_MEASURE_RESULTS, alcoMeasureResultsController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.alcomeasure_integration.PERMISSIONS.ALCO_MEASURE_RESULT.LIST)),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.access_control_common.models.generated.AccessControllers.ACCESS_CONTROLLERS, accessControllersController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.access_control_common.PERMISSIONS.ACCESS_CONTROLLER.LIST)),
				com.github.evanbennett.core.ui.Menu.BranchMenuItem(
						au.com.touchsafe.organisations_and_people.models.generated.People.PEOPLE,
						listOf(
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.organisations_and_people.models.generated.People.PEOPLE, peopleController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.organisations_and_people.PERMISSIONS.PERSON.LIST)),
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.organisations_and_people.models.generated.PersonGroups.PERSON_GROUPS, personGroupsController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.organisations_and_people.PERMISSIONS.PERSON_GROUP.LIST)),
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.organisations_and_people.models.generated.GroupCategories.GROUP_CATEGORIES, groupCategoriesController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.organisations_and_people.PERMISSIONS.GROUP_CATEGORY.LIST)),
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(com.github.evanbennett.module.models.generated.Users.USERS, usersController.reverseRoutes.list(), null, null, listOf(com.github.evanbennett.module.PERMISSIONS.USER.LIST)),
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.organisations_and_people.models.generated.Organisations.ORGANISATIONS, organisationsController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.organisations_and_people.PERMISSIONS.ORGANISATION.LIST))
						)
				),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.organisations_and_people.models.generated.Countries.COUNTRIES, countriesController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.organisations_and_people.PERMISSIONS.COUNTRY.LIST)),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(com.github.evanbennett.module.models.generated.Permissions.PERMISSIONS, permissionsController.reverseRoutes.permissionsPanel(), null, null, listOf(com.github.evanbennett.module.PERMISSIONS.PROFILE.LIST, com.github.evanbennett.module.PERMISSIONS.SYSTEM_PROFILE.LIST, com.github.evanbennett.module.PERMISSIONS.PERMISSION.LIST)),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(com.github.evanbennett.module.models.generated.AuditLog.AUDIT_LOG, auditLogController.reverseRoutes.list(), null, null, listOf(com.github.evanbennett.module.PERMISSIONS.AUDIT_EVENT.LIST)),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(com.github.evanbennett.module.models.generated.Deployments.DEPLOYMENT, null, deploymentsController.reverseRoutes.view(), null, listOf(com.github.evanbennett.module.PERMISSIONS.DEPLOYMENT.VIEW)),
				com.github.evanbennett.core.ui.Menu.BranchMenuItem(
						au.com.touchsafe.organisations_and_people.models.General.REPORTS,
						listOf(
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.organisations_and_people.models.generated.PeopleReport.PEOPLE_REPORT, peopleReportController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.organisations_and_people.PERMISSIONS.PEOPLE_REPORT.LIST)),
								com.github.evanbennett.core.ui.Menu.LeafMenuItem(au.com.touchsafe.alcomeasure_integration.models.generated.AlcoMeasureResultReport.ALCO_MEASURE_RESULT_REPORT, alcoMeasureResultReportController.reverseRoutes.list(), null, null, listOf(au.com.touchsafe.alcomeasure_integration.PERMISSIONS.ALCO_MEASURE_RESULT_REPORT.LIST))
						)
				),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(com.github.evanbennett.module.models.generated.UserPreferences.MY_PREFERENCES, null, myPreferencesController.reverseRoutes.edit(), null, listOf(com.github.evanbennett.module.PERMISSIONS.MY_PREFERENCES.SPECIAL)),
				com.github.evanbennett.core.ui.Menu.LeafMenuItem(com.github.evanbennett.module.models.generated.UserLogins.LOGOUT, applicationController.reverseRoutes.logout(), null, null, listOf(com.github.evanbennett.module.PERMISSIONS.LOGOUT.SPECIAL))
		))
	}
}
