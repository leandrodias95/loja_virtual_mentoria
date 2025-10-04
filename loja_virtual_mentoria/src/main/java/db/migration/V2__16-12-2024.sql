DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.constraint_column_usage
        WHERE table_name = 'usuarios_acesso'
          AND constraint_name = 'uk8bak9jswon2id2jbunuqlfl9e'
    ) THEN
        ALTER TABLE usuarios_acesso DROP CONSTRAINT "uk8bak9jswon2id2jbunuqlfl9e";
    END IF;
END$$;