CREATE INDEX idx_clube_sigla_estado_nome
    ON clube (sigla_estado, nome);

CREATE INDEX idx_clube_ativo_sigla_estado
    ON clube (ativo, sigla_estado);

CREATE INDEX idx_estadio_nome
    ON estadio (nome);

CREATE INDEX idx_partida_clube_casa_data
    ON partida (clube_casa_id, data_hora_partida);

CREATE INDEX idx_partida_clube_visitante_data
    ON partida (clube_visitante_id, data_hora_partida);

CREATE INDEX idx_partida_estadio_data
    ON partida (estadio_id, data_hora_partida);

CREATE INDEX idx_partida_clubes_casa_visitante
    ON partida (clube_casa_id, clube_visitante_id);

CREATE INDEX idx_partida_clubes_visitante_casa
    ON partida (clube_visitante_id, clube_casa_id);
