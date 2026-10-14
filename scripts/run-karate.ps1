param([switch]$Headless, [switch]$OpenReport)
$ErrorActionPreference = 'Stop'
Push-Location (Join-Path (Split-Path $PSScriptRoot -Parent) 'karate-ui')
try {
    $headlessValue = if ($Headless) { 'true' } else { 'false' }
    & mvn -B -ntp test "-Dheadless=$headlessValue"
    $code = $LASTEXITCODE
    if ($OpenReport -and (Test-Path target/karate-reports/karate-summary.html)) {
        Start-Process (Resolve-Path target/karate-reports/karate-summary.html).Path
    }
    if ($code -ne 0) { throw 'Karate browser tests failed.' }
} finally { Pop-Location }