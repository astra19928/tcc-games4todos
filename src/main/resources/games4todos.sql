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
    id_uploader INT NOT NULL,
    id_creator INT,
    name_creator VARCHAR(50),
    downloads INT NOT NULL,
    release_date DATE,
    file_size FLOAT NOT NULL,
    FOREIGN KEY(id_uploader) REFERENCES user(id_user),
    FOREIGN KEY(id_creator) REFERENCES user(id_user)
);

CREATE TABLE comment(
	id_comment INT PRIMARY KEY AUTO_INCREMENT,
    id_uploader INT NOT NULL,
    id_mod INT NOT NULL,
    id_reply INT,
    content VARCHAR(300) NOT NULL,
	likes INT NOT NULL,
    FOREIGN KEY(id_uploader) REFERENCES user(id_user),
    FOREIGN KEY(id_mod) REFERENCES modification(id_modification),
    FOREIGN KEY(id_reply) REFERENCES comment(id_comment)
);

CREATE TABLE favorite(
	id_user INT NOT NULL,
    id_mod INT NOT NULL,
    PRIMARY KEY(id_user, id_mod),
    FOREIGN KEY(id_user) REFERENCES user(id_user),
    FOREIGN KEY(id_mod) REFERENCES modification(id_modification)
);

CREATE TABLE condicao(
	id_condicao INT PRIMARY KEY AUTO_INCREMENT,
    categoria ENUM('Daltonismo', 'Auditivo', 'sans undertale'),
    tipo VARCHAR(30)
);

CREATE TABLE condicao_user(
	id_condicao_user INT PRIMARY KEY AUTO_INCREMENT,
    id_user INT NOT NULL,
    id_condicao INT NOT NULL,
	tipo VARCHAR(30)
    -- se for um tipo mais especifico do que esta disponivel em condicao
    -- ie. tiver condicao de categoria 'Daltonismo' mas de um tipo especifico que não esta na
    -- tabela condicao
);

INSERT INTO condicao VALUES (null, 'Daltonismo', 'Deuteranopia'), (null, 'Daltonismo', 'Tritanopia');