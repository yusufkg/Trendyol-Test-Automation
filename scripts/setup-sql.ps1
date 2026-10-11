param([string]$Server = 'DESKTOP-SDLI8OI\SQLEXPRESS')
$ErrorActionPreference = 'Stop'
Set-Location (Split-Path $PSScriptRoot -Parent)
& sqlcmd -S $Server -E -l 10 -b -Q "IF DB_ID(N'QaAutomation') IS NULL CREATE DATABASE [QaAutomation]"
if ($LASTEXITCODE -ne 0) { throw 'SQL Server bağlantısı veya veritabanı oluşturma başarısız.' }
& sqlcmd -S $Server -E -d QaAutomation -l 10 -b -i database/schema.sql
if ($LASTEXITCODE -ne 0) { throw 'SQL şeması kurulamadı.' }
& mvn -B -ntp org.apache.maven.plugins:maven-dependency-plugin:3.8.1:get '-Dartifact=com.microsoft.sqlserver:mssql-jdbc_auth:12.8.1.x64:dll' '-Dtransitive=false'
if ($LASTEXITCODE -ne 0) { throw 'Windows kimlik doğrulama DLL indirilemedi.' }
$repositoryPath = (& mvn -q -ntp help:evaluate '-Dexpression=settings.localRepository' '-DforceStdout' | Out-String).Trim()
if ($LASTEXITCODE -ne 0) { throw 'Maven repository yolu okunamadı.' }
$nativeFile = Join-Path $repositoryPath 'com/microsoft/sqlserver/mssql-jdbc_auth/12.8.1.x64/mssql-jdbc_auth-12.8.1.x64.dll'
New-Item -ItemType Directory -Path target/native -Force | Out-Null
Copy-Item -LiteralPath $nativeFile -Destination target/native -Force
Write-Host 'QaAutomation hazır. Windows kimlik doğrulaması için README adımlarını izleyin.'
