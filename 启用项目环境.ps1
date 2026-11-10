# 在当前 PowerShell 会话中启用项目本地 JDK 8 和 Maven。
$jdk = Get-ChildItem (Join-Path $PSScriptRoot '.tools') -Directory -Filter 'jdk8*' | Select-Object -First 1
$maven = Join-Path $PSScriptRoot '.tools\apache-maven-3.9.11'
if (-not $jdk -or -not (Test-Path (Join-Path $jdk.FullName 'bin\java.exe'))) { throw '未找到项目本地 JDK 8。' }
if (-not (Test-Path (Join-Path $maven 'bin\mvn.cmd'))) { throw '未找到项目本地 Maven。' }
$env:JAVA_HOME = $jdk.FullName
$env:PATH = "$($env:JAVA_HOME)\bin;$maven\bin;$env:PATH"
Write-Host "项目环境已启用。JAVA_HOME=$env:JAVA_HOME"
