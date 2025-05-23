CREATE TABLE public.movie (
	id          SERIAL              PRIMARY KEY  NOT NULL,
	name        VARCHAR(256)        NOT NULL,
	genre       VARCHAR(256)        NOT NULL,
	duration    DECIMAL             NOT NULL CHECK(duration > 0)
);


CREATE TABLE public.customer (
	id          SERIAL              PRIMARY KEY  NOT NULL,
	name        VARCHAR(256)        NOT NULL,
	email       TEXT                NOT NULL	CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);


CREATE TABLE public.ticket (
    id              SERIAL              PRIMARY KEY  NOT NULL,
    movie_id        INTEGER             NOT NULL,
    customer_id     INTEGER             NOT NULL,
    session_date    TIMESTAMP           NOT NULL,
    seat            INTEGER             NOT NULL,
    price           DECIMAL(10, 2)      NOT NULL CHECK (price >= 0),
    FOREIGN KEY (movie_id) REFERENCES public.movie(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES public.customer(id) ON DELETE CASCADE
);

