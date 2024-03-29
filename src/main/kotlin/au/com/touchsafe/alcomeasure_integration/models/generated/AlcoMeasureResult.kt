package au.com.touchsafe.alcomeasure_integration.models.generated

open class AlcoMeasureResult(alcoMeasureResultId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, accessControllerId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, personId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>, photo1FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>, photo2FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>, photo3FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>, tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>, override val alcoMeasureResultFactory: AlcoMeasureResultFactory) : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResult(alcoMeasureResultId, accessControllerId, personId, result, photo1FileId, photo2FileId, photo3FileId, tested, alcoMeasureResultFactory) {

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE BELOW HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE ABOVE HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}

open class AlcoMeasureResultFactory : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory() {

	override operator fun invoke(alcoMeasureResultId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, accessControllerId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, personId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Integer>, result: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.numeric.Double>, photo1FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>, photo2FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>, photo3FileId: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.File>, tested: com.github.evanbennett.core.ui.Field<com.github.evanbennett.core.types.datetime.Timestamp<*>>): AlcoMeasureResult {
		val model = AlcoMeasureResult(alcoMeasureResultId, accessControllerId, personId, result, photo1FileId, photo2FileId, photo3FileId, tested, this@AlcoMeasureResultFactory)
		model.validateConstructorArguments()
		return model
	}

	override val COLUMNS by lazy { Columns() }
	override val VIEWS by lazy { Views() }
	override val UNIQUE_CONSTRAINTS by lazy { UniqueConstraints() }
	override val FOREIGN_KEY_CONSTRAINTS by lazy { ForeignKeyConstraints() }
	override val CHECK_CONSTRAINTS by lazy { CheckConstraints() }
	override val RAISED_EXCEPTIONS by lazy { RaisedExceptions() }
	override val DEFAULT_LIST_ARGUMENTS by lazy { DisplayListArguments() }

	override fun referenceListArguments(listIdentifier: String, updateUri: com.github.evanbennett.core.HttpAddress?): ReferenceListArguments = ReferenceListArguments(listIdentifier, updateUri)

	open inner class Views : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.Views()
	open inner class UniqueConstraints : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.UniqueConstraints()
	open inner class ForeignKeyConstraints : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.ForeignKeyConstraints()
	open inner class CheckConstraints : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.CheckConstraints()
	open inner class RaisedExceptions : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.RaisedExceptions()
	open inner class DisplayListArguments : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.DisplayListArguments()
	open inner class ReferenceListArguments(listIdentifier: String, updateUri: com.github.evanbennett.core.HttpAddress?) : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.ReferenceListArguments(listIdentifier, updateUri)

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE BELOW HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	open inner class Columns : au.com.touchsafe.alcomeasure_integration.models.generated.managed.AlcoMeasureResultFactory.Columns() {

		override val PERSON_VERSION_SURNAME = super.PERSON_VERSION_SURNAME.copy(listWidth = 180)
		override val PERSON_VERSION_FIRST_NAME = super.PERSON_VERSION_FIRST_NAME.copy(listWidth = 180)
		override val RESULT = super.RESULT.copy(listWidth = 120)
	}

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * DEVELOPER'S ADD CODE ABOVE HERE * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}
