package au.com.touchsafe.alcomeasure_integration.models.generated

open class AlcoMeasureResultRecord(alcoMeasureResultId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, uniqueIdentifier: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength012<*>>, personId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, organisationId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>, result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>, passed: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.Boolean>, override val alcoMeasureResultRecordFactory: AlcoMeasureResultRecordFactory) : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultRecord(alcoMeasureResultId, uniqueIdentifier, personId, organisationId, tested, result, passed, alcoMeasureResultRecordFactory) {

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE BELOW HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE ABOVE HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}

open class AlcoMeasureResultRecordFactory : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultRecordFactory() {

	override operator fun invoke(alcoMeasureResultId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, uniqueIdentifier: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength012<*>>, personId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, organisationId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>, result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>, passed: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.Boolean>): AlcoMeasureResultRecord {
		val model = AlcoMeasureResultRecord(alcoMeasureResultId, uniqueIdentifier, personId, organisationId, tested, result, passed, this@AlcoMeasureResultRecordFactory)
		model.validateConstructorArguments()
		return model
	}

	override val COLUMNS by lazy { Columns() }
	override val DEFAULT_LIST_ARGUMENTS by lazy { DisplayListArguments() }

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE BELOW HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	open inner class Columns : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultRecordFactory.Columns() {

		override val UNIQUE_IDENTIFIER = super.UNIQUE_IDENTIFIER.copy(listWidth = 100)
		override val RESULT = super.RESULT.copy(listWidth = 120)
		override val PERSON_VERSION_UNIQUE_IDENTIFIER = super.PERSON_VERSION_UNIQUE_IDENTIFIER.copy(listWidth = 150, listVisible = false)
		override val PERSON_VERSION_SURNAME = super.PERSON_VERSION_SURNAME.copy(listWidth = 180)
		override val PERSON_VERSION_FIRST_NAME = super.PERSON_VERSION_FIRST_NAME.copy(listWidth = 180)
		override val ORGANISATION_VERSION_NAME = super.ORGANISATION_VERSION_NAME.copy(listWidth = 180)
	}

	open inner class DisplayListArguments : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultRecordFactory.DisplayListArguments() {

		protected open val personVersionGroupFactory: au.com.touchsafe.organisations_and_people.models.generated.PersonVersionGroupFactory by com.github.evanbennett.core.ServiceLocator.lazyGet()
		override val fromToTimestampsConditionColumn by lazy { COLUMNS.TESTED }
		override val personGroupConditionItemName by lazy { personVersionFactory.ITEM_NAME }
		override val personGroupConditionColumn by lazy { personVersionGroupFactory.COLUMNS.PERSON_GROUP_ID }
		override val booleanConditionColumn by lazy { COLUMNS.PASSED }
	}

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE ABOVE HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}
