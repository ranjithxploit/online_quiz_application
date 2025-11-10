# Online Quiz Application - Setup & Run Guide

## ✅ What's Been Done

1. **Upgraded to Java 21 LTS** - Project now targets Java 21 (latest LTS version)
2. **Created Main Application** - Added `MainApplication.java` entry point
3. **Configured Maven Shade Plugin** - Builds a "fat JAR" with all dependencies included
4. **Verified Build & Run** - Successfully tested the application

## 📋 Prerequisites

### 1. Install JDK 21 (Latest LTS)

**Option A: Eclipse Adoptium (Recommended)**
- Download: https://adoptium.net/temurin/releases/?version=21
- Install the `.msi` for Windows
- Note the installation path (e.g., `C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot`)

**Option B: Microsoft Build of OpenJDK**
- Download: https://learn.microsoft.com/java/openjdk/download#openjdk-21

**Set JAVA_HOME:**
```powershell
# Temporary (current session only):
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# Verify:
java -version
# Should show: openjdk version "21.0.x"
```

**Permanent (system-wide):**
- Open System Properties → Environment Variables
- Add `JAVA_HOME` = `C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot`
- Add `%JAVA_HOME%\bin` to your `Path` variable

### 2. Maven is Already Installed

Maven was installed during setup at: `C:\Users\cyber\apache-maven-3.9.9`

**Add to PATH (if not already done):**
```powershell
# Temporary:
$env:Path = "$env:USERPROFILE\apache-maven-3.9.9\bin;$env:Path"

# Verify:
mvn -v
```

**Make it permanent:**
- Add `C:\Users\cyber\apache-maven-3.9.9\bin` to your system PATH

## 🚀 How to Build & Run

### Quick Start

```powershell
# 1. Navigate to project directory
cd D:\projects\ooad\online-quiz-app

# 2. Build the project (creates JAR with all dependencies)
mvn clean package -DskipTests

# 3. Run the application
java -jar target\online-quiz-app-1.0-SNAPSHOT.jar
```

### Expected Output

```
==============================================
  Online Quiz Application - Starting...      
==============================================
✓ Configuration loaded successfully
✓ Database connection established

==============================================
  Application started successfully!          
==============================================
```

## 📂 Project Structure

```
online-quiz-app/
├── src/main/java/com/quiz/
│   ├── MainApplication.java          ← Entry point (NEW)
│   ├── models/                        ← Data models
│   │   ├── Admin.java
│   │   ├── Attempt.java
│   │   ├── Option.java
│   │   ├── Quiz.java
│   │   ├── QuizQuestion.java
│   │   ├── Response.java
│   │   ├── Student.java
│   │   └── User.java
│   ├── repository/                    ← Database access (empty)
│   ├── services/                      ← Business logic (empty)
│   ├── ui/                            ← User interface (empty)
│   └── utils/
│       ├── ConfigLoader.java         ← Loads .env configuration
│       ├── DatabaseConnection.java   ← MongoDB connection
│       ├── GeminiAPIClient.java      ← AI integration
│       └── PasswordUtil.java         ← Password hashing
├── .env                               ← Configuration (DO NOT COMMIT!)
├── .gitignore                         ← Git ignore rules
├── pom.xml                            ← Maven configuration (Java 21)
└── target/
    └── online-quiz-app-1.0-SNAPSHOT.jar  ← Runnable JAR
```

## 🔧 Common Maven Commands

```powershell
# Build without tests (faster)
mvn clean package -DskipTests

# Build with tests
mvn clean package

# Just compile (no JAR)
mvn compile

# Clean build artifacts
mvn clean

# Run tests only
mvn test

# Install to local Maven repository
mvn install
```

## 🛠️ Development Workflow

### Current State
The application currently:
- ✅ Loads configuration from `.env`
- ✅ Connects to MongoDB Atlas
- ✅ Prints startup messages
- ⚠️ **TODO**: Implement actual quiz functionality

### Next Steps for Development

1. **Implement Repository Layer** (`repository/` package)
   - Create data access classes for CRUD operations
   - Example: `QuizRepository.java`, `StudentRepository.java`

2. **Implement Service Layer** (`services/` package)
   - Add business logic
   - Example: `QuizService.java`, `AuthenticationService.java`

3. **Implement UI** (`ui/` package)
   - Command-line interface or GUI
   - User interaction flows (login, take quiz, view results)

4. **Update MainApplication.java**
   - Initialize services
   - Start the UI
   - Handle application lifecycle

## 🔒 Security Notes

### ⚠️ Important: Your `.env` file contains sensitive credentials!

**Current credentials in `.env`:**
- MongoDB connection string with username/password
- Gemini API key
- Admin credentials

**Security checklist:**
- ✅ `.env` is already in `.gitignore` (protected from Git commits)
- ⚠️ **DO NOT** commit `.env` to version control
- ⚠️ Rotate API keys if this repository was ever public
- 💡 Use environment variables in production (not `.env` file)

**For team collaboration:**
Create `.env.example` (safe to commit):
```properties
# MongoDB Atlas Configuration
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/
DATABASE_NAME=online_quiz_db

# Gemini API Configuration
GEMINI_API_KEY=your_api_key_here

# Default Admin Credentials
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

## 📝 Configuration

The application uses `.env` for configuration:

```properties
# MongoDB Atlas (cloud database)
MONGODB_URI=mongodb+srv://...
DATABASE_NAME=online_quiz_db

# Google Gemini AI API
GEMINI_API_KEY=AIza...

# Admin account
ADMIN_USERNAME=admin
ADMIN_PASSWORD=admin123
```

## 🧪 Testing

```powershell
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=YourTestClass

# Run with coverage report
mvn test jacoco:report
```

## 📦 Dependencies (from pom.xml)

- **MongoDB Driver** 4.11.1 - Database connectivity
- **Google Cloud AI Platform** 3.30.0 - Gemini AI integration
- **OkHttp** 4.12.0 - HTTP client for API calls
- **Gson** 2.10.1 - JSON processing
- **Dotenv Java** 3.0.0 - Environment variable management
- **JBCrypt** 0.4 - Password hashing
- **SLF4J Simple** 2.0.9 - Logging

## 🐛 Troubleshooting

### "mvn command not found"
```powershell
$env:Path = "$env:USERPROFILE\apache-maven-3.9.9\bin;$env:Path"
mvn -v
```

### "java command not found" or wrong version
```powershell
$env:JAVA_HOME = 'C:\Program Files\Eclipse Adoptium\jdk-21.0.5.11-hotspot'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
```

### Build fails with "release version 21 not supported"
- Make sure you're using JDK 21 (not an older version)
- Run `java -version` to verify

### "NoClassDefFoundError" when running JAR
- Rebuild with: `mvn clean package -DskipTests`
- The shade plugin bundles all dependencies

### MongoDB connection fails
- Check your internet connection
- Verify MongoDB Atlas credentials in `.env`
- Ensure your IP is whitelisted in MongoDB Atlas

## 📞 Support

For issues:
1. Check this guide's troubleshooting section
2. Review the error messages carefully
3. Verify all prerequisites are installed
4. Check `.env` configuration

## 📄 License

[Add your license information here]

---

**Last Updated:** November 10, 2025  
**Java Version:** 21 (LTS)  
**Build Tool:** Maven 3.9.9
