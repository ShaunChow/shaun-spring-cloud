INSERT INTO users(username,password,enabled) values('shaun','$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q',true); --Bcrypt admin
INSERT INTO users(username,password,enabled) values('user','$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q',true); --Bcrypt admin

INSERT INTO authorities(username,authority) values('shaun','ROLE_ADMIN');
INSERT INTO authorities(username,authority) values('user','ROLE_USER');