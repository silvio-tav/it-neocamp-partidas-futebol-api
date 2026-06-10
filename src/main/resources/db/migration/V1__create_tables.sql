CREATE TABLE estadio (
    estadio_id CHAR(36) PRIMARY KEY,
    nome       VARCHAR(255) NOT NULL
);

CREATE TABLE clube (
    clube_id     CHAR(36) PRIMARY KEY,
    nome         VARCHAR(255) NOT NULL,
    sigla_estado VARCHAR(2)   NOT NULL,
    data_criacao DATE         NOT NULL,
    ativo        BOOLEAN      NOT NULL
);

CREATE TABLE partida (
    partida_id         CHAR(36) PRIMARY KEY,
    clube_casa_id      CHAR(36)         NOT NULL,
    clube_visitante_id CHAR(36)         NOT NULL,
    estadio_id         CHAR(36)         NOT NULL,
    data_hora_partida  TIMESTAMP        NOT NULL,
    gols_casa          INTEGER,
    gols_visitante     INTEGER,
    CONSTRAINT fk_partida_clube_casa      FOREIGN KEY (clube_casa_id)      REFERENCES clube(clube_id),
    CONSTRAINT fk_partida_clube_visitante FOREIGN KEY (clube_visitante_id) REFERENCES clube(clube_id),
    CONSTRAINT fk_partida_estadio         FOREIGN KEY (estadio_id)         REFERENCES estadio(estadio_id)
);
