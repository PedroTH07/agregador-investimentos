# Agregador de investimentos


## Sobre
Este projeto consiste em uma API de um agregador de investimentos feito a partir do vídeo do Bruno Rana. O projeto disponibiliza uma API onde se é possível checar ações reais a partir de uma requisição a uma API terceira, pode-se atribuir quantidades de ações para sua carteira de investimentos. Porém não há movimentação real de dinheiro, é apenas para fins educacionais


## 🛠️ Stack
- Java 21
- Spring Boot 4
- H2DataBase
- OpenFeign

## 🚀 Como rodar com Java
### 📦 Pré requisitos

- Java 21 (JDK ou JRE)
- ou docker

### 🛠️ Build
 
1. clone o repositório 
```bash
git clone https://github.com/PedroTH07/agregador-investimentos.git
cd agregador-investimentos
```

2. rode usando o maven ou o maven wrapper
```
./mvnw spring-boot:run
```

## 🐳 Rodar com Docker

### 🛠️ Build
 
1. clone o repositório 
```bash
git clone https://github.com/PedroTH07/agregador-investimentos.git
cd agregador-investimentos
```

2. Faça o build e rode a imagem docker
```
docker compose up --build -d
```