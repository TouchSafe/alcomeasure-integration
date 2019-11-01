package au.com.touchsafe.alcohol_testing.models.generated

open class AlcoholTestResult(alcoMeasureResultId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, uniqueIdentifier: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength012<*>>, firstName: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength064<*>>, surname: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength064<*>>, organisationId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>, result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>, passed: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.Boolean>, override val alcoholTestResultFactory: AlcoholTestResultFactory) : au.com.touchsafe.alcohol_testing.models.generated.managed.AlcoholTestResult(alcoMeasureResultId, uniqueIdentifier, firstName, surname, organisationId, tested, result, passed, alcoholTestResultFactory) {

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE BELOW HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE ABOVE HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}

open class AlcoholTestResultFactory : au.com.touchsafe.alcohol_testing.models.generated.managed.AlcoholTestResultFactory() {

	override operator fun invoke(alcoMeasureResultId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, uniqueIdentifier: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength012<*>>, firstName: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength064<*>>, surname: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.string.StringMaxLength064<*>>, organisationId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>, result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>, passed: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.Boolean>): AlcoholTestResult {
		val model = AlcoholTestResult(alcoMeasureResultId, uniqueIdentifier, firstName, surname, organisationId, tested, result, passed, this@AlcoholTestResultFactory)
		model.validateConstructorArguments()
		return model
	}

	override val COLUMNS by lazy { Columns() }
	override val DEFAULT_LIST_ARGUMENTS by lazy { DisplayListArguments() }

	override fun referenceListArguments(listIdentifier: String, updateUri: com.github.evanbennett.core.HttpAddress?): ReferenceListArguments = ReferenceListArguments(listIdentifier, updateUri)

	open inner class Columns : au.com.touchsafe.alcohol_testing.models.generated.managed.AlcoholTestResultFactory.Columns()
	open inner class DisplayListArguments : au.com.touchsafe.alcohol_testing.models.generated.managed.AlcoholTestResultFactory.DisplayListArguments()
	open inner class ReferenceListArguments(listIdentifier: String, updateUri: com.github.evanbennett.core.HttpAddress?) : au.com.touchsafe.alcohol_testing.models.generated.managed.AlcoholTestResultFactory.ReferenceListArguments(listIdentifier, updateUri)

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE BELOW HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE ABOVE HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}
