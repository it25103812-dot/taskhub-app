-- TaskHub database for MySQL Workbench
-- OPTIONAL: the app creates all of this automatically on first run.
-- Run this file (lightning icon) if you want to create the tables yourself.

CREATE DATABASE IF NOT EXISTS taskhub_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE taskhub_db;

CREATE TABLE IF NOT EXISTS users (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name   VARCHAR(100) NOT NULL,
  email       VARCHAR(150) NOT NULL UNIQUE,
  password    VARCHAR(255) NOT NULL,
  role        VARCHAR(20)  NOT NULL,
  created_at  DATETIME(6)  NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(120) NOT NULL,
  description VARCHAR(1000),
  status      VARCHAR(20)  NOT NULL,
  due_date    DATE,
  owner_id    BIGINT NOT NULL,
  created_at  DATETIME(6)  NOT NULL,
  CONSTRAINT fk_tasks_owner FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);
