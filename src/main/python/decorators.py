import time
from functools import wraps
import random

# 1. Simple Decorator Example
def decorator(func):
    def wrapper(*args, **kwargs):
        print("Before the function call")
        result = func(*args, **kwargs)
        print("After the function call")
        return result
    return wrapper

@decorator
def say_hello():
    print("Hello!")

# 2. @square Decorator Example
def square(func):
    def wrapper(*args, **kwargs):
        result = func(*args, **kwargs)
        return result ** 2
    return wrapper

@square
def get_number():
    return 5

# 3. Authentication Decorator Example
def require_auth(func):
    @wraps(func)
    def wrapper(*args, **kwargs):
        # Simulate a check for an authorization header
        if not hasattr(wrapper, 'is_authenticated') or not wrapper.is_authenticated:
            print("Unauthorized: No valid authentication provided.")
            return None
        return func(*args, **kwargs)
    return wrapper

@require_auth
def secure_data():
    return {"data": "This is secured data!"}

# Simulate setting authentication status
require_auth.is_authenticated = True

# 4. @retry Decorator Example
def retry(retries=3, delay=1):
    def decorator(func):
        def wrapper(*args, **kwargs):
            for attempt in range(retries):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    print(f"Attempt {attempt + 1} failed: {e}")
                    time.sleep(delay)
            raise Exception(f"Failed after {retries} attempts")
        return wrapper
    return decorator

@retry(retries=5, delay=2)
def test_function():
    if random.choice([True, False]):
        raise ValueError("Random failure occurred!")
    return "Success!"

# Main Execution
if __name__ == "__main__":
    print("-- Simple Decorator Example --")
    say_hello()

    print("\n-- @square Decorator Example --")
    print(get_number())

    print("\n-- Authentication Decorator Example --")
    print(secure_data())

    print("\n-- @retry Decorator Example --")
    try:
        print(test_function())
    except Exception as e:
        print(e)
