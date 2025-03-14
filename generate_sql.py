import os

# Путь к SQL-файлу для инициализации БД
output_file = "src/main/resources/data.sql"

# SQL-скрипт для создания таблиц и вставки данных
sql_statements = """
CREATE TABLE public.movie (
	id          SERIAL              PRIMARY KEY  NOT NULL,
	name        VARCHAR(256)        NOT NULL,
	genre       VARCHAR(256)        NOT NULL,
	duration    DECIMAL             NOT NULL CHECK(duration > 0)
);

CREATE TABLE public.customer (
	id          SERIAL              PRIMARY KEY  NOT NULL,
	name        VARCHAR(256)        NOT NULL,
	email       TEXT                NOT NULL CHECK (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')
);

CREATE TABLE public.ticket (
    id              SERIAL              PRIMARY KEY  NOT NULL,
    movie_id        INTEGER             NOT NULL,
    customer_id     INTEGER             NOT NULL,
    session_date    TIMESTAMP           NOT NULL,
    seat            INTEGER             NOT NULL,
    price           DECIMAL(10, 2)      NOT NULL CHECK (price >= 0),
    FOREIGN KEY (movie_id) REFERENCES public.movie(id),
    FOREIGN KEY (customer_id) REFERENCES public.customer(id)
);

INSERT INTO public.movie (name, genre, duration) VALUES
('The Matrix', 'Sci-Fi', 136.0),
('Titanic', 'Romance', 195.0),
('Inception', 'Thriller', 148.0),
('The Lion King', 'Animation', 88.0);

INSERT INTO public.customer (name, email) VALUES
('Alice Johnson', 'alice.johnson@example.com'),
('Bob Smith', 'bob.smith123@domain.org'),
('Clara Lee', 'clara_lee99@xyz.co.uk'),
('David Brown', 'david.brown+test@gmail.com');

INSERT INTO public.ticket (movie_id, customer_id, session_date, seat, price) VALUES
(1, 1, '2025-03-13 18:30:00', 15, 12.50),
(2, 2, '2025-03-14 20:00:00', 8, 15.00),
(3, 3, '2025-03-15 19:15:00', 22, 13.75),
(4, 4, '2025-03-12 15:00:00', 5, 10.00),
(1, 2, '2025-03-13 18:30:00', 16, 12.50);
"""

# Создание директории и запись в файл
os.makedirs(os.path.dirname(output_file), exist_ok=True)
with open(output_file, "w") as file:
    file.write(sql_statements)

print(f"SQL script generated at {output_file}")
