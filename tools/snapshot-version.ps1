param(
    [Parameter(Mandatory = $true)]
    [string]$Version,
    [Parameter(Mandatory = $true)]
    [int]$VersionCode,
    [string]$Root = (Resolve-Path (Join-Path $PSScriptRoot '..')).Path,
    [string]$OutputRoot = (Join-Path (Resolve-Path (Join-Path $PSScriptRoot '..\..')).Path '_snapshots')
)

$ErrorActionPreference = 'Stop'
$stamp = "$Version-$VersionCode-" + (Get-Date -Format 'yyyyMMdd-HHmmss')
$destination = Join-Path $OutputRoot $stamp
if (Test-Path -LiteralPath $destination) {
    throw "Snapshot already exists: $destination"
}

New-Item -ItemType Directory -Force -Path $destination | Out-Null
$items = @(
    'app',
    'adapter-carlife',
    'core-airplay',
    'core-iap2',
    'build.gradle.kts',
    'settings.gradle.kts',
    'gradle.properties',
    'carlink-release.jks'
)

foreach ($item in $items) {
    $source = Join-Path $Root $item
    if (-not (Test-Path -LiteralPath $source)) { continue }
    $target = Join-Path $destination $item
    if ((Get-Item -LiteralPath $source).PSIsContainer) {
        Copy-Item -LiteralPath $source -Destination $target -Recurse -Force `
            -Exclude 'build', '.gradle', '.kotlin'
    } else {
        Copy-Item -LiteralPath $source -Destination $target -Force
    }
}

[ordered]@{
    version = $Version
    versionCode = $VersionCode
    createdAt = (Get-Date).ToString('o')
    source = $Root
    note = 'Generated source snapshot; generated build/.gradle/.kotlin output is excluded.'
} | ConvertTo-Json | Set-Content -LiteralPath (Join-Path $destination 'snapshot.json') -Encoding UTF8

Get-ChildItem -LiteralPath $destination -Recurse -File |
    Get-FileHash -Algorithm SHA256 |
    Select-Object Path, Hash |
    ConvertTo-Json | Set-Content -LiteralPath (Join-Path $destination 'sha256.json') -Encoding UTF8

$archive = "$destination.zip"
Compress-Archive -Path (Join-Path $destination '*') -DestinationPath $archive -CompressionLevel Optimal
Write-Output $archive
