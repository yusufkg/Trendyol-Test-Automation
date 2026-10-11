-- SSMS ile hedef veritabanında çalıştırın. Mevcut kayıtlar silinmez.
IF OBJECT_ID(N'dbo.TestResults', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.TestResults (
        Id BIGINT IDENTITY(1,1) PRIMARY KEY,
        Scenario NVARCHAR(1000) NOT NULL,
        Status NVARCHAR(30) NOT NULL,
        StartedAt DATETIME2(3) NOT NULL,
        FinishedAt DATETIME2(3) NOT NULL,
        DurationMs BIGINT NOT NULL,
        ErrorMessage NVARCHAR(MAX) NULL,
        ScreenshotPath NVARCHAR(1000) NULL,
        Browser NVARCHAR(100) NULL
    );
END;
-- Son çalıştırmalar ve başarı/başarısızlık özeti:
SELECT TOP (100) * FROM dbo.TestResults ORDER BY Id DESC;
SELECT Status, COUNT(*) AS TestCount, AVG(DurationMs) AS AverageDurationMs
FROM dbo.TestResults GROUP BY Status;
