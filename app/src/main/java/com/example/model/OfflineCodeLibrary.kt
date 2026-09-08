package com.example.model

object OfflineCodeLibrary {
  val snippets: List<CodeLibraryItem> = listOf(
    CodeLibraryItem(
      id = "py_rest_api",
      title = "Python REST API Client",
      category = "Backend & Networking",
      language = "Python",
      description = "Production-grade HTTP client with timeout, retry, and JSON parsing in Python.",
      complexity = "O(1) Network IO",
      tags = listOf("Python", "API", "Requests", "JSON"),
      code = """
import requests

def get_crypto_price(coin_id: str = "bitcoin"):
    url = f"https://api.coingecko.com/api/v3/simple/price?ids={coin_id}&vs_currencies=usd"
    try:
        res = requests.get(url, timeout=5)
        res.raise_for_status()
        price = res.json()[coin_id]['usd']
        print(f"Current {coin_id.capitalize()} price: " + str(price))
        return price
    except Exception as err:
        print(f"API Error: {err}")
        return None

if __name__ == "__main__":
    get_crypto_price("bitcoin")
      """.trimIndent()
    ),
    CodeLibraryItem(
      id = "kt_compose_card",
      title = "Jetpack Compose Glass Card",
      category = "Android UI",
      language = "Kotlin",
      description = "Modern Material 3 card with animated hover ripple, rounded corners, and elevated typography.",
      complexity = "O(1) Layout",
      tags = listOf("Kotlin", "Jetpack Compose", "Android", "M3"),
      code = """
@Composable
fun InteractiveGlassCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
      """.trimIndent()
    ),
    CodeLibraryItem(
      id = "cpp_dijkstra",
      title = "C++ Shortest Path (Dijkstra)",
      category = "Algorithms",
      language = "C++",
      description = "Dijkstra's shortest path algorithm using priority_queue min-heap in C++.",
      complexity = "O((V + E) log V)",
      tags = listOf("C++", "Graph", "Shortest Path", "Heap"),
      code = """
#include <iostream>
#include <vector>
#include <queue>

using namespace std;

typedef pair<int, int> pii; // {weight, destination}

vector<int> dijkstra(int n, vector<vector<pii>>& adj, int src) {
    vector<int> dist(n, 1e9);
    priority_queue<pii, vector<pii>, greater<pii>> pq;

    dist[src] = 0;
    pq.push({0, src});

    while (!pq.empty()) {
        auto [d, u] = pq.top();
        pq.pop();

        if (d > dist[u]) continue;

        for (auto& edge : adj[u]) {
            int v = edge.second;
            int weight = edge.first;

            if (dist[u] + weight < dist[v]) {
                dist[v] = dist[u] + weight;
                pq.push({dist[v], v});
            }
        }
    }
    return dist;
}
      """.trimIndent()
    ),
    CodeLibraryItem(
      id = "js_async_debounce",
      title = "JavaScript Async Debounce",
      category = "Web Development",
      language = "JavaScript",
      description = "High-performance debounce wrapper with cancellation for search inputs.",
      complexity = "O(1) Runtime",
      tags = listOf("JavaScript", "ES6", "Debounce", "Frontend"),
      code = """
function debounce(func, delay = 300) {
  let timer;
  return function (...args) {
    const context = this;
    clearTimeout(timer);
    timer = setTimeout(() => {
      func.apply(context, args);
    }, delay);
  };
}

// Usage Example
const onSearch = debounce((query) => {
  console.log("Executing search for: " + query);
  // fetchSearchResults(query);
}, 400);
      """.trimIndent()
    ),
    CodeLibraryItem(
      id = "sql_window_func",
      title = "SQL Running Total & Ranking",
      category = "Database",
      language = "SQL",
      description = "Window functions using OVER(), PARTITION BY, and RANK() for analytical queries.",
      complexity = "O(N log N) Sort",
      tags = listOf("SQL", "Postgres", "Analytics", "Database"),
      code = """
-- Calculate running sales revenue and department salary rank
SELECT 
    employee_id,
    department_id,
    salary,
    RANK() OVER (
        PARTITION BY department_id 
        ORDER BY salary DESC
    ) as dept_salary_rank,
    SUM(salary) OVER (
        PARTITION BY department_id 
        ORDER BY hire_date 
        ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
    ) as cumulative_payroll
FROM employees;
      """.trimIndent()
    ),
    CodeLibraryItem(
      id = "py_fastapi_crud",
      title = "Python FastAPI Microservice",
      category = "Backend & Cloud",
      language = "Python",
      description = "Lightweight asynchronous REST API with Pydantic validation and Swagger UI.",
      complexity = "O(1) Asynchronous",
      tags = listOf("Python", "FastAPI", "Async", "REST"),
      code = """
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI(title="CHRONO AI Microservice", version="1.0")

class Item(BaseModel):
    name: str
    price: float
    is_active: bool = True

db = {}

@app.post("/items/{item_id}")
async def create_item(item_id: int, item: Item):
    db[item_id] = item
    return {"message": "Item stored", "data": item}

@app.get("/items/{item_id}")
async def get_item(item_id: int):
    if item_id not in db:
        raise HTTPException(status_code=404, detail="Item not found")
    return db[item_id]
      """.trimIndent()
    )
  )
}
