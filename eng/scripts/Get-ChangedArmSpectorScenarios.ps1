<#
.SYNOPSIS
  Lists ARM Spector scenario folders added or modified by a typespec-azure PR.

.DESCRIPTION
  Scans the diff of a given PR in Azure/typespec-azure for files under
  `packages/azure-http-specs/specs/azure/resource-manager/**` and emits the unique
  scenario folders (the directory containing a `main.tsp`). Output is a JSON array
  on stdout so it can be consumed by `fromJson()` in a GitHub Actions step.

  Requires `gh` to be authenticated for `Azure/typespec-azure`.

.PARAMETER PrNumber
  PR number (or full URL) in Azure/typespec-azure.

.PARAMETER TypespecAzureRoot
  Absolute path to the typespec-azure checkout (must contain the changed scenario
  files). Used to walk upward and find the directory that actually contains a
  `main.tsp`.

.PARAMETER RepoSlug
  Defaults to "Azure/typespec-azure".

.EXAMPLE
  pwsh ./eng/scripts/Get-ChangedArmSpectorScenarios.ps1 -PrNumber 1234 -TypespecAzureRoot ../typespec-azure
#>
[CmdletBinding()]
param(
  [Parameter(Mandatory = $true)]
  [string] $PrNumber,

  [Parameter(Mandatory = $true)]
  [string] $TypespecAzureRoot,

  [string] $RepoSlug = "Azure/typespec-azure"
)

$ErrorActionPreference = "Stop"

if ($PrNumber -match "/pull/(\d+)") {
  $PrNumber = $Matches[1]
}

if (-not (Test-Path $TypespecAzureRoot)) {
  throw "TypespecAzureRoot not found at $TypespecAzureRoot"
}

Write-Host "Resolving changed files for $RepoSlug#$PrNumber" -ForegroundColor Cyan

# `gh pr diff --name-only` returns one path per line.
$diffLines = gh pr diff $PrNumber --repo $RepoSlug --name-only
if ($LASTEXITCODE -ne 0) {
  throw "gh pr diff failed for $RepoSlug#$PrNumber"
}

$armSpecPrefix = "packages/azure-http-specs/specs/azure/resource-manager/"

$scenarioDirs = New-Object 'System.Collections.Generic.HashSet[string]'

foreach ($file in $diffLines) {
  if (-not $file) { continue }
  $normalized = ($file -replace "\\", "/").Trim()

  if (-not $normalized.StartsWith($armSpecPrefix)) { continue }
  if (-not $normalized.EndsWith(".tsp")) { continue }

  # Walk upward from the changed file's parent until we find a folder that
  # actually contains a main.tsp on disk. This handles imported nested .tsp
  # files (e.g., scenarios/*.tsp) where the scenario root sits above them.
  $dir = ($normalized -split "/")
  $dir = ($dir[0..($dir.Length - 2)] -join "/")
  while ($dir -and $dir.StartsWith($armSpecPrefix)) {
    $candidate = Join-Path $TypespecAzureRoot ($dir + "/main.tsp")
    if (Test-Path $candidate) {
      [void]$scenarioDirs.Add($dir)
      break
    }
    $parts = $dir -split "/"
    if ($parts.Length -le 1) { break }
    $dir = ($parts[0..($parts.Length - 2)] -join "/")
  }
}

$result = @($scenarioDirs) | Sort-Object
if ($result.Count -le 1) {
  $json = "[" + (($result | ForEach-Object { '"' + $_ + '"' }) -join ",") + "]"
} else {
  $json = $result | ConvertTo-Json -Compress
}
Write-Output $json
