INSERT INTO candidat (nom, prenom, adresse, lien_photo) VALUES
('Candidat1', 'prenom', 'Antananarivo', 'photo1.jpg'),
('Candidat2', 'prenom', 'Fianarantsoa', 'photo2.jpg');

INSERT INTO correcteur (nom, prenom, adresse, lien_photo) VALUES
('Correcteur1', 'prenom', 'Toamasina', 'corr1.jpg'),
('Correcteur2', 'prenom', 'Toamasina', 'corr2.jpg'),
('Correcteur3', 'prenom', 'Toamasina', 'corr3.jpg');

INSERT INTO matiere (nom, coef) VALUES
('JAVA', 6),
('PHP', 5);

INSERT INTO examen (nom, date_debut, date_fin) VALUES
('Baccalaureat 2025', '2025-07-01', '2025-07-10');

INSERT INTO note (correcteur_id, candidat_id, matiere_id, examen_id, note) VALUES
(1, 1, 1, 1, 15),
(2, 1, 1, 1, 10),
(3, 1, 1, 1, 12),
(1, 2, 1, 1, 9),
(2, 2, 1, 1, 8),
(3, 2, 1, 1, 11),
(1, 1, 2, 1, 10),
(2, 1, 2, 1, 10),
(1, 2, 2, 1, 13),
(2, 2, 2, 1, 11);

INSERT INTO operateur (operateur) VALUES
('<'),
('<='),
('>'),
('>=');

INSERT INTO resolution (resolution) VALUES
('plus petit'),
('plus grand'),
('moyenne');

INSERT INTO parametre (matiere_id, operateur_id, seuil_sum_diff, resolution_id) VALUES
(1, 1, 7, 2),
(1, 4, 7, 3),
(2, 2, 2, 1),
(2, 3, 2, 2);