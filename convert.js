const fs = require('fs');
const path = require('path');

const sidebar = (activeSection) => `            <!-- Sidebar -->
            <div class="col-md-2 sidebar">
                <div class="text-center mb-4">
                    <i class="fas fa-water fa-2x mb-2"></i>
                    <h5 class="mb-0">Forage MG</h5>
                    <small>Gestion des Services</small>
                </div>
                
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'home' ? ' active' : ''}" th:href="@{/}">
                            <i class="fas fa-home"></i> Accueil
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'clients' ? ' active' : ''}" th:href="@{/clients}">
                            <i class="fas fa-users"></i> Clients
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'demandes' ? ' active' : ''}" th:href="@{/demandes}">
                            <i class="fas fa-file-alt"></i> Demandes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'devis' ? ' active' : ''}" th:href="@{/devis}">
                            <i class="fas fa-file-invoice-dollar"></i> Devis
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'paiements' ? ' active' : ''}" th:href="@{/paiements}">
                            <i class="fas fa-money-bill-wave"></i> Paiements
                        </a>
                    </li>
                    <li class="nav-item mt-3">
                        <span class="nav-link disabled" style="color: rgba(255,255,255,0.5);">
                            <i class="fas fa-map-marker-alt"></i> Géographie
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'regions' ? ' active' : ''}" th:href="@{/regions}">
                            <i class="fas fa-globe-africa"></i> Régions
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'districts' ? ' active' : ''}" th:href="@{/districts}">
                            <i class="fas fa-map"></i> Districts
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'communes' ? ' active' : ''}" th:href="@{/communes}">
                            <i class="fas fa-landmark"></i> Communes
                        </a>
                    </li>
                    <li class="nav-item mt-3">
                        <span class="nav-link disabled" style="color: rgba(255,255,255,0.5);">
                            <i class="fas fa-cog"></i> Configuration
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'types-devis' ? ' active' : ''}" th:href="@{/types-devis}">
                            <i class="fas fa-list-alt"></i> Types de Devis
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'statuts' ? ' active' : ''}" th:href="@{/statuts}">
                            <i class="fas fa-check-circle"></i> Statuts
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link${activeSection === 'details-devis' ? ' active' : ''}" th:href="@{/details-devis}">
                            <i class="fas fa-stream"></i> Détails Devis
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-10 main-content">`;

function convertFile(filePath, section) {
    const content = fs.readFileSync(filePath, 'utf8');
    
    // Extract main content
    const mainMatch = content.match(/<main>([\s\S]*?)<\/main>/);
    if (!mainMatch) {
        console.log(`Skipping ${filePath} - no main tag found`);
        return;
    }
    
    const mainContent = mainMatch[1];
    
    // Extract title
    let title = 'Forage Madagascar';
    const titleMatch = content.match(/title='([^']+)'/) || content.match(/title="([^"]+)"/) || content.match(/title=\$\{([^}]+)\}/);
    if (titleMatch) {
        title = titleMatch[1];
    }
    
    // Build new HTML
    const newHtml = `<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" th:href="@{/css/custom.css}">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
${sidebar(section)}
${mainContent}
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
`;
    
    fs.writeFileSync(filePath, newHtml, 'utf8');
    console.log(`Converted: ${filePath}`);
}

// Process all folders
const folders = {
    'communes': 'communes',
    'demandes': 'demandes',
    'devis': 'devis',
    'details-devis': 'details-devis',
    'districts': 'districts',
    'paiements': 'paiements',
    'regions': 'regions',
    'statuts': 'statuts',
    'types-devis': 'types-devis'
};

const baseDir = 'src/main/resources/templates';

Object.entries(folders).forEach(([folder, section]) => {
    const folderPath = path.join(baseDir, folder);
    if (fs.existsSync(folderPath)) {
        fs.readdirSync(folderPath).forEach(file => {
            if (file.endsWith('.html')) {
                convertFile(path.join(folderPath, file), section);
            }
        });
    }
});

console.log('\nConversion complete!');
