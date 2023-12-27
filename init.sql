# Créer la base de données
CREATE DATABASE gestion_userr;

CREATE TABLE user (
                      id INT(4) NOT NULL ,
                      email VARCHAR(50) NOT NULL,
                      password VARCHAR(100) NOT NULL,
                      passwordHashed VARCHAR(100) NOT NULL,
                      PRIMARY KEY (email)
);

CREATE TABLE role (
                      id INT(4) NOT NULL AUTO_INCREMENT,
                      nom VARCHAR(50) NOT NULL,
                      PRIMARY KEY (id)
);
-- Insérer des données initiales
INSERT INTO role (nom) VALUES ('Admin');
INSERT INTO user (id, email, password, passwordHashed) VALUES (1, 'admin1@gmail.com','�N�Eȥ�x}''kd&?�', '�N�Eȥ�x}''kd&?�');
# mdp = Admin123@