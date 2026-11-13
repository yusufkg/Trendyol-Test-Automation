param([switch]$Smoke)

$ErrorActionPreference = 'Stop'
$modulePath = Join-Path (Split-Path $PSScriptRoot -Parent) 'api-tests'
$suite = if ($Smoke) { 'src/test/resources/testng-smoke.xml' } else { 'src/test/resources/testng.xml' }

Push-Location $modulePath
try {
    & mvn -B -ntp clean test "-Dqa.suite=$suite"
    $testExitCode = $LASTEXITCODE
    Write-Host "TestNG report: $(Join-Path $modulePath 'target/surefire-reports/emailable-report.html')"
    if ($testExitCode -ne 0) {
        throw "API suite $suite failed with exit code $testExitCode."
    }
} finally {
    Pop-Location
}
