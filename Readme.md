# 🧠 MetaData Server for Distributed Databases

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-blue?logo=java" />
  <img src="https://img.shields.io/badge/Redis-6.x+-red?logo=redis" />
  <img src="https://img.shields.io/badge/Consistent-Hashing-orange" />
  <img src="https://img.shields.io/badge/LoadBalancer-Server-Aware-green" />
</p>

<p align="center">
  <b>⚡ A simple distributed load balancer with consistent hashing and Redis-powered persistence for smart user-server mapping.</b><br>
  <i>Mimics distributed system routing, supports server updates without full remapping.</i>
</p>

---

## 🏗️ Project Structure

```text
loadbalancer/
├── src/                  # Java source files
│   ├── Main.java         # Entry point
│   ├── Router.java       # Routes HTTP requests
│   ├── Server.java       # TCP server logic
│   ├── ServerHandler.java# /addServer, /replaceServer endpoints
│   ├── DataHandler.java  # /registerUser, /cachereport endpoints
│   └── Redis.java        # Redis client setup via Redisson
├── Tester.sh             # Simulate server/user operations
├── request_test.sh       # Basic CURL testing
├── build.sh              # Compile + run helper
├── client_requests.log   # Logs requests
├── pom.xml               # Maven config
└── README.md             # This file
```

---

## ⚙️ Prerequisites

- Java 17+
- Maven
- Redis (local or Docker)
- Internet (to download dependencies)

---

## 🚀 Setup & Run Instructions

### 🔴 Step 1: Start Redis Locally

You can install Redis or use Docker:

**Option A: System Install**

```bash
sudo apt update
sudo apt install redis-server
redis-server
```

**Option B: Using Docker**

```bash
docker run -d -p 6379:6379 --name redis-server redis
```

### 🔨 Step 2: Compile & Run the Server

```bash
chmod +x build.sh
./build.sh
# Server starts on localhost:4000
```

### 📦 Step 3: Simulate Requests

```bash
chmod +x Tester.sh
./Tester.sh
# Adds 3 servers, registers users, replaces a server, prints Redis cache reports, saves logs to report-x.txt
```

---

## 🔁 Redis Data Stored

- **ServerCache**: HashMap of `<userHash, ServerNode>`
- **ServerList**: Redis list of all active servers (for recovery after restart)

---

## 📥 API Endpoints

| Method | Endpoint         | Description                      |
|--------|------------------|----------------------------------|
| POST   | /addServer       | Add a new backend server         |
| POST   | /replaceServer   | Replace an existing server       |
| POST   | /registerUser    | Assign user to a server          |
| GET    | /cachereport     | Get all user-to-server mappings  |

> Use `userId` in header when calling `/registerUser`

---

## 🧠 Example CURL Commands

```bash
# Add a server
curl -X POST http://localhost:4000/addServer -H "Content-Type: text/plain" \
     -d "127.0.0.1:3001,Server1,user1,pass1"

# Register a user
curl -X POST http://localhost:4000/registerUser -H "userId: 101" -d ""

# View current cache report
curl http://localhost:4000/cachereport
```

---

## 💾 Persistence Strategy

- 🗃️ Redis stores all mappings persistently.
- On server restart, the consistent hashing ring is rehydrated from Redis `ServerList`.
- Ensures user mappings remain stable across server crashes and restarts.

---

## 🧪 Testing & Debugging

- Logs are printed to the console
- Each test run using `Tester.sh` generates a `report-x.txt` under the root directory
- Use `redis-cli` to view actual Redis data:

```bash
redis-cli
> HGETALL ServerCache
> LRANGE ServerList 0 -1
```

---

## 🧰 Useful Tools

- [RedisInsight](https://redis.com/redis-enterprise/redis-insight/) (GUI for Redis)
- [Postman](https://www.postman.com/) or [HTTPie](https://httpie.io/) for API testing

---

## 🧠 Learning Concepts

- ✅ Consistent Hashing
- ✅ Fault-tolerant Redis mapping
- ✅ Server-aware routing
- ✅ Persistent recovery without database

---

## 📜 License

MIT – Feel free to build on this foundation.

---

## ✨ Author

**Prem Sai** – Backend & Distributed Systems Enthusiast  
💡 For any questions, improvements or collabs – feel free to reach out!


