# ⚔️ Sword URL Shortener  

![React](https://img.shields.io/badge/React-19-61dafb?logo=react)  
![Vite](https://img.shields.io/badge/Vite-Bundler-646cff?logo=vite)  
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)  
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=springboot)  
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?logo=mysql)  
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message%20Broker-ff6600?logo=rabbitmq)  
![JWT](https://img.shields.io/badge/JWT-Security-purple?logo=jsonwebtokens)  
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)  

Sword [(swrd.shop)](**swrd.shop**) is a **full-stack URL shortener** built with a modern React + Vite frontend and a Spring Boot backend designed with microservices.  
It provides secure login/registration, short link generation, redirection, and click analytics powered by **RabbitMQ**.  

---

## Live Demo
- [swrd.shop](https://swrd.shop)
- [visionary-gingersnap-faaed6.netlify.app](https://visionary-gingersnap-faaed6.netlify.app/)
---

## 🚀 Features  

### 🌐 Frontend (React + Vite)
- Landing page, About page, Login & Registration
- Create and manage short URLs  
- View analytics (click counts, trends)  
- Responsive design with gradients, hover effects, and animations  

### ⚙️ Backend (Spring Boot Microservices)  
- **Auth Service** → Login & registration with JWT security  
- **Redirect Service** → Handles short URL redirection, publishes click events to RabbitMQ  
- **URL Service** → Creates short links, lists user URLs, processes analytics (consumes from RabbitMQ)  

---

## 🏗️ Tech Stack  

- **Frontend:** React 19, Vite, TailwindCSS, Axios, Netlify hosting  
- **Backend:** Java 25, Spring Boot 3.x, MySQL/PostgreSQL, RabbitMQ, JWT, Render hosting  
- **DevOps:** Docker-ready for containerized deployment  

---

## 📂 Microservice Structure  

- **Auth Service** – Authentication & user management  
- **Redirect Service** – Short URL redirection & event publishing  
- **URL Service** – Shortening, listing, and analytics via RabbitMQ consumer  

Each service is an independent Spring Boot application.  

---

## 🔧 Setup & Installation  

### Frontend
[GitHub source code](https://github.com/bhandariprerak/url-shortener-frontend)
```bash
git clone git@github.com:bhandariprerak/url-shortener-frontend.git
npm install
npm run dev
```
### Backend (Monolith)
[GitHub source code](https://github.com/bhandariprerak/url-shortener-sb)

```bash
git clone git@github.com:bhandariprerak/url-shortener-sb.git
mvn spring-boot:run
```
### Microservice Backend (per service)
#### Auth Service
[GitHub source code](https://github.com/bhandariprerak/url-shortener-sb)
```bash
git clone git@github.com:bhandariprerak/url-shortener-sb.git
mvn spring-boot:run
```

#### Redirect Service
[GitHub source code](https://github.com/bhandariprerak/redirect-service)
```bash
git clone git@github.com:bhandariprerak/redirect-service.git
mvn spring-boot:run
```

#### URL Service
[GitHub source code](https://github.com/bhandariprerak/url-service)
```bash
git clone git@github.com:bhandariprerak/url-service.git
mvn spring-boot:run
```

Configure environment variables in `.env` or `application.properties`:  
- Database credentials  
- JWT secret & expiration
---

## 🌍 Deployment  
- **Docker** →
```bash
./mvnw package
docker build --platform linux/amd64 -t url-shortener-backend .
docker tag url-shortener-backend <docker-username>/url-shortener-backend:latest
docker push <docker-username>/url-shortener-backend:latest
```
- **Frontend** → Netlify with custom domain **swrd.shop**  
- **Backend** → Render free tier (⚠️ cold start delay up to 50s) (Upload the docker image directly)
- **Database** → Neon PostgreSQL for production.
---

## 🔮 Future Enhancements  

- Richer analytics (geolocation, device)
- Refresh tokens & OAuth integrations  
- Horizontal scaling with load balancers  
- Delete/Update a short URL
- Custom Slug
- Bulk processing of CSV file containing long URLs
---

## 🤝 Connect with Me  

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?logo=linkedin)](https://www.linkedin.com/in/prerak-bhandari)  
[![GitHub](https://img.shields.io/badge/GitHub-Follow-black?logo=github)](https://github.com/bhandariprerak)  