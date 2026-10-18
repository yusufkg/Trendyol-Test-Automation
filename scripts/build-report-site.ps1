param(
    [string]$Output = 'target/report-site',
    [string]$RunUrl = $env:GITHUB_SERVER_URL + '/' + $env:GITHUB_REPOSITORY + '/actions/runs/' + $env:GITHUB_RUN_ID,
    [string]$Commit = $env:GITHUB_SHA
)
$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
$destination = [IO.Path]::GetFullPath((Join-Path $root $Output))
if (!$destination.StartsWith([IO.Path]::GetFullPath($root) + [IO.Path]::DirectorySeparatorChar, [StringComparison]::OrdinalIgnoreCase)) {
    throw 'Output must remain inside the project.'
}
if (Test-Path -LiteralPath $destination) { throw 'Output already exists; choose a fresh -Output directory.' }
$karate = Join-Path $root 'karate-ui/target/karate-reports'
$mapping = Join-Path $root 'target/cucumber-step-mapping.html'
foreach ($required in @($mapping, (Join-Path $karate 'karate-summary.html'), (Join-Path $karate 'setup.browser-smoke.html'))) {
    if (!(Test-Path -LiteralPath $required)) { throw "Missing report: $required" }
}
$summary = [IO.File]::ReadAllText((Join-Path $karate 'karate-summary.html'))
if ($summary -notmatch 'setup.browser-smoke.html' -or $summary -match 'navigation.navigation.html') {
    throw 'Only a local-fixture Karate run may be published by this script.'
}
$suites = Get-ChildItem (Join-Path $root 'target/surefire-reports') -Filter 'TEST-*.xml'
if (!$suites) { throw 'Surefire results are missing.' }
$rows = foreach ($file in $suites) {
    [xml]$xml = Get-Content -LiteralPath $file.FullName -Raw
    $suite = $xml.testsuite
    if ([int]$suite.failures -gt 0 -or [int]$suite.errors -gt 0) { throw 'Do not publish a success site from failed test results.' }
    $scope = if ($suite.name -like '*EcommerceIT') { 'Step mapping only (dry-run)' } else { 'Unit tests' }
    '<tr><td>' + [Net.WebUtility]::HtmlEncode([string]$suite.name) + '</td><td>' + $scope + '</td><td>' + $suite.tests + '</td></tr>'
}
New-Item -ItemType Directory -Force -Path (Join-Path $destination 'karate') | Out-Null
Copy-Item -LiteralPath $mapping -Destination (Join-Path $destination 'cucumber-step-mapping.html')
foreach ($pattern in @('karate-summary.html','setup.browser-smoke.html','karate-tags.html','karate-timeline.html','*.svg','favicon.ico','setup.browser-smoke*.png')) {
    Get-ChildItem -Path (Join-Path $karate $pattern) -File | ForEach-Object {
        Copy-Item -LiteralPath $_.FullName -Destination (Join-Path $destination 'karate')
    }
}
Copy-Item -LiteralPath (Join-Path $karate 'res') -Destination (Join-Path $destination 'karate/res') -Recurse
$safeUrl = [Net.WebUtility]::HtmlEncode($RunUrl)
$safeCommit = [Net.WebUtility]::HtmlEncode($Commit)
$date = [DateTime]::UtcNow.ToString('yyyy-MM-dd HH:mm:ss') + ' UTC'
$html = @"
<!doctype html><html lang="en"><head><meta charset="utf-8"><meta name="viewport" content="width=device-width,initial-scale=1"><title>Automation test reports</title>
<style>body{font:17px system-ui;max-width:1000px;margin:3rem auto;padding:0 1rem;color:#17223b}table{border-collapse:collapse;width:100%}td,th{padding:12px;border:1px solid #ccd4df;text-align:left}a{color:#174fa3}code{overflow-wrap:anywhere}</style></head>
<body><h1>Automation test reports</h1><p>Generated $date</p><p>Commit: <code>$safeCommit</code> · <a href="$safeUrl">Source CI run</a></p>
<p>These reports cover unit tests, Cucumber step mapping and a real headless Chrome test against a local HTML fixture. They do not prove live Trendyol behavior.</p>
<table><thead><tr><th>Suite</th><th>Scope</th><th>Tests</th></tr></thead><tbody>$($rows -join '')</tbody></table>
<ul><li><a href="karate/karate-summary.html">Karate browser report and screenshot</a></li><li><a href="cucumber-step-mapping.html">Cucumber step mapping (dry-run)</a></li></ul>
<p>This is automated test-report publication, not production deployment of Trendyol. Failed and optional live-run reports remain downloadable workflow artifacts.</p></body></html>
"@
[IO.File]::WriteAllText((Join-Path $destination 'index.html'), $html, (New-Object System.Text.UTF8Encoding($false)))
[IO.File]::WriteAllText((Join-Path $destination '.nojekyll'), '')
Write-Host "Report site prepared at $destination"
