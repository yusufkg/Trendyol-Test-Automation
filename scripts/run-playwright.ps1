param(
    [switch]$Live,
    [switch]$InstallBrowser
)

$ErrorActionPreference = 'Stop'
$modulePath = Join-Path (Split-Path $PSScriptRoot -Parent) 'playwright-ui'

Push-Location $modulePath
try {
    if ($InstallBrowser) {
        & mvn -B -ntp org.codehaus.mojo:exec-maven-plugin:3.6.4:java '-Dexec.classpathScope=test' '-Dexec.mainClass=com.microsoft.playwright.CLI' '-Dexec.args=install --only-shell chromium'
        if ($LASTEXITCODE -ne 0) {
            throw 'Playwright Chromium installation failed.'
        }
    }

    $testClass = if ($Live) { 'HomePageIT' } else { 'SearchSmokeTest' }
    & mvn -B -ntp clean test "-Dtest=$testClass"
    $testExitCode = $LASTEXITCODE
    Write-Host "Surefire results: $(Join-Path $modulePath 'target/surefire-reports')"
    if ($Live) {
        Write-Host "Live failure evidence: $(Join-Path $modulePath 'target/playwright-live')"
    }
    if ($testExitCode -ne 0) {
        throw "Playwright $testClass failed with exit code $testExitCode."
    }
} finally {
    Pop-Location
}
