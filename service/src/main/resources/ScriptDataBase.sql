create DATABASE payments_system;

-- create DATABASE payments_system;

create TABLE users
(
    id       BIGSERIAL PRIMARY KEY,
    login    VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255)       NOT NULL,
    role     VARCHAR(20)        NOT NULL
);

create TABLE client
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGSERIAL REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    balance      NUMERIC(10, 2),
    first_name   VARCHAR(128)                                      NOT NULL,
    last_name    VARCHAR(128)                                      NOT NULL,
    status       VARCHAR(56)                                       NOT NULL,
    created_time TIMESTAMP                                         NOT NULL
);

create TABLE card
(
    id           BIGSERIAL PRIMARY KEY,
    client_id    BIGSERIAL REFERENCES client (id) ON DELETE CASCADE NOT NULL,
    card_no      INT UNIQUE,
    balance      NUMERIC(10, 2)                                     NOT NULL,
    created_date DATE                                               NOT NULL,
    expire_date  DATE                                               NOT NULL,
    status       VARCHAR(56)                                        NOT NULL
);


create TABLE payment
(
    id               BIGSERIAL PRIMARY KEY,
    shop_name        VARCHAR(128)                                       NOT NULL,
    amount           NUMERIC(10, 2)                                     NOT NULL,
    pay_by_client_id BIGSERIAL REFERENCES client (id) ON DELETE CASCADE NOT NULL,
    pay_by_card_no   INT REFERENCES card (card_no)                      NOT NULL,
    time_of_pay      TIMESTAMP                                          NOT NULL,
    status           VARCHAR(20)                                        NOT NULL
);


create TABLE transfer
(
    id               BIGSERIAL PRIMARY KEY,
    card_no_from     INT REFERENCES card (card_no) NOT NULL,
    card_no_to       INT REFERENCES card (card_no) NOT NULL,
    amount           NUMERIC(10, 2)                NOT NULL,
    time_of_transfer TIMESTAMP                     NOT NULL,
    status           VARCHAR(20)                   NOT NULL
);

create TABLE replenishment
(
    id                    BIGSERIAL PRIMARY KEY,
    client_to             BIGSERIAL REFERENCES client (id) ON DELETE CASCADE NOT NULL,
    card_no_to            INT REFERENCES card (card_no)                      NOT NULL,
    amount                NUMERIC(10, 2)                                     NOT NULL,
    time_of_replenishment TIMESTAMP                                          NOT NULL,
    status                VARCHAR(20)                                        NOT NULL
);


