CREATE TABLE estadio (
    estadio_id UUID PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL
);

CREATE TABLE clube (
    clube_id     UUID PRIMARY KEY,
    nome         VARCHAR(255) NOT NULL,
    sigla_estado VARCHAR(2)   NOT NULL,
    data_criacao DATE         NOT NULL,
    ativo        BOOLEAN      NOT NULL
);

CREATE TABLE partida (
    partida_id         UUID PRIMARY KEY,
    clube_casa_id      UUID             NOT NULL,
    clube_visitante_id UUID             NOT NULL,
    estadio_id         UUID             NOT NULL,
    data_hora_partida  TIMESTAMP        NOT NULL,
    gols_casa          INTEGER,
    gols_visitante     INTEGER,
    CONSTRAINT fk_partida_clube_casa      FOREIGN KEY (clube_casa_id)      REFERENCES clube(clube_id),
    CONSTRAINT fk_partida_clube_visitante FOREIGN KEY (clube_visitante_id) REFERENCES clube(clube_id),
    CONSTRAINT fk_partida_estadio         FOREIGN KEY (estadio_id)         REFERENCES estadio(estadio_id)
);
