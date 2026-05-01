INSERT INTO persons (email, password, name, age, is_admin, created_at, updated_at)
VALUES (
    'admin@pragma.com',
    '$2a$10$S1jnueVl3DzB4268qIBcZercQ7Oepxvo6hJB1zpDsnNNgyHVpGsAu',
    'Admin Pragma',
    30,
    TRUE,
    NOW(),
    NOW()
);
