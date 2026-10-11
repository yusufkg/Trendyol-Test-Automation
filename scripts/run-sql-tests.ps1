param([string]$Server = 'localhost:14330', [switch]$Trendyol, [switch]$Hepsiburada, [switch]$TrustServerCertificate, [switch]$OpenReport)
$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
$trustCertificate = $TrustServerCertificate.IsPresent.ToString().ToLowerInvariant()
$env:QA_DB_URL = "jdbc:sqlserver://$Server;databaseName=QaAutomation;integratedSecurity=true;encrypt=true;trustServerCertificate=$trustCertificate;loginTimeout=10"
Remove-Item Env:QA_DB_USER -ErrorAction SilentlyContinue
Remove-Item Env:QA_DB_PASSWORD -ErrorAction SilentlyContinue
$testSelection = '*Test'
$selectedTags = @()
if ($Trendyol) { $selectedTags += '@trendyol' }
if ($Hepsiburada) { $selectedTags += '@hepsiburada' }
$tagArguments = @()
if ($selectedTags.Count -gt 0) {
    $testSelection += ',EcommerceIT'
    $tagArguments = @('-Dcucumber.filter.tags=' + ($selectedTags -join ' or '))
}
$nativeDirectory = Join-Path (Get-Location) 'target/native'
if (-not (Test-Path (Join-Path $nativeDirectory 'mssql-jdbc_auth-12.8.1.x64.dll'))) {
    throw 'Windows auth DLL eksik. Önce scripts/setup-sql.ps1 çalıştırın.'
}
& mvn -B -ntp -Psql test @tagArguments "-Dtest=$testSelection" "-DargLine=-Djava.library.path=$nativeDirectory"
$testExitCode = $LASTEXITCODE
if ($OpenReport -and (Test-Path -LiteralPath 'target/qa-dashboard.html')) {
    try { Invoke-Item -LiteralPath 'target/qa-dashboard.html' }
    catch { Write-Warning 'HTML dosyası oluştu fakat otomatik açılamadı: target/qa-dashboard.html' }
}
if ($testExitCode -ne 0) { throw 'Test veya zorunlu SQL log kaydı başarısız. target raporlarını inceleyin.' }
