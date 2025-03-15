import os
import random
from datetime import datetime, timedelta

# Configuration: number of records to generate
num_movies = 10
num_customers = 20
num_tickets = 50

# Output SQL file path
output_file = "src/main/resources/data.sql"

# Predefined movie genres
genres = ["Action", "Comedy", "Drama", "Horror", "Sci-Fi", "Thriller", "Animation"]

# Generate movies
movies = [(i + 1, f"Movie {i + 1}", random.choice(genres), round(random.uniform(80, 180), 1)) for i in range(num_movies)]

# Generate customers
customers = [(i + 1, f"Customer {i + 1}", f"customer{i+1}@example.com") for i in range(num_customers)]

# Generate tickets
tickets = []
for i in range(num_tickets):
    movie_id = random.randint(1, num_movies)
    customer_id = random.randint(1, num_customers)
    session_date = datetime.now() + timedelta(days=random.randint(-10, 10))
    seat = random.randint(1, 100)
    price = round(random.uniform(8, 20), 2)
    tickets.append((i + 1, movie_id, customer_id, session_date.strftime('%Y-%m-%d %H:%M:%S'), seat, price))

# SQL script
table_definitions = """
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
"""

insert_movies = "INSERT INTO public.movie (id, name, genre, duration) VALUES\n" + ",\n".join(
    [f"({id}, '{name}', '{genre}', {duration})" for id, name, genre, duration in movies]
) + ";\n"

insert_customers = "INSERT INTO public.customer (id, name, email) VALUES\n" + ",\n".join(
    [f"({id}, '{name}', '{email}')" for id, name, email in customers]
) + ";\n"

insert_tickets = "INSERT INTO public.ticket (id, movie_id, customer_id, session_date, seat, price) VALUES\n" + ",\n".join(
    [f"({id}, {movie_id}, {customer_id}, '{session_date}', {seat}, {price})" for id, movie_id, customer_id, session_date, seat, price in tickets]
) + ";\n"

# Write to file
os.makedirs(os.path.dirname(output_file), exist_ok=True)
with open(output_file, "w") as file:
    file.write(table_definitions + "\n" + insert_movies + "\n" + insert_customers + "\n" + insert_tickets)

print(f"SQL script generated at {output_file}")
