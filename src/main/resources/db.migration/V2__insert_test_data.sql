
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
(1, 1, '2025-03-13 18:30:00', 15, 12.50),  -- Alice buys a ticket for The Matrix
(2, 2, '2025-03-14 20:00:00', 8, 15.00),   -- Bob buys a ticket for Titanic
(3, 3, '2025-03-15 19:15:00', 22, 13.75),  -- Clara buys a ticket for Inception
(4, 4, '2025-03-12 15:00:00', 5, 10.00),   -- David buys a ticket for The Lion King
(1, 2, '2025-03-13 18:30:00', 16, 12.50);  -- Bob buys another ticket for The Matrix

