$ErrorActionPreference = 'Continue'

function Get-JavaHomeFromProperties {
    $raw = cmd /c "java -XshowSettings:properties -version 2>&1"
    $text = [string]::Join("`n", @($raw))
    if ($text -match 'java\.home = ([^\r\n]+)') {
        return $matches[1].Trim().TrimEnd([char]0x0D, [char]0x0A, ' ', "`t")
    }
    return $null
}

function Get-JavaHomeFromPath {
    $cmd = Get-Command java -ErrorAction SilentlyContinue
    if (-not $cmd) { return $null }
    try {
        $javaExe = [System.IO.Path]::GetFullPath($cmd.Source)
    } catch {
        return $null
    }
    $binDir = Split-Path -Parent $javaExe
    $candidate = Split-Path -Parent $binDir
    $test = Join-Path $candidate 'bin\java.exe'
    if (Test-Path -LiteralPath $test) {
        return $candidate
    }
    return $null
}

$javaHome = Get-JavaHomeFromProperties
if ($javaHome -and (Test-Path -LiteralPath (Join-Path $javaHome 'bin\java.exe'))) {
    Write-Output $javaHome
    exit 0
}

$javaHome2 = Get-JavaHomeFromPath
if ($javaHome2) {
    Write-Output $javaHome2
    exit 0
}

if ($javaHome) {
    Write-Output $javaHome
}
