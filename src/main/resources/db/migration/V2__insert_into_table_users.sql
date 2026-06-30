INSERT INTO users (
    name,
    email,
    password,
    role,
    created_at
)
SELECT
    'Wanley Alexis',
    'admin@wanroo.com',
    '$2a$10$50A/U55C69GQ6RSPUMMMmO1Ai8/D4ZAkTU2Jug.g11YAHeEYkZLFe',
    'ADMIN',
    NOW()
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE email = 'admin@wanroo.com'
);