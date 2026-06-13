-- Regioes de trilha reais (seed). Sem cadastro de regioes ainda; estas servem
-- de base para criar aventuras. Idempotente: nao duplica se ja existirem.
INSERT INTO regioes (id, nome, descricao, lat_min, lat_max, lng_min, lng_max) VALUES
    ('regiao-caparao', 'Serra do Caparao', 'Pico da Bandeira, na divisa MG/ES', -20.50, -20.38, -41.85, -41.70),
    ('regiao-itatiaia', 'Parque Nacional do Itatiaia', 'Agulhas Negras e Prateleiras (RJ/MG)', -22.45, -22.30, -44.75, -44.55),
    ('regiao-mantiqueira', 'Serra da Mantiqueira', 'Montanhas entre SP, MG e RJ', -22.80, -22.30, -45.60, -44.50),
    ('regiao-orgaos', 'Serra dos Orgaos', 'Dedo de Deus, Teresopolis (RJ)', -22.52, -22.40, -43.05, -42.90),
    ('regiao-diamantina', 'Chapada Diamantina', 'Vale do Pati e Cachoeira da Fumaca (BA)', -13.50, -12.00, -41.90, -41.00),
    ('regiao-veadeiros', 'Chapada dos Veadeiros', 'Cerrado e cachoeiras de Goias', -14.30, -13.70, -47.80, -47.30)
ON CONFLICT (id) DO NOTHING;
