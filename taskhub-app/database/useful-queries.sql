-- Run these in MySQL Workbench AFTER the app has started and you have registered users.
USE taskhub_db;

-- See all registered users (passwords are stored as BCrypt hashes, never plain text)
SELECT id, full_name, email, role, created_at FROM users;

-- See every task with its owner
SELECT t.id, t.title, t.status, t.due_date, u.email AS owner
FROM tasks t JOIN users u ON u.id = t.owner_id
ORDER BY t.id DESC;

-- Count tasks per user
SELECT u.email, COUNT(t.id) AS total_tasks
FROM users u LEFT JOIN tasks t ON t.owner_id = u.id
GROUP BY u.email;
