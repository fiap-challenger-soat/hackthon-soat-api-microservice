INSERT INTO tb_user (id, email, created_at)
SELECT '3fa85f64-5717-4562-b3fc-2c963f66afa6', 'usuario@example.com', NOW()
WHERE NOT EXISTS (SELECT 1 FROM tb_user);