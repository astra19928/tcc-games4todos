DROP DATABASE IF EXISTS GAMES4TODOS;
CREATE DATABASE GAMES4TODOS;
USE GAMES4TODOS;

CREATE TABLE user(
	id_user INT PRIMARY KEY AUTO_INCREMENT,
	nickname VARCHAR(45) NOT NULL,
	email VARCHAR(250) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL
);

CREATE TABLE modification(
    id_modification INT PRIMARY KEY AUTO_INCREMENT,
    id_uploader INT,
    game_name VARCHAR(100) NOT NULL,
    name_developers VARCHAR(80),
    mod_name VARCHAR(100) NOT NULL,
    description TEXT,
    downloads INT NOT NULL,
    release_date DATE,
    file_size FLOAT NOT NULL,
    FOREIGN KEY(id_uploader) REFERENCES user(id_user) ON DELETE SET NULL
);

CREATE TABLE comment(
	id_comment INT PRIMARY KEY AUTO_INCREMENT,
    id_uploader INT,
    id_mod INT NOT NULL,
    content VARCHAR(300) NOT NULL,
	likes INT NOT NULL,
    send_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    FOREIGN KEY(id_uploader) REFERENCES user(id_user) ON DELETE SET NULL,
    FOREIGN KEY(id_mod) REFERENCES modification(id_modification) ON DELETE CASCADE
);

CREATE TABLE favorite(
	id_user INT NOT NULL,
    id_mod INT NOT NULL,
    PRIMARY KEY(id_user, id_mod),
    FOREIGN KEY(id_user) REFERENCES user(id_user) ON DELETE CASCADE,
    FOREIGN KEY(id_mod) REFERENCES modification(id_modification) ON DELETE CASCADE
);

CREATE TABLE disability_category(
    id_category INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL UNIQUE
);

CREATE TABLE disability_type(
    id_type INT PRIMARY KEY AUTO_INCREMENT,
    id_category INT NOT NULL,
    name VARCHAR(80) NOT NULL,
    UNIQUE(id_category, name),
    UNIQUE(id_type, id_category),
    FOREIGN KEY(id_category) REFERENCES disability_category(id_category) ON DELETE CASCADE
);

CREATE TABLE user_disability(
    id_user INT NOT NULL,
    id_category INT NOT NULL,
    id_type INT NOT NULL,
    PRIMARY KEY(id_user, id_category),
    FOREIGN KEY(id_user) REFERENCES user(id_user) ON DELETE CASCADE,
    FOREIGN KEY(id_type, id_category) REFERENCES disability_type(id_type, id_category) ON DELETE CASCADE
);

INSERT INTO disability_category(id_category, name) VALUES
    (1, 'Visão baixa'),
    (2, 'Daltonismo'),
    (3, 'Perda auditiva');

INSERT INTO disability_type(id_type, id_category, name) VALUES
    -- Visão baixa
    (1,  1, 'Cegueira total'),
    (2,  1, 'Visão monocular'),
    (3,  1, 'Baixa acuidade visual'),
    (4,  1, 'Redução do campo visual'),
    (5,  1, 'Sensibilidade à luz'),
    -- Daltonismo
    (6,  2, 'Protanopia'),
    (7,  2, 'Protanomalia'),
    (8,  2, 'Deuteranopia'),
    (9,  2, 'Deuteranomalia'),
    (10, 2, 'Tritanopia'),
    (11, 2, 'Tritanomalia'),
    (12, 2, 'Acromatopsia (monocromacia)'),
    -- Perda auditiva
    (13, 3, 'Surdez total'),
    (14, 3, 'Perda auditiva leve'),
    (15, 3, 'Perda auditiva moderada'),
    (16, 3, 'Perda auditiva severa'),
    (17, 3, 'Perda auditiva unilateral');