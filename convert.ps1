# PowerShell script to convert HTML files

$sidebarTemplate = @'
            <!-- Sidebar -->
            <div class="col-md-2 sidebar">
                <div class="text-center mb-4">
                    <i class="fas fa-water fa-2x mb-2"></i>
                    <h5 class="mb-0">Forage MG</h5>
                    <small>Gestion des Services</small>
                </div>
                
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_HOME}" th:href="@{/}">
                            <i class="fas fa-home"></i> Accueil
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_CLIENTS}" th:href="@{/clients}">
                            <i class="fas fa-users"></i> Clients
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_DEMANDES}" th:href="@{/demandes}">
                            <i class="fas fa-file-alt"></i> Demandes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_DEVIS}" th:href="@{/devis}">
                            <i class="fas fa-file-invoice-dollar"></i> Devis
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_PAIEMENTS}" th:href="@{/paiements}">
                            <i class="fas fa-money-bill-wave"></i> Paiements
                        </a>
                    </li>
                    <li class="nav-item mt-3">
                        <span class="nav-link disabled" style="color: rgba(255,255,255,0.5);">
                            <i class="fas fa-map-marker-alt"></i> Géographie
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_REGIONS}" th:href="@{/regions}">
                            <i class="fas fa-globe-africa"></i> Régions
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_DISTRICTS}" th:href="@{/districts}">
                            <i class="fas fa-map"></i> Districts
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_COMMUNES}" th:href="@{/communes}">
                            <i class="fas fa-landmark"></i> Communes
                        </a>
                    </li>
                    <li class="nav-item mt-3">
                        <span class="nav-link disabled" style="color: rgba(255,255,255,0.5);">
                            <i class="fas fa-cog"></i> Configuration
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_TYPES}" th:href="@{/types-devis}">
                            <i class="fas fa-list-alt"></i> Types de Devis
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_STATUTS}" th:href="@{/statuts}">
                            <i class="fas fa-check-circle"></i> Statuts
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{ACTIVE_DETAILS}" th:href="@{/details-devis}">
                            <i class="fas fa-stream"></i> Détails Devis
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-10 main-content">
'@

function Get-ActiveSidebar($section) {
    $sidebar = $sidebarTemplate
    $activeMap = @{
        'clients' = 'CLIENTS'
        'demandes' = 'DEMANDES'
        'devis' = 'DEVIS'
        'paiements' = 'PAIEMENTS'
        'regions' = 'REGIONS'
        'districts' = 'DISTRICTS'
        'communes' = 'COMMUNES'
        'types-devis' = 'TYPES'
        'statuts' = 'STATUTS'
        'details-devis' = 'DETAILS'
    }
    
    $activeKey = $activeMap[$section]
    
    foreach ($key in @('HOME', 'CLIENTS', 'DEMANDES', 'DEVIS', 'PAIEMENTS', 'REGIONS', 'DISTRICTS', 'COMMUNES', 'TYPES', 'STATUTS', 'DETAILS')) {
        if ($key -eq $activeKey) {
            $sidebar = $sidebar.Replace("{ACTIVE_$key}", " active")
        } else {
            $sidebar = $sidebar.Replace("{ACTIVE_$key}", "")
        }
    }
    
    return $sidebar
}

function Convert-HtmlFile($filePath, $section) {
    $content = Get-Content -Path $filePath -Raw -Encoding UTF8
    
    # Extract main content
    if ($content -match '<main>([\s\S]*?)</main>') {
        $mainContent = $matches[1]
    } else {
        Write-Host "Skipping $filePath - no main tag found"
        return
    }
    
    # Extract title
    $title = "Forage Madagascar"
    if ($content -match "title='([^']+)'") {
        $title = $matches[1]
    } elseif ($content -match 'title="([^"]+)"') {
        $title = $matches[1]
    } elseif ($content -match 'title=\$\{([^}]+)\}') {
        $title = $matches[1]
    }
    
    # Get sidebar with active class
    $sidebar = Get-ActiveSidebar $section
    
    # Build new HTML
    $newHtml = @"
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>$title</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" th:href="@{/css/custom.css}">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
$sidebar
$mainContent
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
"@
    
    Set-Content -Path $filePath -Value $newHtml -Encoding UTF8
    Write-Host "Converted: $filePath"
}

# Process all folders
$folders = @{
    'communes' = 'communes'
    'demandes' = 'demandes'
    'devis' = 'devis'
    'details-devis' = 'details-devis'
    'districts' = 'districts'
    'paiements' = 'paiements'
    'regions' = 'regions'
    'statuts' = 'statuts'
    'types-devis' = 'types-devis'
}

$baseDir = "src\main\resources\templates"

foreach ($folder in $folders.Keys) {
    $folderPath = Join-Path $baseDir $folder
    if (Test-Path $folderPath) {
        Get-ChildItem -Path $folderPath -Filter "*.html" | ForEach-Object {
            Convert-HtmlFile $_.FullName $folder
        }
    }
}

Write-Host "`nConversion complete!"
