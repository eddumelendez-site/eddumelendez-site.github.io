CREATE TABLE users (
  id SERIAL,
  username VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  lastname VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

INSERT INTO users (username, name, lastname) VALUES ('eddumelendez', 'Eddú', 'Meléndez');
