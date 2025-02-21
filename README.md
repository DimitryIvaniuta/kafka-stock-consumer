# Kafka KRaft + Spring Boot Kafka Consumer Microservice

This project demonstrates how to build a **Kafka Consumer Microservice** using **Spring Boot**, **Gradle**, and **Apache Kafka** in **KRaft mode** (without Zookeeper)

---

## 📋 Table of Contents

- [🚀 Project Overview](#-project-overview)
- [⚙️ Prerequisites](#️-prerequisites)
- [📁 Project Structure](#-project-structure)
- [💾 Installing Kafka (KRaft)](#-installing-kafka-kraft)
    - [1️⃣ Download and Install Kafka](#1%EF%B8%8F-download-and-install-kafka)
    - [2️⃣ Configure Kafka in KRaft Mode](#2%EF%B8%8F-configure-kafka-in-kraft-mode)
    - [3️⃣ Format Kafka Storage](#3%EF%B8%8F-format-kafka-storage)
    - [4️⃣ Start Kafka in KRaft Mode](#4%EF%B8%8F-start-kafka-in-kraft-mode)
- [⚡ Spring Boot Kafka Consumer Setup](#%EF%B8%8F-spring-boot-kafka-consumer-setup)
    - [5️⃣ Project Dependencies](#5%EF%B8%8F-project-dependencies)
    - [6️⃣ Kafka Consumer Configuration](#6%EF%B8%8F-kafka-consumer-configuration)
    - [7️⃣ Kafka Consumer Service](#7%EF%B8%8F-kafka-consumer-service)
- [🐳 Docker Support (Optional)](#-docker-support-optional)
- [🧪 Testing Kafka Consumer](#-testing-kafka-consumer)
- [📖 Useful Kafka Commands](#-useful-kafka-commands)
- [📚 References](#-references)

---

## 🚀 Project Overview

This microservice consumes messages from a **Kafka** topic using **Spring Boot** and **Apache Kafka**. It connects to a **KRaft-mode Kafka broker**, removing the need for **Zookeeper**.

---

## ⚙️ Prerequisites

- **Java 11+**
- **Gradle 7.x**
- **Apache Kafka 3.x+ (with KRaft)**
- **Git Bash / PowerShell**

---

## 📁 Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── KafkaConsumerApplication.java
│   │   │           └── service
│   │   │               └── KafkaConsumerService.java
│   │   └── resources
│   │       └── application.yml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 💾 Installing Kafka (KRaft)

### 1️⃣ Download and Install Kafka

1. **Download Kafka:**
    - Visit [Kafka Downloads](https://kafka.apache.org/downloads).
    - Download **kafka_2.13-3.x.x.tgz**.

2. **Extract Kafka:**
   ```bash
   tar -xvzf kafka_2.13-3.x.x.tgz
   mv kafka_2.13-3.x.x C:\kafka
   cd C:\kafka
   ```

### 2️⃣ Configure Kafka in KRaft Mode

1. Open `config\kraft\server.properties` and set:

   ```properties
   node.id=1
   process.roles=controller,broker
   controller.listener.names=CONTROLLER
   controller.quorum.voters=1@localhost:9093
   listeners=PLAINTEXT://:9092,CONTROLLER://:9093
   advertised.listeners=PLAINTEXT://localhost:9092
   log.dirs=C:\kafka\logs
   ```

### 3️⃣ Format Kafka Storage

Run the following in **PowerShell** or **Git Bash**:

```bash
bin\windows\kafka-storage.bat format -t (bin\windows\kafka-storage.bat random-uuid) -c config\kraft\server.properties
```

### 4️⃣ Start Kafka in KRaft Mode

```bash
bin\windows\kafka-server-start.bat config\kraft\server.properties
```

Kafka is now running without Zookeeper! 🎉

---

## ⚡ Spring Boot Kafka Consumer Setup

### 5️⃣ Project Dependencies

Add dependencies in `build.gradle.kts`:

```kotlin
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.kafka:spring-kafka")
}
```

Run:

```bash
./gradlew build
```

### 6️⃣ Kafka Consumer Configuration

In `application.yml`:

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: my-consumer-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
```

### 7️⃣ Kafka Consumer Service

**KafkaConsumerService.java:**

```java
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "my-topic", groupId = "my-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value());
    }
}
```

Run the Spring Boot app:

```bash
./gradlew bootRun
```

---

## 🐳 Docker Support (Optional)

**Dockerfile:**

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t my-kafka-consumer .
docker run --network="host" my-kafka-consumer
```

---

## 🧪 Testing Kafka Consumer

### ✅ Create Kafka Topic

```bash
bin\windows\kafka-topics.bat --create --topic my-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### ✅ Send a Test Message

```bash
bin\windows\kafka-console-producer.bat --bootstrap-server localhost:9092 --topic my-topic
```
Type:
```
Hello, KRaft!
```

### ✅ Verify Consumer Output

Your Spring Boot logs should show:

```
Received message: Hello, KRaft!
```

---

## 📖 Useful Kafka Commands

- **List Topics:**
  ```bash
  bin\windows\kafka-topics.bat --list --bootstrap-server localhost:9092
  ```

- **Describe Topic:**
  ```bash
  bin\windows\kafka-topics.bat --describe --topic my-topic --bootstrap-server localhost:9092
  ```

- **Consume Messages:**
  ```bash
  bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic my-topic --from-beginning
  ```

- **Delete Topic:**
  ```bash
  bin\windows\kafka-topics.bat --delete --topic my-topic --bootstrap-server localhost:9092
  ```

---

## 📚 References

- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/docs/current/reference/html/)
- [Gradle Documentation](https://docs.gradle.org/)

---

### 🎉 Enjoy building Kafka consumers without Zookeeper using **KRaft** and **Spring Boot**! 🚀

