---- Spring Security (Default)----

INSERT INTO users(username,password,enabled) values ('shaun','$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q',true); --Bcrypt admin
INSERT INTO users(username,password,enabled) values ('user','$2a$10$bcy1p7wpkwZZ6o8MfNWVEe0/HAuL6dmqIJ11rYRmwFOnBjj/Pf54q',true); --Bcrypt admin

INSERT INTO authorities(username,authority) values ('shaun','ROLE_ADMIN');
INSERT INTO authorities(username,authority) values ('user','ROLE_USER');

---- Spring Security (Groups)----

INSERT INTO groups(id,group_name) values (1,'GROUP_ADMIN');
INSERT INTO groups(id,group_name) values (2,'GROUP_USER');

INSERT INTO group_authorities(group_id,authority) values (1,'ROLE_WRITE');
INSERT INTO group_authorities(group_id,authority) values (1,'ROLE_READ');
INSERT INTO group_authorities(group_id,authority) values (2,'ROLE_READ');

INSERT INTO group_members(group_id,username) values (1,'shaun');
INSERT INTO group_members(group_id,username) values (1,'admin');
INSERT INTO group_members(group_id,username) values (2,'user');

---- Spring Oauth2 ----

INSERT INTO oauth_client_details VALUES (
    'curl_client',
    'product_api',
    '$2a$10$5ze/vcFOsQBF1og.s.eQ0.8VdsUXh7zzul8VM0Dzcq/NKVNrD8ffO',
    'read,write',
    'client_credentials',
    'http://localhost:8781/oauth2/accessToken',
    'ROLE_PRODUCT_ADMIN',
    7200,
    0,
    null,
    'true');
INSERT INTO oauth_client_details VALUES (
    'client_code',
    'product_api',
    '$2a$10$5ze/vcFOsQBF1og.s.eQ0.8VdsUXh7zzul8VM0Dzcq/NKVNrD8ffO',
    'read,write',
    'authorization_code,refresh_token',
    'http://localhost:8781/oauth2/code',
    'ROLE_PRODUCT_ADMIN',
    7200,
    72000,
    null,
    'true');
INSERT INTO oauth_client_details VALUES (
    'client_implicit',
    'product_api',
    '$2a$10$5ze/vcFOsQBF1og.s.eQ0.8VdsUXh7zzul8VM0Dzcq/NKVNrD8ffO',
    'read,write',
    'implicit',
    'http://localhost:8781/oauth2/accessToken',
    'ROLE_PRODUCT_ADMIN',
    7200,
    72000,
    null,
    'true');
