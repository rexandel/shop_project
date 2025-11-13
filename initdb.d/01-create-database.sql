-- Создаем базу данных Marketplace если она не существует
SELECT 'CREATE DATABASE "Marketplace"'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'Marketplace')\gexec

-- Подключаемся к созданной базе данных
\c "Marketplace"

-- Создаем пользователя user если он не существует
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'user') THEN
        CREATE ROLE "user" WITH LOGIN PASSWORD '2012';
    END IF;
END
$$;

-- Даем права пользователю на базу данных
GRANT ALL PRIVILEGES ON DATABASE "Marketplace" TO "user";



