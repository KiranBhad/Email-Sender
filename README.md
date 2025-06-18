# 📧 Email Sender Application

A full-stack web application that lets users send rich-format emails. The frontend is built with **React.js** and **TinyMCE Editor**, while the backend uses **Java Spring Boot** for handling email delivery.

---

## 🧩 Features

- ✅ Send emails with **To**, **Subject**, and **Rich Message Body**
- 📝 Use a rich text editor (**TinyMCE**) for formatting
- 🌙 Light/Dark mode toggle
- 🔔 Toast notifications using `react-hot-toast`
- 📤 Email sending via backend (JavaMailSender in Spring Boot)

---

## 📁 Project Structure

email-sender-app/
├── email-sender-backend/ # Java Spring Boot project
└── email-sender-frontend/ # React.js frontend with TinyMCE editor


---

## 🚀 Getting Started

### 🔧 Prerequisites

- Node.js and npm
- Java 17+
- Maven or Gradle
- Git

---

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/<your-username>/email-sender-app.git
cd email-sender-app
```

2️⃣ Setup Backend (Spring Boot)
```
cd email-sender-backend
# Use your IDE (IntelliJ, Eclipse, VS Code) or run:
./mvnw spring-boot:run
```
3️⃣ Setup Frontend (React)
```
cd ../email-sender-frontend
npm install
npm run dev
```
⚙️ Spring Boot Email Configuration
In src/main/resources/application.properties:
```
spring.mail.host=smtp.your-email-provider.com
spring.mail.port=587
spring.mail.username=your@email.com
spring.mail.password=your-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

👨‍💻 Author
Made with ❤️ by Kiran Bhad
📧 bhadkiran1804@gmail.com

