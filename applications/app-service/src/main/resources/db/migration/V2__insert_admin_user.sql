INSERT INTO persons (email, password, name, age, is_admin, created_at, updated_at)
VALUES (
    'admin@pragma.com',
    '$2a$12$jgrpC.b51X8IelYjTi2Di.rSxH2s7Ja3K6V84lmQZrjAv6VCgsAMO',
    'Admin Pragma',
    30,
    TRUE,
    NOW(),
    NOW()
);
