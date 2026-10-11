-- SSMS'te QaAutomation veritabanını seçip çalıştırın.
-- Bu sorgular yalnızca okuma yapar; geçmiş test sonuçlarını silmez.

-- Son 50 test: gerçek istisna metni ve kanıt dosyası yolu dahil.
SELECT TOP (50)
    Id, Scenario, Status, StartedAt AS StartedAtUtc, FinishedAt AS FinishedAtUtc,
    DurationMs, Browser, ErrorMessage, ScreenshotPath
FROM dbo.TestResults
ORDER BY Id DESC;

-- SQL'de saklanan tüm çalıştırmaların özeti.
SELECT Status, COUNT_BIG(*) AS TestCount, AVG(DurationMs) AS AverageDurationMs
FROM dbo.TestResults
GROUP BY Status
ORDER BY Status;

-- En son başarısız kayıtların hata ayrıntıları.
SELECT TOP (20) Id, Scenario, FinishedAt, ErrorMessage, ScreenshotPath
FROM dbo.TestResults
WHERE Status IN ('FAILED', 'FAIL', 'ERROR', 'BLOCKED')
ORDER BY Id DESC;
