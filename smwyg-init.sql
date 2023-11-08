CREATE DATABASE smwyg;
USE smwyg;
CREATE USER 'smwyg-api'@'localhost' IDENTIFIED BY 'iwswyg';
GRANT ALL ON smwyg.* TO 'smwyg-api'@'localhost';