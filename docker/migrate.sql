CREATE TABLE authors
(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  username varchar(32) NOT NULL,
  nickname VARCHAR(128) NOT NULL,
  password VARCHAR(64) NOT NULL,
  valid INT(1) NOT NULL
);
