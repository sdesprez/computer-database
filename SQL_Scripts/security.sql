drop table if exists user;

CREATE TABLE user 
( id integer not null auto_increment
, username 			varchar(50) unique
, password			varchar(50)
, enabled 			boolean
, role 				varchar(50)
, constraint pk_company primary key (id));

INSERT INTO user VALUES (1, 'user', '2858e5c6cbe232057e99a4e1fda6b733', TRUE, 'ROLE_ADMIN');

#password is hashed with MD5 of :    USERNAME:REALM:PASSWORD