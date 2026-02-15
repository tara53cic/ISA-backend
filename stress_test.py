import threading
import requests
import time

URL = "http://localhost:8082/api/videos"
BEARER_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzcHJpbmctc2VjdXJpdHktZXhhbXBsZSIsInN1YiI6InRhcmExMjMiLCJhdWQiOiJ3ZWIiLCJpYXQiOjE3NzExNTY3NjgsImV4cCI6MTc3MTE1ODU2OH0.hCXWF3QessxU2dxNs2FVnqWhyH8DpMPKC5st5DsPgMgrspFk8t1znVSL6v_b_lDZq3R9XMorlwQj0IFIYDXxSw" 

TOTAL_REQUESTS = 2000  
CONCURRENT_THREADS = 50 

HEADERS = {
    "Authorization": f"Bearer {BEARER_TOKEN}",
    "Accept": "application/json"
}

def send_request():
    try:

        response = requests.get(URL, headers=HEADERS, timeout=5)
        print(f"Status: {response.status_code}")
    except Exception as e:
        print(f"Request failed: {e}")

def run_test():
    print(f"Starting stress test on {URL} with Authorization...")
    start_time = time.time()
    
    threads = []
    for i in range(TOTAL_REQUESTS):
        t = threading.Thread(target=send_request)
        threads.append(t)
        t.start()

        time.sleep(0.005) 

    for t in threads:
        t.join()

    end_time = time.time()
    print(f"Test complete. Sent {TOTAL_REQUESTS} requests in {end_time - start_time:.2f} seconds.")

if __name__ == "__main__":
    run_test()