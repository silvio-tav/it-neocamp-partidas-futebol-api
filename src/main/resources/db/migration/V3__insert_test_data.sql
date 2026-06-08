-- Estádios adicionais
INSERT INTO estadio (estadio_id, nome) VALUES
    ('a1b2c3d4-0006-0000-0000-000000000006', 'Estádio São Januário'),
    ('a1b2c3d4-0007-0000-0000-000000000007', 'Beira-Rio');

-- Clubes adicionais (um inativo para teste)
INSERT INTO clube (clube_id, nome, sigla_estado, data_criacao, ativo) VALUES
    ('b1b2c3d4-0008-0000-0000-000000000008', 'Internacional', 'RS', '1909-04-04', true),
    ('b1b2c3d4-0009-0000-0000-000000000009', 'Vasco',         'RJ', '1898-08-21', true),
    ('b1b2c3d4-0010-0000-0000-000000000010', 'Santos',        'SP', '1912-04-14', false);

-- Partidas cobrindo os seguintes cenários de teste:
--
-- Goleadas (diferença >= 3):
--   Flamengo 4x0 Vasco, Palmeiras 5x1 Santos, Grêmio 0x3 Flamengo
--
-- Múltiplos confrontos entre os mesmos clubes (retrospecto por adversário):
--   Flamengo x Fluminense: 3 partidas (Flamengo como mandante 2x, visitante 1x)
--   Flamengo x Atlético Mineiro: 2 partidas
--
-- Atuação mandante/visitante do Flamengo:
--   Mandante: vence 2, empata 1, perde 0
--   Visitante: vence 2, empata 0, perde 1

INSERT INTO partida (partida_id, clube_casa_id, clube_visitante_id, estadio_id, data_hora_partida, gols_casa, gols_visitante) VALUES

    -- Flamengo (mandante) x Fluminense — vitória Flamengo 3x0 (goleada)
    ('c1b2c3d4-0005-0000-0000-000000000005',
     'b1b2c3d4-0001-0000-0000-000000000001', 'b1b2c3d4-0002-0000-0000-000000000002',
     'a1b2c3d4-0001-0000-0000-000000000001', '2024-06-01 16:00:00', 3, 0),

    -- Fluminense (mandante) x Flamengo — vitória Flamengo de virada 2x1
    ('c1b2c3d4-0006-0000-0000-000000000006',
     'b1b2c3d4-0002-0000-0000-000000000002', 'b1b2c3d4-0001-0000-0000-000000000001',
     'a1b2c3d4-0001-0000-0000-000000000001', '2024-07-10 19:00:00', 1, 2),

    -- Flamengo (mandante) x Vasco — goleada 4x0
    ('c1b2c3d4-0007-0000-0000-000000000007',
     'b1b2c3d4-0001-0000-0000-000000000001', 'b1b2c3d4-0009-0000-0000-000000000009',
     'a1b2c3d4-0001-0000-0000-000000000001', '2024-07-25 16:00:00', 4, 0),

    -- Grêmio (mandante) x Flamengo — goleada Flamengo 0x3
    ('c1b2c3d4-0008-0000-0000-000000000008',
     'b1b2c3d4-0006-0000-0000-000000000006', 'b1b2c3d4-0001-0000-0000-000000000001',
     'a1b2c3d4-0005-0000-0000-000000000005', '2024-08-10 18:00:00', 0, 3),

    -- Flamengo (mandante) x Atlético Mineiro — empate 1x1
    ('c1b2c3d4-0009-0000-0000-000000000009',
     'b1b2c3d4-0001-0000-0000-000000000001', 'b1b2c3d4-0005-0000-0000-000000000005',
     'a1b2c3d4-0001-0000-0000-000000000001', '2024-08-25 16:00:00', 1, 1),

    -- Atlético Mineiro (mandante) x Flamengo — vitória Atlético 2x0
    ('c1b2c3d4-0010-0000-0000-000000000010',
     'b1b2c3d4-0005-0000-0000-000000000005', 'b1b2c3d4-0001-0000-0000-000000000001',
     'a1b2c3d4-0004-0000-0000-000000000004', '2024-09-15 11:00:00', 2, 0),

    -- Palmeiras (mandante) x Santos — goleada 5x1
    ('c1b2c3d4-0011-0000-0000-000000000011',
     'b1b2c3d4-0003-0000-0000-000000000003', 'b1b2c3d4-0010-0000-0000-000000000010',
     'a1b2c3d4-0002-0000-0000-000000000002', '2024-09-22 15:00:00', 5, 1),

    -- Corinthians (mandante) x Palmeiras — vitória Corinthians 1x0
    ('c1b2c3d4-0012-0000-0000-000000000012',
     'b1b2c3d4-0004-0000-0000-000000000004', 'b1b2c3d4-0003-0000-0000-000000000003',
     'a1b2c3d4-0003-0000-0000-000000000003', '2024-10-06 16:00:00', 1, 0),

    -- Internacional (mandante) x Grêmio — empate 2x2
    ('c1b2c3d4-0013-0000-0000-000000000013',
     'b1b2c3d4-0008-0000-0000-000000000008', 'b1b2c3d4-0006-0000-0000-000000000006',
     'a1b2c3d4-0007-0000-0000-000000000007', '2024-10-20 18:00:00', 2, 2),

    -- Grêmio (mandante) x Internacional — vitória Grêmio 3x1
    ('c1b2c3d4-0014-0000-0000-000000000014',
     'b1b2c3d4-0006-0000-0000-000000000006', 'b1b2c3d4-0008-0000-0000-000000000008',
     'a1b2c3d4-0005-0000-0000-000000000005', '2024-11-10 17:00:00', 3, 1),

    -- Fluminense (mandante) x Vasco — vitória Fluminense 2x0
    ('c1b2c3d4-0015-0000-0000-000000000015',
     'b1b2c3d4-0002-0000-0000-000000000002', 'b1b2c3d4-0009-0000-0000-000000000009',
     'a1b2c3d4-0006-0000-0000-000000000006', '2024-11-24 16:00:00', 2, 0),

    -- Vasco (mandante) x Fluminense — goleada Vasco 4x1
    ('c1b2c3d4-0016-0000-0000-000000000016',
     'b1b2c3d4-0009-0000-0000-000000000009', 'b1b2c3d4-0002-0000-0000-000000000002',
     'a1b2c3d4-0006-0000-0000-000000000006', '2024-12-08 15:00:00', 4, 1);
