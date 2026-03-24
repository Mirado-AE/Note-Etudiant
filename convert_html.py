import os
import re

# Sidebar HTML template
SIDEBAR = '''            <!-- Sidebar -->
            <div class="col-md-2 sidebar">
                <div class="text-center mb-4">
                    <i class="fas fa-water fa-2x mb-2"></i>
                    <h5 class="mb-0">Forage MG</h5>
                    <small>Gestion des Services</small>
                </div>
                
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link{active_home}" th:href="@{{/}}">
                            <i class="fas fa-home"></i> Accueil
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_clients}" th:href="@{{/clients}}">
                            <i class="fas fa-users"></i> Clients
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_demandes}" th:href="@{{/demandes}}">
                            <i class="fas fa-file-alt"></i> Demandes
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_devis}" th:href="@{{/devis}}">
                            <i class="fas fa-file-invoice-dollar"></i> Devis
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_paiements}" th:href="@{{/paiements}}">
                            <i class="fas fa-money-bill-wave"></i> Paiements
                        </a>
                    </li>
                    <li class="nav-item mt-3">
                        <span class="nav-link disabled" style="color: rgba(255,255,255,0.5);">
                            <i class="fas fa-map-marker-alt"></i> Géographie
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_regions}" th:href="@{{/regions}}">
                            <i class="fas fa-globe-africa"></i> Régions
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_districts}" th:href="@{{/districts}}">
                            <i class="fas fa-map"></i> Districts
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_communes}" th:href="@{{/communes}}">
                            <i class="fas fa-landmark"></i> Communes
                        </a>
                    </li>
                    <li class="nav-item mt-3">
                        <span class="nav-link disabled" style="color: rgba(255,255,255,0.5);">
                            <i class="fas fa-cog"></i> Configuration
                        </span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_types}" th:href="@{{/types-devis}}">
                            <i class="fas fa-list-alt"></i> Types de Devis
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_statuts}" th:href="@{{/statuts}}">
                            <i class="fas fa-check-circle"></i> Statuts
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link{active_details}" th:href="@{{/details-devis}}">
                            <i class="fas fa-stream"></i> Détails Devis
                        </a>
                    </li>
                </ul>
            </div>
            
            <!-- Main Content -->
            <div class="col-md-10 main-content">'''

def get_active_class(folder):
    """Determine which nav item should be active based on folder"""
    active_map = {
        'clients': 'clients',
        'demandes': 'demandes',
        'devis': 'devis',
        'paiements': 'paiements',
        'regions': 'regions',
        'districts': 'districts',
        'communes': 'communes',
        'types-devis': 'types',
        'statuts': 'statuts',
        'details-devis': 'details'
    }
    
    sidebar = SIDEBAR
    for key in ['home', 'clients', 'demandes', 'devis', 'paiements', 'regions', 'districts', 'communes', 'types', 'statuts', 'details']:
        if active_map.get(folder) == key:
            sidebar = sidebar.replace(f'{{active_{key}}}', ' active')
        else:
            sidebar = sidebar.replace(f'{{active_{key}}}', '')
    
    return sidebar

def convert_html_file(filepath, folder):
    """Convert a single HTML file from layout-based to standalone"""
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Extract the main content (everything between <main> tags)
    main_match = re.search(r'<main>(.*?)</main>', content, re.DOTALL)
    if not main_match:
        print(f"No <main> tag found in {filepath}")
        return False
    
    main_content = main_match.group(1)
    
    # Extract title from the layout replace directive
    title_match = re.search(r"title='([^']+)'", content) or re.search(r'title="([^"]+)"', content) or re.search(r'title=\$\{([^}]+)\}', content)
    if title_match:
        title = title_match.group(1)
    else:
        title = "Forage Madagascar"
    
    # Get the appropriate sidebar with active class
    sidebar = get_active_class(folder)
    
    # Build the new HTML
    new_html = f'''<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{title}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
    <link rel="stylesheet" th:href="@{{/css/custom.css}}">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
{sidebar}
{main_content}
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
'''
    
    # Write the new content
    with open(filepath, 'w', encoding='utf-8') as f:
        f.write(new_html)
    
    print(f"Converted: {filepath}")
    return True

# Process all HTML files in subdirectories
base_dir = 'src/main/resources/templates'
folders = ['clients', 'communes', 'demandes', 'devis', 'details-devis', 'districts', 'paiements', 'regions', 'statuts', 'types-devis']

for folder in folders:
    folder_path = os.path.join(base_dir, folder)
    if os.path.exists(folder_path):
        for filename in os.listdir(folder_path):
            if filename.endswith('.html'):
                filepath = os.path.join(folder_path, filename)
                convert_html_file(filepath, folder)

print("\\nConversion complete!")
