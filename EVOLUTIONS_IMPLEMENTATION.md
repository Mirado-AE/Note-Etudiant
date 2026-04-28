# Évolutions - Historique des Demandes, Remise Automatique et Chiffre d'Affaire

## 📋 Résumé des Modifications

Ce document récapitule les modifications apportées à l'application pour:
1. **Garder l'historique des demandes** avec version tracking
2. **Appliquer une remise automatique** de 10% si prix unitaire >= 1.000.000 Ar
3. **Afficher le chiffre d'affaire prévisionnel** (montant global de tous les devis)

---

## 🗂️ Fichiers Modifiés / Créés

### 1. **Base de Données (base.sql)**

#### Modifications:

**Table `demandes`** - Ajout colonne `version`:
```sql
ALTER TABLE demandes ADD COLUMN version INTEGER DEFAULT 1;
```
- Permet de tracker l'historique des modifications
- Quand on modifie une demande, on peut créer une nouvelle version
- La version par défaut est 1

**Table `details_devis`** - Ajout colonne `remise_unitaire`:
```sql
ALTER TABLE details_devis ADD COLUMN remise_unitaire NUMERIC(15,2) DEFAULT 0;
```
- Stocke la remise appliquée par ligne
- Valeur par défaut: 0 (pas de remise)
- La remise est appliquée si prix >= 1.000.000 Ar

#### Views Créées:

**VUE: `v_chiffre_affaire_previsionnel`**
```sql
SELECT 
    SUM((dd.prix_unitaire - COALESCE(dd.remise_unitaire, 0)) * dd.quantite) as total_chiffre_affaire
FROM details_devis dd
JOIN devis d ON dd.id_devis = d.id
WHERE d.id_statut IN (SELECT id FROM statut WHERE libelle IN ('Accepté', 'En cours', 'Complété'));
```
- Calcule le montant total prévisionnel
- Exclut les devis non acceptés

**VUE: `v_chiffre_affaire_par_devis`**
- Affiche le détail du chiffre d'affaire par devis
- Inclut client, type de devis, statut, montant total

---

### 2. **Entités Java**

#### `DetailDevis.java` - Modifications:

```java
@Column(precision = 15, scale = 2)
private BigDecimal remiseUnitaire = BigDecimal.ZERO;

// Méthode pour obtenir le prix après remise
public BigDecimal getPrixApresRemise() { ... }

// Méthode pour calcul du sous-total avec remise
public BigDecimal getSousTotal() { ... }

// Méthode pour appliquer remise automatique
public void calculerRemiseAutomatique() { ... }
```

#### `Demande.java` - Modifications:

```java
@Column(name = "version", nullable = false)
private Integer version = 1;

// Également ajouté le champ description qui était manquant
@Column(name = "description")
private String description;
```

---

### 3. **Services Java**

#### ✨ Nouveau: `DashboardService.java`
```java
public BigDecimal getChiffreAffairePrevisionnelTotal()
public List<ChiffreAffaireDto> getChiffreAffaireParDevis()
```
- Service dédié pour le tableau de bord
- Récupère le chiffre d'affaire depuis la base de données

#### Modifié: `DetailDevisService.java`
```java
public void appliquerRemiseAutomatique(DetailDevis detailDevis)
public void recalculerRemisesAutomatiques(Long devisId)
```
- Applique la remise 10% automatiquement lors de la sauvegarde
- Si prix >= 1.000.000 Ar → remise = prix * 0.10

---

### 4. **Repository**

#### Modifié: `DetailDevisRepository.java`
```java
@Query(...) 
BigDecimal getChiffreAffairePrevisionnelTotal();

@Query(...)
List<Object[]> getChiffreAffaireParDevis();
```
- Requêtes natives pour récupérer le chiffre d'affaire
- Filtrage par statut (Accepté, En cours, Complété)

---

### 5. **DTO**

#### ✨ Nouveau: `ChiffreAffaireDto.java`
```java
public class ChiffreAffaireDto {
    private Long devisId;
    private LocalDateTime dateDevis;
    private Long demandeId;
    private String clientNom;
    private String typeDevis;
    private String statut;
    private BigDecimal montantTotal;
}
```
- Transfert de données du chiffre d'affaire vers la vue

