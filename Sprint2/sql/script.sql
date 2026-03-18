CREATE DATABASE note_db;
USE note_db;


CREATE TABLE Candidat (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    numero VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Matiere (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    coefficient INT
);

CREATE TABLE Prof (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE Signe (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(40) NOT NULL
);

CREATE TABLE Action (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL
);

CREATE TABLE Note (
    id SERIAL PRIMARY KEY,
    idetudiant INT REFERENCES Candidat(id),
    idProf INT REFERENCES Prof(id),
    idmatiere INT REFERENCES Matiere(id),
    note DOUBLE PRECISION 
);

CREATE TABLE Paramettre (
    id SERIAL PRIMARY KEY,
    idmatiere INT REFERENCES Matiere(id),
    nombredifference DOUBLE PRECISION, 
    idsigne INT REFERENCES Signe(id),
    idaction INT REFERENCES Action(id)
);