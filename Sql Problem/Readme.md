# Database query

i have used mysql to write this query

## First seed the database

### DDL

```sql
-- test.training_details definition

CREATE TABLE `training_details` (
  `user_training_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `training_id` int DEFAULT NULL,
  `training_date` date DEFAULT NULL,
  PRIMARY KEY (`user_training_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- test.`user` definition

CREATE TABLE `user` (
  `user_id` int NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```

### Seeding Sql

```sql

INSERT INTO user (user_id, username) VALUES (1, 'John Doe');
INSERT INTO user (user_id, username) VALUES (2, 'Jane Don');
INSERT INTO user (user_id, username) VALUES (3, 'Alice Jones');
INSERT INTO user (user_id, username) VALUES (4, 'Lisa Romero');

INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (1, 1, 1, '2015-08-02');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (2, 2, 1, '2015-08-03');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (3, 3, 2, '2015-08-02');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (4, 4, 2, '2015-08-04');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (5, 2, 2, '2015-08-03');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (6, 1, 1, '2015-08-02');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (7, 3, 2, '2015-08-04');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (8, 4, 3, '2015-08-03');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (9, 1, 4, '2015-08-03');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (10, 3, 1, '2015-08-02');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (11, 4, 2, '2015-08-04');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (12, 3, 2, '2015-08-02');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (13, 1, 1, '2015-08-02');
INSERT INTO training_details (user_training_id, user_id, training_id, training_date) VALUES (14, 4, 3, '2015-08-03');

```

## SQL query

```sql
SELECT u.user_id, u.username, td.training_id, td.training_date, COUNT(*) AS count
FROM user u
JOIN training_details td ON u.user_id = td.user_id
GROUP BY u.user_id, td.training_id, td.training_date
HAVING COUNT(*) > 1
ORDER BY td.training_date DESC;
```
