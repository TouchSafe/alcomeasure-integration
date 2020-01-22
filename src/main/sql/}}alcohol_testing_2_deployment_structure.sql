BEGIN READ WRITE;

CREATE TABLE AlcoMeasureResults ( --TABLE_NAME_SINGULAR:AlcoMeasureResult
	alcoMeasureResultId SERIAL CONSTRAINT AlcoMeasureResultsPrimaryKey PRIMARY KEY,
	accessControlBoardId INTEGER NOT NULL CONSTRAINT AlcoMeasureResultsRefsAccessControlBoard REFERENCES AccessControlBoards, -- TODO: Update to AccessPoint???
	personId INTEGER NOT NULL CONSTRAINT AlcoMeasureResultsRefsPerson REFERENCES People,
	result DOUBLE PRECISION NOT NULL,
	photo1FileId FILE CONSTRAINT AlcoMeasureResultsRefsPhoto1 REFERENCES Files, --UNASSIGNABLE
	photo2FileId FILE CONSTRAINT AlcoMeasureResultsRefsPhoto2 REFERENCES Files, --UNASSIGNABLE
	photo3FileId FILE CONSTRAINT AlcoMeasureResultsRefsPhoto3 REFERENCES Files, --UNASSIGNABLE
	tested TIMESTAMP NOT NULL DEFAULT Now() --UNASSIGNABLE
);

COMMIT;
