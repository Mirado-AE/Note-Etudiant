INSERT INTO type_devis (libelle) VALUES
('Etudes de forage'),
('Devis materiaux'),
('Main d''oeuvre'),
('Equipement complet');

INSERT INTO statut (libelle) VALUES
('Brouillon'),
('En cours'),
('Valide'),
('En attente'),
('Termine'),
('Annule');

INSERT INTO region (nom) VALUES
('Analamanga'),
('Atsinanana'),
('Analanjirofo'),
('Alaotra Mangoro'),
('Boeny'),
('Sofia'),
('Betsiboka'),
('Melaky'),
('Atsimo-Atsinanana'),
('Ihorombe'),
('Haute Matsiatra'),
('Atsimo-Andrefana'),
('Androy'),
('Anosy'),
('Menabe'),
('Vakinankaratra'),
('Sava'),
('Diana'),
('Bongolava'),
('Itasy');

INSERT INTO district (nom, id_region) VALUES
('Antananarivo Avaratra', 1),
('Antananarivo Atsimondrano', 1),
('Ambatolampy', 1),
('Antananarivo Renivohitra', 1),
('Mandoto', 1),
('Toamasina I', 2),
('Toamasina II', 2),
('Brickaville', 2),
('Vatomandry', 2);

INSERT INTO commune (nom, id_district) VALUES
('Ambohitrinaina', 1),
('Anjanahary', 1),
('Soavina', 1),
('Manjakaray', 1),
('Ampahateza', 1),
('Toamasina Centre', 6),
('Tanambao', 6),
('Mangarano', 6),
('Betsiboka', 6);
