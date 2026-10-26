# SmartHire — AI-Powered Job Portal & Applicant Tracking System (ATS)

[![Java 21](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot 3.2.5](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React 18](https://img.shields.io/badge/React-18-blue.svg)](https://react.dev/)
[![MySQL 8.0](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**SmartHire** is an enterprise-grade recruitment and applicant tracking platform designed to eliminate hiring friction. It features automated resume parsing (PDF/Word), deterministic multi-factor match scoring, a resilient multi-tier LLM router (OpenAI GPT-4o mini, Google Gemini 1.5 Flash, and in-house rule-based fallback), interview kit generation, online interview scheduling, and role-based access control.

---

## 🚀 Key Features

### 👤 1. Job Seeker Experience
* **Smart Profile & Resumes:** Upload multiple resumes in PDF, DOC, DOCX, or TXT formats; designate a primary resume for instant applications.
* **Intelligent Job Search:** Filter active opportunities by keyword, work location, and job type (Remote, Hybrid, Full-Time).
* **1-Click Applications:** Apply instantly with tailored cover letters and track application stages (`APPLIED` → `UNDER_REVIEW` → `SHORTLISTED` → `INTERVIEW_SCHEDULED` → `HIRED` / `REJECTED`).
* **Interview Dashboard:** View upcoming technical rounds with meeting links, scheduled times, and interviewer notes.
* **In-App Notifications:** Real-time notification center tracking status changes and interview confirmations.

### 👔 2. Recruiter ATS Dashboard
* **Job Posting Lifecycle:** Create, edit, and deactivate job listings with detailed salary bands, required skills, and deadlines.
* **AI Candidate Leaderboard:** Applicants are automatically ranked by an overall match percentage.
* **Deep-Dive Assessment:** View granular score breakdowns across **Skills (35%)**, **Experience (25%)**, **JD Relevance (20%)**, and **Education (20%)**, alongside explicit matched vs. missing skills.
* **AI Interview Kit Generator:** Generates role-specific technical, system design, and behavioral questions with answer criteria and hints.
* **Interview Scheduling:** Schedule Google Meet or Zoom rounds with automated candidate notifications.

### 🛡️ 3. Admin Governance Panel
* **System Metrics:** Real-time platform counts across total users, candidates, recruiters, companies, active jobs, and AI evaluations.
* **User Management:** Audit and activate or suspend accounts.
* **Company & Job Audits:** Approve/deactivate companies and moderate job postings across the ecosystem.

---

## 🧠 AI Engine & Scoring Architecture

```
                    ┌──────────────────────────────┐
                    │    Resume Upload (PDF/DOCX)  │
                    └──────────────┬───────────────┘
                                   │
                                   ▼
                    ┌──────────────────────────────┐
                    │   Apache Tika Text Parser    │
                    │  & Regex Experience Matcher  │
                    └──────────────┬───────────────┘
                                   │
                                   ▼
        ┌────────────────────────────────────────────────────────┐
        │        Deterministic Multi-Factor Scoring Engine       │
        │  • Skills Match (35%)     • Experience Score (25%)    │
        │  • JD Relevance (20%)     • Education Score (20%)     │
        └──────────────────────────┬─────────────────────────────┘
                                   │
                                   ▼
        ┌────────────────────────────────────────────────────────┐
        │            Resilient Generative AI Router              │
        │   [OpenAI GPT-4o mini] ──> [Google Gemini 1.5 Flash]  │
        │                      └──> [In-House Fallback Engine]   │
        └────────────────────────────────────────────────────────┘
```

1. **Document Ingestion:** Handled by **Apache Tika 2.9.2**, extracting raw text streams from arbitrary document layouts.
2. **Deterministic Mathematical Scoring:**
   $$\text{Overall Score} = (\text{Skills} \times 0.35) + (\text{Experience} \times 0.25) + (\text{JD Relevance} \times 0.20) + (\text{Education} \times 0.20)$$
   * **Skills Score (35%):** Normalized comparison between job requirements and extracted resume/profile competencies.
   * **Experience Score (25%):** Evaluates parsed years of experience against role requirements.
   * **JD Relevance (20%):** Keyword density analysis across job title, description, and responsibilities.
   * **Education Score (20%):** Evaluates academic degrees and computer science qualifications.
3. **Multi-Tier Fault-Tolerant LLM Router:**
   * **Tier 1:** OpenAI `gpt-4o-mini` with structured JSON output.
   * **Tier 2:** Google Gemini `gemini-1.5-flash` via Google Generative AI REST API.
   * **Tier 3:** Deterministic In-House Fallback ensuring **100% uptime** during external API outages or rate limits.

---

## 🛠️ Technology Stack

| Layer | Technologies |
|---|---|
| **Backend** | Java 21, Spring Boot 3.2.5, Spring Security 6, Spring Data JPA, Hibernate, JJWT 0.12.5, Apache Tika 2.9.2, Lombok |
| **Frontend** | React 18, Vite 5, React Router DOM 6, Axios, Lucide React, CSS3 Design Tokens |
| **Databases** | MySQL 8.0 (Production / Docker), H2 Database (Local Dev) |
| **DevOps** | Docker, Docker Compose, Git |
| **API Documentation** | Springdoc OpenAPI 3.0 / Swagger UI |

---

## 🔐 Security & Architecture Highlights

* **Stateless Authentication:** Stateless JWT signed via HMAC-SHA256 (`HS256`) with 24-hour expiration.
* **Role-Based Access Control (RBAC):** Strict endpoint segregation (`JOB_SEEKER`, `RECRUITER`, `ADMIN`) via Spring Security filter chains and `@PreAuthorize`.
* **Password Encryption:** Passwords hashed with **BCrypt** with unique cryptographic salts.
* **Secure Document Storage:** Uploaded files stored with UUID-prefixed sanitization to prevent filename collisions and path traversal attacks.
* **Global Error Handling:** Centralized `@RestControllerAdvice` mapping validation constraints and system errors to standard JSON payloads without leaking internal stack traces.

---

## 🔑 Pre-Seeded Demo Accounts

The project includes an automatic database seeder (`DataInitializer.java`) with top Indian and global tech firms (Google, Microsoft, Amazon, Razorpay, Flipkart, Swiggy, Zomato, CRED, Uber, PhonePe, Zerodha):

| Role | Email | Password | Access Highlights |
|---|---|---|---|
| **System Admin** | `admin@smarthire.ai` | `password123` | Global analytics, user management, company auditing, job lifecycle. |
| **Job Seeker** | `candidate@smarthire.ai` | `password123` | Pre-configured profile, job browsing, 1-click apply, application tracking. |
| **Recruiter (Razorpay)** | `recruiter@razorpay.com` | `password123` | Ranked candidates, AI scoring breakdown, interview kit generator. |
| **Recruiter (Google)** | `recruiter@google.com` | `password123` | Staff ML/AI role management. |

---

## ⚡ Quick Start

### Option 1: Run with Docker Compose (Recommended)

```bash
# 1. Clone repository
git clone https://github.com/chirag-git-account/smart-hire.git
cd smart-hire

# 2. Build and launch all services
docker-compose up --build
```

* **Frontend:** [http://localhost:5173](http://localhost:5173)
* **Backend API:** [http://localhost:8080/api](http://localhost:8080/api)
* **Swagger UI:** [http://localhost:8080/api/swagger-ui/index.html](http://localhost:8080/api/swagger-ui/index.html)

---

### Option 2: Run Locally (Without Docker)

#### Prerequisites
* Java 21 JDK
* Node.js v18+ & npm

#### 1. Backend Setup
```bash
cd smart-hire-backend
./mvnw clean spring-boot:run
```
*(Runs with embedded H2 file database in MySQL mode automatically)*

#### 2. Frontend Setup
```bash
cd smart-hire-frontend
npm install
npm run dev
```

---

## 📄 License
This project is licensed under the MIT License.