---

### 6. **Contrôleurs**

#### ✨ Nouveau: `DashboardController.java`
```java
@GetMapping
public String showDashboard(Model model) {
    BigDecimal totalChiffreAffaire = dashboardService.getChiffreAffairePrevisionnelTotal();
    List<ChiffreAffaireDto> chiffreAffaireParDevis = dashboardService.getChiffreAffaireParDevis();
    
    model.addAttribute("totalChiffreAffaire", totalChiffreAffaire);
    model.addAttribute("chiffreAffaireParDevis", chiffreAffaireParDevis);
    
    return "dashboard/index";
}
```
- Endpoint: `GET /dashboard`
- Affiche le tableau de bord du chiffre d'affaire

---

### 7. **Templates HTML (Thymeleaf)**

#### ✨ Nouveau: `templates/dashboard/index.html`
- Page complète du dashboard
- Affiche:
  - **Grand total**: Chiffre d'affaire prévisionnel total
  - **Tableau détaillé**: Par devis avec client, date, type, statut, montant
  - **Code SQL** affiché à titre informatif
- Design responsive avec Bootstrap 5
- Sidebar de navigation

---

## 🔧 Utilisation

### Accéder au Dashboard:
```
http://localhost:8080/dashboard
```

### Ajouter/Modifier un détail de devis:
```java
DetailDevis detail = new DetailDevis();
detail.setPrixUnitaire(new BigDecimal("1500000")); // >= 1.000.000
detail.setQuantite(2);

detailDevisService.save(detail);
// La remise 10% sera appliquée automatiquement!
// remiseUnitaire = 1500000 * 0.10 = 150000
// prix_apres_remise = 1500000 - 150000 = 1350000
// sous_total = 1350000 * 2 = 2700000
```

### Gérer l'historique des demandes:
```java
Demande demande = demandeService.findById(1L);
// version = 1

// Modifier la demande (version incrémentée par l'application)
demande.setVersion(2);
demandeService.save(demande);
```

---

## 📊 Requêtes SQL Importantes

Consultez le fichier `REQUETES_CHIFFRE_AFFAIRE.sql` pour:

1. **Chiffre d'affaire total** - Total prévisionnel global
2. **Par devis** - Détail avec client et type
3. **Par client** - Agrégation par client
4. **Analyse des remises** - Nombre de remises appliquées
5. **Historique des demandes** - Version tracking
6. **Détails avec remise** - Chaque ligne avec remise

---

## ✅ Checklist d'Implementation

- ✅ Ajouter colonne `version` à `demandes`
- ✅ Ajouter colonne `remise_unitaire` à `details_devis`
- ✅ Créer VUE du chiffre d'affaire
- ✅ Modifier entité `DetailDevis`
- ✅ Modifier entité `Demande`
- ✅ Créer DTO `ChiffreAffaireDto`
- ✅ Créer service `DashboardService`
- ✅ Modifier service `DetailDevisService` pour remise auto
- ✅ Modifier repository `DetailDevisRepository`
- ✅ Créer contrôleur `DashboardController`
- ✅ Créer page HTML `dashboard/index.html`
- ✅ Documenter requêtes SQL

---

## 🎯 Prochaines Étapes (Optionnel)

1. **Graphiques du chiffre d'affaire**: Ajouter Chart.js pour visualiser les tendances
2. **Export Excel**: Générer rapport Excel du chiffre d'affaire
3. **Filtres temporels**: Filtrer par date, region, client
4. **Notifications**: Alerter si chiffre d'affaire atteint un seuil
5. **API REST**: Créer endpoints pour chiffre d'affaire en JSON

---

## 📝 Notes

- Les remises ne s'appliquent **QUE SI prix unitaire >= 1.000.000 Ar**
- La remise est **10% du prix unitaire**
- Le chiffre d'affaire prévisionnel inclut **SEULEMENT** les devis:
  - Accepté
  - En cours
  - Complété
- Les devis "Rejeté", "Attente", etc. sont **EXCLUS**

---

**Date de création**: 20 Avril 2026  
**Version**: 1.0  
**Statut**: ✅ Complété
