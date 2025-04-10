import argparse
import requests
from faker import Faker
import json
from urllib.parse import urljoin
from datetime import datetime
import random

BASE_URL = "http://localhost:8080/api/v1/"  # Adjust to your API base URL

fake = Faker()

def clear_all_data():
    """Clear all data with cascade deletion starting from dependent tables"""
    endpoints = ["ticket", "customer", "movie"]  # Order respects dependencies
    for endpoint in endpoints:
        url = urljoin(BASE_URL, f"{endpoint}/clear")
        try:
            response = requests.post(url)
            response.raise_for_status()
            print(f"Successfully cleared {endpoint}")
        except requests.exceptions.RequestException as e:
            print(f"Error clearing {endpoint}: {e}")

def generate_movie():
    """Generate movie data"""
    return {
        "name": fake.catch_phrase(),
        "genre": fake.random_element(elements=("Action", "Comedy", "Drama", "Horror", "Sci-Fi")),
        "duration": round(fake.random_number(digits=2) + fake.random.random(), 1)
    }

def generate_customer():
    """Generate customer data"""
    return {
        "name": fake.name(),
        "email": fake.email()
    }

def generate_ticket(movie_ids, customer_ids):
    """Generate ticket data"""
    session_date = fake.date_time_this_year().replace(microsecond=0).isoformat()
    return {
        "movie": fake.random_element(elements=movie_ids),
        "customer": fake.random_element(elements=customer_ids),
        "sessionDate": session_date,  # e.g., "2025-04-09T14:30:45"
        "seat": fake.random_int(min=1, max=100),
        "price": round(fake.random_number(digits=2) + fake.random.random(), 2)
    }

def populate_endpoint(endpoint, count, clear=False):
    """Populate endpoint with test data"""
    url = urljoin(BASE_URL, endpoint)

    if clear:
        clear_all_data()

    if endpoint == "movie":
        for _ in range(count):
            movie_data = generate_movie()
            try:
                response = requests.post(url, json=movie_data)
                response.raise_for_status()
                print(f"Created movie: {movie_data['name']}")
            except requests.exceptions.RequestException as e:
                print(f"Error creating movie: {e}")

    elif endpoint == "customer":
        for _ in range(count):
            customer_data = generate_customer()
            try:
                response = requests.post(url, json=customer_data)
                response.raise_for_status()
                print(f"Created customer: {customer_data['name']}")
            except requests.exceptions.RequestException as e:
                print(f"Error creating customer: {e}")

    elif endpoint == "ticket":

        # Randomly determine movie and customer counts
        # At least 1, up to count/5 or count, whichever is smaller
        movie_count = random.randint(1, count // 5)
        customer_count = random.randint(1, count // 5)

        # Populate movies
        movies_url = urljoin(BASE_URL, "movie")
        movie_ids = []
        for _ in range(movie_count):
            movie_data = generate_movie()
            try:
                response = requests.post(movies_url, json=movie_data)
                response.raise_for_status()
                movie_ids.append(response.json()["id"])
#                 print(f"Created movie for tickets: {movie_data['name']} (Total movies: {len(movie_ids)})")
            except requests.exceptions.RequestException as e:
                print(f"Error creating movie: {e}")

        # Populate customers
        customers_url = urljoin(BASE_URL, "customer")
        customer_ids = []
        for _ in range(customer_count):
            customer_data = generate_customer()
            try:
                response = requests.post(customers_url, json=customer_data)
                response.raise_for_status()
                customer_ids.append(response.json()["id"])
#                 print(f"Created customer for tickets: {customer_data['name']} (Total customers: {len(customer_ids)})")
            except requests.exceptions.RequestException as e:
                print(f"Error creating customer: {e}")

        if not movie_ids or not customer_ids:
            print("Failed to create movies or customers. Aborting ticket population.")
            return

        # Populate tickets with debugging
        for _ in range(count):
            ticket_data = generate_ticket(movie_ids, customer_ids)
            try:
                response = requests.post(url, json=ticket_data)
                response.raise_for_status()
#                 print(f"Created ticket for movie ID: {ticket_data['movie']}")
            except requests.exceptions.RequestException as e:
                print(f"Error creating ticket: {e}")

def main():
    parser = argparse.ArgumentParser(
        description="Populate movie ticketing REST service with test data"
    )
    parser.add_argument(
        "--count",
        type=int,
        default=500,
        help="Number of objects to create (default: 500)"
    )
    parser.add_argument(
        "--endpoint",
        type=str,
        required=True,
        choices=["movie", "customer", "ticket"],
        help="API endpoint to populate (movie, customer, or ticket)"
    )
    parser.add_argument(
        "--clear",
        action="store_true",
        help="Clear all data before population"
    )

    args = parser.parse_args()

    print(f"Populating {args.endpoint} with {args.count} objects...")
    populate_endpoint(args.endpoint, args.count, args.clear)
    print("Population completed!")

if __name__ == "__main__":
    main()