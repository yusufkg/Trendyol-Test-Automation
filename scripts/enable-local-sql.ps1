# Yönetici PowerShell gerekir. Yalnızca loopback TCP erişimi açar.
$ErrorActionPreference = 'Stop'
$instanceRegistry = 'HKLM:\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL16.SQLEXPRESS\MSSQLServer\SuperSocketNetLib\Tcp'
if (-not (Test-Path $instanceRegistry)) { throw 'Beklenen SQLEXPRESS instance bulunamadı.' }
$service = Get-Service -Name 'MSSQL$SQLEXPRESS'
Set-ItemProperty -LiteralPath $instanceRegistry -Name Enabled -Value 1
Set-ItemProperty -LiteralPath $instanceRegistry -Name ListenOnAllIPs -Value 0
foreach ($ipKey in Get-ChildItem -LiteralPath $instanceRegistry) {
    if ($ipKey.PSChildName -eq 'IPAll') { continue }
    $ipConfiguration = Get-ItemProperty -LiteralPath $ipKey.PSPath
    $isLoopback = $ipConfiguration.IpAddress -eq '127.0.0.1'
    Set-ItemProperty -LiteralPath $ipKey.PSPath -Name Enabled -Value ([int]$isLoopback)
    if ($isLoopback) {
        Set-ItemProperty -LiteralPath $ipKey.PSPath -Name TcpDynamicPorts -Value ''
        Set-ItemProperty -LiteralPath $ipKey.PSPath -Name TcpPort -Value '14330'
    }
}
Restart-Service -Name $service.Name
Write-Host 'SQL Express yalnızca 127.0.0.1:14330 üzerinden JDBC bağlantısına hazır.'
