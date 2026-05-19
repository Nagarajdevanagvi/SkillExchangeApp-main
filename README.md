# 🚀 SkillExchange App

SkillExchange is a real-time Android application that allows users to exchange services and skills without using money.

The platform works on a skill barter system where users can:

* connect with skilled workers
* send swap requests
* chat in real time
* manage ongoing swaps

Built using **Kotlin**, **Jetpack Compose**, and **Firebase**.

---

# ✨ Features Implemented

## 🔐 Authentication System

* Firebase Email Authentication
* User Registration
* User Login
* Persistent Login Session
* Logout Functionality

---

## 👤 User Profile System

Users can:

* Create profiles
* Edit personal information
* Add bio and location
* Add skills offered
* Add skills wanted
* Save profile data to Firebase Firestore
* Upload profile pictures using Firebase Storage

---

# 🏠 Home Screen

* View all registered users
* Explore available skills
* Connect with other users
* Modern card-based UI using Jetpack Compose

---

# 🔄 Skill Swap Workflow

Implemented complete swap lifecycle system.

## Features:

* Send swap requests
* Accept requests
* Reject requests
* Start ongoing swaps
* Complete swaps
* Cancel swaps

## Swap Status Workflow:

* Pending
* Accepted
* Rejected
* Ongoing
* Completed
* Cancelled

All swap data is stored and updated in Firebase Firestore in real time.

---

# 💬 Real-Time Chat System

* One-to-one chat functionality
* Real-time messaging using Firestore
* Separate conversations for users
* Live message updates using Kotlin Flow

---

# ☁ Firebase Integration

Integrated Firebase services:

* Firebase Authentication
* Cloud Firestore
* Firebase Storage

---

# 🛠 Tech Stack

| Technology              | Purpose               |
| ----------------------- | --------------------- |
| Kotlin                  | Programming Language  |
| Jetpack Compose         | UI Development        |
| Firebase Authentication | Login & Registration  |
| Cloud Firestore         | Real-time Database    | 
| MVVM Architecture       | Project Structure     |
| Kotlin Coroutines       | Async Operations      |
| StateFlow & Flow        | Real-time UI Updates  |

---

# 🧠 Architecture

The application follows **MVVM Architecture**.

### Layers:

* UI Layer (Compose Screens)
* ViewModel Layer
* Repository Layer
* Firebase Backend

---

# 📱 Screens Developed

## Authentication Screen

* User Login & Registration

## Home Screen

* User discovery
* Skill browsing
* Connect & Chat option

## Profile Screen

* Edit user profile
* Add skills
* Upload profile picture

## Swap Screen

* Manage swap requests
* Accept/Reject workflow
* Ongoing & completed swaps

## Chat Screen

* Real-time messaging interface

---

# 🔄 User Workflow

1. User registers/logs in
2. Creates profile
3. Adds offered and wanted skills
4. Browses other users
5. Sends swap request
6. Other user accepts/rejects
7. Users communicate through chat
8. Swap progresses through workflow
9. Swap gets completed

---

# 🚀 Future Improvements

Planned future features:

* Push Notifications
* Online/Offline Status
* Skill Search & Filtering
* Ratings & Reviews
* AI Skill Matching
* Regional Language Support
* Voice-Based Features

---

# ⚙️ How to Run the Project

## 1️⃣ Clone Repository

```bash
git clone <your-github-link>
```

---

## 2️⃣ Open in Android Studio

Open the project folder in Android Studio.

---

## 3️⃣ Connect Firebase

Enable:

* Authentication
* Firestore Database
* Firebase Storage

Add:

```text
google-services.json
```

inside:

```text
app/
```

---

## 4️⃣ Sync Gradle

Click:

```text
Sync Project with Gradle Files
```

---

## 5️⃣ Run the Application

Use:

* Android Emulator
  OR
* Physical Android Device

---

# 👨‍💻 Developed By

## Prathijna KP

1VK22IS042

---

# 🌟 Project Goal

SkillExchange aims to create a community-driven platform where users can exchange services and skills without relying on money, making collaboration and self-employment more accessible.

---

# ❤️ Thank You

Thank you for checking out this project!
Feel free to explore and improve the application.
