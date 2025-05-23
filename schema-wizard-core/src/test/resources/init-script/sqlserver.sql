CREATE DATABASE swtest;

USE swtest;

CREATE SCHEMA swtest;

CREATE LOGIN admin WITH PASSWORD = 'Password1!';

ALTER LOGIN admin WITH DEFAULT_DATABASE = swtest;

CREATE USER admin FOR LOGIN admin;

ALTER ROLE db_owner ADD MEMBER admin;

ALTER USER admin WITH DEFAULT_SCHEMA = swtest;