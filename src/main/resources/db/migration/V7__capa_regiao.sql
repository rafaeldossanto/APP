-- Foto de capa da regiao (colecao de aventuras): URL do binario ja armazenado
-- no servico de midia. Opcional — sem capa a UI mostra o icone padrao.
-- Em dev o Hibernate (ddl-auto) cria a coluna; esta migration garante o mesmo
-- em prod (validate).
ALTER TABLE regioes ADD COLUMN IF NOT EXISTS capa_url VARCHAR(1024);
