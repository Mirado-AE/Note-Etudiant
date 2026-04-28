# Résumé de la Cohérence de la Base de Données

## 📋 Objectif Atteint
Modification complète de la base de données pour assurer la cohérence entre:
- **Models JPA** (Entités Java)
- **Schéma SQL** (base.sql)
- **Données d'initialisation** (data.sql)

## ✅ Améliorations Apportées

### 1. **data.sql - Données d'Initialisation (Complètement Restructuré)**

#### Structure Logique
- **Statuts cohérents**: "Créée", "En cours", "Approuvée", "Rejetée", "Annulée"
- **Types de devis**: "Études de forage", "Devis matériaux", "Main d'oeuvre", "Équipement complet"
- **Régions réelles de Madagascar**: 22 régions authentiques
- **Districts hiérarchiques**: Organisés par région (5 pour Analamanga, etc.)
- **Communes complètes**: Organisées par district (125+ communes)
- **Clients professionnels**: 5 exemples with email, telefone, adresse
- **Catalogue de produits**: 12 services cohérents avec prix unitaire

#### Données Produits (Services)
```
1. Captage d'eau - 500,000 Ar
2. Forage pastoral - 400,000 Ar
3. Pompe solaire 2kW - 1,200,000 Ar
4. Pompe submersible 3kW - 1,800,000 Ar
5. Tuyauterie PVC 1" - 85,000 Ar/m
6. Tuyauterie PVC 2" - 125,000 Ar/m
7. Câble électrique 4mm² - 12,000 Ar/m
8. Moteur électrique 2kW - 800,000 Ar
9. Turbinage - 150,000 Ar/heure
10. Désensablement - 100,000 Ar/jour
11. Traitement de l'eau - 50,000 Ar
12. Installation - 200,000 Ar/forfait
```

#### Géographie Madagascar (Hiérarchique)
```
REGIONS (22)
  ↓
DISTRICTS (organisés par région)
  Analamanga: 5 districts
  Atsinanana: 5 districts
  Analanjirofo: 4 districts
  Alaotra Mangoro: 3 districts
  Boeny: 4 districts
  Sofia: 3 districts
  ↓
COMMUNES (organisées par district)
  Antananarivo Avaratra: 5 communes
  ... (125+ communes au total)
```

### 2. **base.sql - Schéma de Base de Données (Complètement Amélioré)**

#### Améliorations de Conformité
| Aspect | Ancien | Nouveau |
|--------|--------|---------|
| **Format** | PostgreSQL | MySQL/H2 compatible |
| **Codage** | utf8mb4 | utf8mb4_unicode_ci (meilleure support) |
| **Timestamps** | Manquants | created_at, updated_at par table |
| **Colonnes manquantes** | description absente | Ajoutée dans demandes, devis |
| **Unicité email** | Non contrainte | UNIQUE KEY ajoutée |
| **Indice performance** | Minimal | Complet (20+ indices) |

#### Structure des Tables (Mise à Jour)

**STATUT**
- `id` BIGINT PK AUTO_INCREMENT
- `libelle` VARCHAR(100) UNIQUE
- `created_at`, `updated_at`

**CLIENT**
- `id`, `nom`, `email` (UNIQUE), `telephone`, `adresse`
- `created_at`, `updated_at`
- Indices: nom, email

**REGION**
- `id`, `nom` (UNIQUE)
- `created_at`, `updated_at`

**DISTRICT**
- `id`, `nom`, `id_region` (FK)
- Unique: (nom, id_region) - évite les doublons par région
- `created_at`, `updated_at`
- FK: ON DELETE CASCADE (suppression région = districts supprimés)

**COMMUNE**
- `id`, `nom`, `id_district` (FK)
- Unique: (nom, id_district) - évite les doublons par district
- `created_at`, `updated_at`
- FK: ON DELETE CASCADE

**PRODUITS**
- `id`, `nom`, `description`, `prix_unitaire`, `unite`, `actif`
- `created_at`, `updated_at`
- Indices: actif, nom

**DEMANDES**
- `id`, `date_demande` (DEFAULT CURRENT_TIMESTAMP)
- `id_client`, `id_region`, `id_district`, `id_commune` (FKs)
- `id_statut` (FK, NOT NULL) - Obligatoire depuis le refactor
- `description`
- `created_at`, `updated_at`
- **Intégrité**: ON DELETE RESTRICT (empêche suppression si demandes liées)
- Indices: client, region, district, commune, statut, date

**DEVIS**
- `id`, `date_devis` (DEFAULT CURRENT_TIMESTAMP)
- `id_demande` (FK, CASCADE), `id_type_devis`, `id_statut` (FKs)
- `description` (nouveau)
- `created_at`, `updated_at`
- **Pas de montant_total** (calculé en Java avec @Transient)
- Indices: demande, type, statut, date

**DETAILS_DEVIS**
- `id`, `id_devis` (FK CASCADE), `id_produit` (FK RESTRICT)
- `quantite`, `prix_unitaire`
- `created_at`, `updated_at`
- **Calcul sousTotal**: quantite * prix_unitaire (Java côté @Transient)
- FK: ON DELETE RESTRICT pour produits (empêche suppression si utilisé)

**PAIEMENT**
- `id`, `id_devis` (FK, peut être NULL), `montant`
- `date_paiement` (DEFAULT), `mode_paiement`, `reference`
- `description` (nouveau)
- `created_at`, `updated_at`
- FK: ON DELETE SET NULL (paiement orphelin possible)

### 3. **Alignement Entités JPA ↔ Schéma**

Les entités Java suivantes correspondent maintenant parfaitement:

✅ **Statut.java** → table `statut`
- Champs: id, libelle, timestamps (JPA audit)

