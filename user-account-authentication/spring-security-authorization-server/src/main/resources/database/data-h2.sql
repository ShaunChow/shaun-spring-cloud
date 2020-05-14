---- Spring Security (Default)----

INSERT INTO users(username,password,enabled) values ('shaun','$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q',true); --Bcrypt admin
INSERT INTO users(username,password,enabled) values ('user','$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q',true); --Bcrypt admin

INSERT INTO authorities(username,authority) values ('shaun','ROLE_ADMIN');
INSERT INTO authorities(username,authority) values ('user','ROLE_USER');

---- Spring Security (Groups)----

INSERT INTO groups(id,group_name) values (1,'GROUP_ADMIN');
INSERT INTO groups(id,group_name) values (2,'GROUP_USER');

INSERT INTO group_authorities(group_id,authority) values (1,'ROLE_GROUP_WRITE');
INSERT INTO group_authorities(group_id,authority) values (1,'ROLE_GROUP_READ');
INSERT INTO group_authorities(group_id,authority) values (2,'ROLE_GROUP_READ');

INSERT INTO group_members(group_id,username) values (1,'shaun');
INSERT INTO group_members(group_id,username) values (1,'admin');
INSERT INTO group_members(group_id,username) values (2,'user');

---- Spring Oauth2 ----

INSERT INTO oauth_client_details VALUES (
    'cc8781001',
    'client_credentials_resource',
    '$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q', --Bcrypt admin
    'read,write',
    'client_credentials,refresh_token',
    null,
    'ROLE_ADMIN,ROLE_GROUP_WRITE',
    7200,
    0,
    null,
    'true');
INSERT INTO oauth_client_details VALUES (
    'pwd8781001',
    'password_resource',
    '$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q', --Bcrypt admin
    'read,write',
    'password,check_token,refresh_token',
    null,
    null,
    7200,
    72000,
    null,
    'true');
INSERT INTO oauth_client_details VALUES (
    'ac8781001',
    'authorization_code_resource',
    '$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q', --Bcrypt admin
    'read,write',
    'authorization_code,check_token,refresh_token',
    'http://localhost:8781/oauth2/callback',
    null,
    7200,
    72000,
    null,
    'true');
INSERT INTO oauth_client_details VALUES (
    'imp8781001',
    'implicit_api',
    '$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q', --Bcrypt admin
    'read,write',
    'implicit',
    'http://localhost:8781/oauth2/callback',
    'ROLE_ADMIN,ROLE_GROUP_READ',
    7200,
    72000,
    null,
    'true');
INSERT INTO oauth_client_details VALUES (
    'oauth8781001',
    'oauth_login_resource',
    '$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q', --Bcrypt admin
    'read,write',
    'oauth2,refresh_token',
    null,
    'ROLE_ADMIN,ROLE_GROUP_WRITE',
    7200,
    0,
    null,
    'true');
