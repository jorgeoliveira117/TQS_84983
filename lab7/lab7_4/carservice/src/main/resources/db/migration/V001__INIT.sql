CREATE TABLE cars (
      id BIGSERIAL PRIMARY KEY,
      maker varchar(255),
      model varchar(255)
);

INSERT INTO cars (maker, model) VALUES ('Porsche', 'Taycan');
INSERT INTO cars (maker, model) VALUES ('Rimac', 'Concept Two');
INSERT INTO cars (maker, model) VALUES ('Audi', 'RS3');