✅ **Client.java** → table `client`
- Champs: id, nom, email (UNIQUE), telephone, adresse, timestamps

✅ **Region.java** → table `region`
- Champs: id, nom (UNIQUE), districts(OneToMany), timestamps

✅ **District.java** → table `district`
- Champs: id, nom, region(ManyToOne FK), communes(OneToMany), timestamps
- Contrainte: (nom, id_region) UNIQUE

✅ **Commune.java** → table `commune`
- Champs: id, nom, district(ManyToOne FK), demandes(OneToMany), timestamps
- Contrainte: (nom, id_district) UNIQUE

✅ **Produit.java** → table `produits`
- Champs: id, nom, description, prixUnitaire, unite, actif, detailsDevis(OneToMany), timestamps

✅ **Demande.java** → table `demandes`
- Champs: id, dateDemande, client FK, region FK, district FK, commune FK, statut FK
- Description, timestamps
- Status auto-set to "Créée" via @PrePersist

✅ **Devis.java** → table `devis`
- Champs: id, dateDevis, demande FK (CASCADE), typeDevis FK, statut FK
- Description, timestamps
- montantTotal: @Transient computed property (NOT in DB)

✅ **DetailDevis.java** → table `details_devis`
- Champs: id, devis FK (CASCADE), produit FK, quantite, prixUnitaire
- sousTotal: @Transient computed property
- timestamps

✅ **Paiement.java** → table `paiement`
- Champs: id, devis FK (SET NULL), montant, datePaiement, modePaiement, reference, description, timestamps

## 🔒 Intégrité Référentielle

### Contraintes ON DELETE
```
Region → District: CASCADE (suppression région = districts supprimés)
District → Commune: CASCADE (suppression district = communes supprimées)

Demande → Client/Region/District/Commune/Statut: RESTRICT
(Empêche suppression si demandes liées - intégrité critique)

Devis → Demande: CASCADE (devis supprimé avec demande)
Devis → TypeDevis/Statut: RESTRICT (aie besoin d'au moins un)

DetailDevis → Devis: CASCADE (supprimer devis = supprimer lignes)
DetailDevis → Produit: RESTRICT (empêche suppression produit si utilisé)

Paiement → Devis: SET NULL (paiement reste orphelin)
```

## 📊 Indices de Performance

Total: **20+ indices** créés pour performance:

- `idx_client_nom`, `idx_client_email` - Recherche clients
- `idx_produits_actif`, `idx_produits_nom` - Filtrage produits
- `idx_demandes_*` (client, region, district, commune, statut, date, created) - Requêtes demandes
- `idx_devis_*` (demande, type, statut, date, created) - Requêtes devis
- `idx_details_*` (devis, produit) - Jointures linéaires
- `idx_paiement_*` (devis, date) - Requêtes paiements
- `idx_district_region`, `idx_commune_district` - Hiérarchie géographique

## 🔄 Flux de Travail Cohérent

### Création Demande
1. Client remplit formulaire (région → district → commune cascadés)
2. Demande créée avec statut="Créée" (auto @PrePersist)
3. Geografia stockée: region + district + commune (lié cohérent)

### Création Devis depuis Demande
1. Devis lié à Demande (FK NOT NULL)
2. Type de devis sélectionné (4 types fixes)
3. Statut héritant logique (même que demande initialement)

### Ajout Produits au Devis
1. DetailDevis créé: quantite + produit
2. prixUnitaire = copie du produit.prixUnitaire (historique)
3. sousTotal calculé en Java: quantite * prixUnitaire

### Calcul Totaux (En Java)
```java
// Devis.java
@Transient
public BigDecimal getMontantTotal() {
  return details.stream()
    .map(d -> BigDecimal.valueOf(d.getQuantite())
      .multiply(d.getPrixUnitaire()))
    .reduce(BigDecimal.ZERO, BigDecimal::add);
}
```

## 📝 Données de Référence

### Statuts Workflow
```
"Créée" → "En cours" → "Approuvée"
                    → "Rejetée"
                    → "Annulée"
```

### Types de Devis
1. Études de forage
2. Devis matériaux
3. Main d'oeuvre
4. Équipement complet

### Régions Madagascar (22)
Analamanga, Atsinanana, Analanjirofo, Alaotra Mangoro, Boeny, Sofia, Betsiboka, Melaky, Atsimo-Atsinanana, Ihorombe, Haute Matsiatra, Atsimo-Andrefana, Androy, Anosy, Menabe, Vakinankaratra, Sava, Diana, Bongolava, Itasy, Vatovavy Fitovinany, Amoron'i Mania

## ✨ Avantages de Cette Cohérence

1. **Intégrité des Données**: Contraintes FK/UNIQUE appliquées à la base
2. **Performance**: Indices optimisés pour requêtes courantes
3. **Maintenabilité**: Schéma clair et bien commenté
4. **Scalabilité**: UTF8MB4 supporte accents/caractères spéciaux
5. **Audit**: Timestamps created_at/updated_at pour toutes les tables
6. **Données Réelles**: Madagascar vraie géographie et données métier
7. **Workflow Logique**: Demande→Devis→Paiement cohérent

## 🚀 Prochaines Étapes

1. ✅ **Base de données cohérente** - DONE
2. ✅ **Entités JPA alignées** - DONE
3. ✅ **Data.sql complète** - DONE
4. ✅ **Base.sql MySQL/H2** - DONE
5. **Validation**: Exécuter application et tester workflows complets
6. **Optimisations**: Ajouter plus d'indices si nécessaire après profilage

## 📞 Contactez-moi
Pour questions sur la cohérence ou ajustements: remplacez les données/schéma comme nécessaire via les fichiers:
- [base.sql](base.sql) - Définition du schéma
- [data.sql](src/main/resources/data.sql) - Données d'initialisation
