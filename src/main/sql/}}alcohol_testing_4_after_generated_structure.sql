BEGIN READ WRITE;

CREATE VIEW AlcoMeasureResultReport AS --VIEW_NAME_SINGULAR:AlcoMeasureResultRecord
	   SELECT alcoMeasureResultId, uniqueIdentifier, personId, organisationId, tested, result, (result = 0) AS passed /* passed BOOLEAN */
	     FROM AlcoMeasureResults
	     JOIN PersonVersions_CurrentCommitted AS PersonVersions USING (personId);

COMMIT;
