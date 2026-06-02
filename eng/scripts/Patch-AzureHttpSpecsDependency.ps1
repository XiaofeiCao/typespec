<#
.SYNOPSIS
  Patches packages/http-client-java/generator/http-client-generator-test/package.json
  to point @azure-tools/azure-http-specs at a locally-built tarball, aligning the
  TypeSpec override versions to the ones in the local typespec-azure checkout.

.DESCRIPTION
  Used by the generate-java-e2e-for-arm-spector workflow so the Java regen consumes
  the in-PR azure-http-specs sources without publishing. In `-Revert` mode, the
  script restores the original package.json from the .bak file the patch step wrote.

.PARAMETER PackageJson
  Absolute path to the http-client-generator-test package.json.

.PARAMETER TypespecAzureRoot
  Absolute path to the local typespec-azure checkout root (used to align overrides).

.PARAMETER AzureHttpSpecsTarball
  Absolute path to the locally-packed @azure-tools/azure-http-specs tarball (.tgz).

.PARAMETER Revert
  If set, restores package.json from the backup written by a previous patch run.
#>
[CmdletBinding(DefaultParameterSetName = "Patch")]
param(
  [Parameter(Mandatory = $true)]
  [string] $PackageJson,

  [Parameter(Mandatory = $true, ParameterSetName = "Patch")]
  [string] $TypespecAzureRoot,

  [Parameter(Mandatory = $true, ParameterSetName = "Patch")]
  [string] $AzureHttpSpecsTarball,

  [Parameter(ParameterSetName = "Revert")]
  [switch] $Revert
)

$ErrorActionPreference = "Stop"

if (-not (Test-Path $PackageJson)) {
  throw "package.json not found at $PackageJson"
}

$backupPath = "$PackageJson.bak"

if ($Revert) {
  if (-not (Test-Path $backupPath)) {
    Write-Warning "No backup at $backupPath; nothing to revert."
    return
  }
  Copy-Item -Path $backupPath -Destination $PackageJson -Force
  Remove-Item -Path $backupPath -Force
  Write-Host "Reverted $PackageJson from backup." -ForegroundColor Green
  return
}

if (-not (Test-Path $AzureHttpSpecsTarball)) {
  throw "Tarball not found at $AzureHttpSpecsTarball"
}
if (-not (Test-Path $TypespecAzureRoot)) {
  throw "typespec-azure root not found at $TypespecAzureRoot"
}

Copy-Item -Path $PackageJson -Destination $backupPath -Force

$pkg = Get-Content -Raw -Path $PackageJson | ConvertFrom-Json -AsHashtable

$tarballUri = "file:" + ($AzureHttpSpecsTarball -replace "\\", "/")
$pkg["dependencies"]["@azure-tools/azure-http-specs"] = $tarballUri
Write-Host "Set @azure-tools/azure-http-specs -> $tarballUri" -ForegroundColor Cyan

$packagesDir = Join-Path $TypespecAzureRoot "packages"

function Get-LocalPackageVersion {
  param([string] $PackageName)
  $candidates = Get-ChildItem -Path $packagesDir -Directory -ErrorAction SilentlyContinue
  foreach ($c in $candidates) {
    $pj = Join-Path $c.FullName "package.json"
    if (-not (Test-Path $pj)) { continue }
    try {
      $data = Get-Content -Raw -Path $pj | ConvertFrom-Json
    } catch { continue }
    if ($data.name -eq $PackageName) {
      return [string]$data.version
    }
  }
  return $null
}

if ($pkg.ContainsKey("overrides")) {
  foreach ($key in @($pkg["overrides"].Keys)) {
    $localVersion = Get-LocalPackageVersion -PackageName $key
    if ($localVersion) {
      if ($pkg["overrides"][$key] -ne $localVersion) {
        Write-Host ("  override {0}: {1} -> {2}" -f $key, $pkg["overrides"][$key], $localVersion) -ForegroundColor Yellow
        $pkg["overrides"][$key] = $localVersion
      }
    } else {
      Write-Host ("  override {0}: not found in local typespec-azure, leaving as-is" -f $key) -ForegroundColor DarkGray
    }
  }
}

$json = $pkg | ConvertTo-Json -Depth 100
# Normalize indent to 2 spaces (PowerShell 7 default is already 2, this is a no-op there).
$json = ($json -split "`r?`n") | ForEach-Object {
  if ($_ -match '^(\s+)(.*)$' -and ($Matches[1].Length % 4) -eq 0 -and $Matches[1].Length -gt 0) {
    return (' ' * ($Matches[1].Length / 2)) + $Matches[2]
  }
  return $_
}
$json = ($json -join "`n") + "`n"
Set-Content -Path $PackageJson -Value $json -NoNewline -Encoding utf8

Write-Host "Patched $PackageJson" -ForegroundColor Green
