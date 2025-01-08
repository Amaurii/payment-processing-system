# -*- coding: utf-8 -*-

import requests
import threading
import time

# Configurações do teste
URL = "http://localhost:8080/api/payments"  # Substitua pelo endpoint correto
NUM_THREADS = 100  # Número de threads (simultaneidade)
NUM_REQUESTS_PER_THREAD = 10  # Número de requisições por thread
PAYLOAD = {
    "customerId": "12345",
    "amount": 100.0
}  # Corpo da requisição
HEADERS = {"Content-Type": "application/json"}

# Função para enviar requisições
def send_requests():
    for _ in range(NUM_REQUESTS_PER_THREAD):
        try:
            response = requests.post(URL, json=PAYLOAD, headers=HEADERS)
            print(f"Status Code: {response.status_code}, Response: {response.text}")
        except Exception as e:
            print(f"Erro: {e}")

# Início do teste
if __name__ == "__main__":
    threads = []
    start_time = time.time()

    # Criar e iniciar threads
    for _ in range(NUM_THREADS):
        thread = threading.Thread(target=send_requests)
        threads.append(thread)
        thread.start()

    # Esperar todas as threads terminarem
    for thread in threads:
        thread.join()

    end_time = time.time()
    print(f"Teste concluído em {end_time - start_time:.2f} segundos.")
