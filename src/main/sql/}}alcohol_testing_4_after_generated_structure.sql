BEGIN READ WRITE;

CREATE VIEW AlcoholTestResults AS --VIEW_NAME_SINGULAR:AlcoholTestResult
	SELECT alcoMeasureResultId, uniqueIdentifier, firstName, surname, organisationId, tested, result, (result = 0) AS passed /* passed BOOLEAN */
	FROM AlcoMeasureResults
	     JOIN PersonVersions_CurrentCommitted AS PersonVersions USING (personId);
-- TODO: Add To & From Dates conditions
-- TODO: Add `passed` condition
-- TODO: Add Person Category conditions (these will be added as column/s to CSV export)
-- TODO: Implement a view mode that displays the photos!!!

COMMIT;
