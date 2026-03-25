#  Echo - Mental Wellness Tracker

<p align="center">
  <img src="app/src/main/res/drawable/ic_splash_logo.png" alt="Echo Logo" width="200"/>
</p>

##  Overview
**Echo** is a beautifully designed, privacy-first Android application built to help users seamlessly track their mental wellness, log daily habits, and reflect on their emotions. Instead of sacrificing your intimate thoughts to cloud servers, Echo stores **100% of your data completely offline** using a highly-secured local Room database framework. 

Coupled with a sleek, edge-to-edge Glassmorphism design and deep Android API integrations, tracking your mental health has never felt so premium or private.

## Features
*  Total Privacy & Security:* A completely offline architecture using Jetpack Room and SHA-256 cryptographic password hashing to secure your personal journal entries internally on the device.
*  Daily Wellness Dashboard:* Easily log your sleep duration, water intake, daily exercise, and instantly calculate a "Wellness Score" summarizing your day.
*  Reflective Journaling:* A safe, persistent text workspace to write daily reflections and document personal growth.
*  Interactive History:* A swipeable, beautifully animated `RecyclerView` timeline that organizes all past wellness entries into translucent glass-morphic cards.
*  Focus & Breathing Timer:* A built-in guided focus session providing an auto-scaling breathing animation paired with a countdown timer to rapidly de-stress.
*  Native Support Gateway:* Direct hardware routing using Implicit Intents to instantly ping preset SMS and Email contacts in times of crisis.
*  Glassmorphism UI:* A highly modern, calming aesthetic leveraging `WindowCompat` to bleed immersively underneath system bars (edge-to-edge).

##  Tech Stack
* **Language:** Kotlin
* **Architecture:** MVVM (Model-View-ViewModel) / Single-Activity Architecture
* **UI Components:** Android Views, Material Design 3, Fragment Navigation Component
* **Local Database:** Jetpack Room (SQLite)
* **Local Key-Value Storage:** Android DataStore / SessionManager
* **Concurrency:** Kotlin Coroutines & Lifecycles
* **Environment:** Android Studio & Gradle 8.4 

##  Getting Started

### Prerequisites
* Android Studio (Iguana or newer recommended)
* Android SDK (API 34+)
* Ensure you have a virtual emulator or a physical Android device ready.

### Installation
1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/your-username/Echo-Wellness-App.git
   ```
2. Open the project folder in **Android Studio**.
3. Allow Gradle to sync completely to download all necessary Jetpack dependencies.
4. Click the green **Run (▶)** button at the top to compile the `.apk` and launch it on your device/emulator!

##  Contribution
This project was developed as a comprehensive exercise demonstrating the practical integration of complex Android laboratory tasks. Feel free to fork the repository, open an issue, or create a pull request if you want to extend its capabilities (e.g., adding Biometric Authentication, Chart Animations, etc.).

---

<p align="center">
  <i>Take a breath, take your time, and take care of your mind.</i>
</p>
