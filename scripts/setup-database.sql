-- ===========================================
-- Script de Setup do Banco de Dados
-- ===========================================

-- Criar banco de dados
CREATE DATABASE IF NOT EXISTS barbearia_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Criar usuário
CREATE USER IF NOT EXISTS 'barbearia_user'@'localhost' IDENTIFIED BY 'barbearia_password';

-- Conceder privilégios
GRANT ALL PRIVILEGES ON barbearia_db.* TO 'barbearia_user'@'localhost';

-- Aplicar mudanças
FLUSH PRIVILEGES;

-- Usar o banco
USE barbearia_db;

-- Verificar se está funcionando
SELECT 'Database setup completed successfully!' as status;