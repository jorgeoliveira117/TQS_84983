CREATE TABLE students (
   id BIGSERIAL PRIMARY KEY,
   name varchar(255),
   num int,
   email varchar(255)
);

INSERT INTO students (name, num, email) VALUES ('João Silva', 100200, 'js100@ua.pt');
INSERT INTO students (name, num, email) VALUES ('Maria Vieira', 200300, 'mvieira@ua.pt');
INSERT INTO students (name, num, email) VALUES ('Adama Vieira', 404404, 'adama@ua.pt');