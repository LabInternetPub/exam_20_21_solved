DROP TABLE if EXISTS house;
CREATE TABLE house
(
    name VARCHAR (255) PRIMARY KEY,
    region VARCHAR (255),
    coatOfArms VARCHAR (255),
    words VARCHAR(255)
);

DROP TABLE if EXISTS student;
CREATE TABLE student
(
  id VARCHAR (256) PRIMARY KEY ,
  password VARCHAR (255) ,  
  email VARCHAR (100) ,
  name VARCHAR (255) UNIQUE,
  secondname VARCHAR (255),
  enabled TINYINT NOT NULL DEFAULT 1,
  house VARCHAR (255),
  FOREIGN KEY (house) REFERENCES house(name)
);

DROP TABLE if EXISTS classroom;
CREATE TABLE classroom
(
  name VARCHAR (255) PRIMARY KEY ,
  orientation VARCHAR (50),
  capacity integer,
  plugs TINYINT DEFAULT 0
);

DROP TABLE if EXISTS allocation;
CREATE TABLE allocation
(
  id INT AUTO_INCREMENT PRIMARY KEY,
  student VARCHAR (256),
  classroom VARCHAR (256),
  dayofweek VARCHAR (10),
  FOREIGN KEY (student) REFERENCES student(id),
  FOREIGN KEY (classroom) REFERENCES classroom(name)
);

DROP TABLE if EXISTS authorities;
CREATE TABLE authorities (
    authority_id int(11) NOT NULL AUTO_INCREMENT,
    username varchar(45) NOT NULL,
    role varchar(45) NOT NULL,
    PRIMARY KEY (authority_id),
    UNIQUE KEY uni_username_role (role,username),
    CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES student (name));
