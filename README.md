<div align="center">

# Tour Splitter

### Split tour expenses with friends — no more awkward "you owe me" math

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Firebase](https://img.shields.io/badge/Firebase-Auth_%2B_Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Gradle](https://img.shields.io/badge/Build-Gradle_Kotlin_DSL-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)
[![Material](https://img.shields.io/badge/UI-Material_Components-757575?style=for-the-badge&logo=materialdesign&logoColor=white)](https://m3.material.io/)

<br/>

![Tour Splitter preview](assets/hero.webp)

<br/>

An Android app for splitting travel costs among friends. Create a tour, add your travel buddies, log every expense (fuel, food, hotel…), and see who paid what and who owes whom. Built with a clean MVP architecture and Firebase as the backend.

</div>

## ✨ Features

- **Login & registration** — email/password auth powered by Firebase Authentication
- **Create tours** — local or international, with a name and details
- **Dashboard** — overview of all your tours at a glance
- **Manage members** — add and remove the friends on each trip
- **Add expenses** — log each cost with description, amount, category, and who paid
- **Expense summary** — per-member balances so settling up is painless

## 🛠 Tech Stack

| Layer | Tech |
|---|---|
| Language | Java (screens) + Kotlin (theme/config) |
| Architecture | MVP — `model/`, `view/`, `presenter/` per feature |
| UI | XML layouts, RecyclerView, CardView, Material Components |
| Backend | Firebase Authentication + Cloud Firestore (`google-services.json` included) |
| Build | Gradle with Kotlin DSL (AGP 8.x, `minSdk 30` / `targetSdk 36`) |

## 🚀 Build & Run

**Prerequisites:** Android Studio (Hedgehog or newer) and a JDK.

```bash
# Clone and build a debug APK
./gradlew assembleDebug

# …or open the project in Android Studio and press ▶ Run
```

The Firebase config (`app/google-services.json`) is already in the repo, so auth and Firestore work out of the box against the linked Firebase project. To point it at your own project, create a Firebase app with the package name `com.example.toursplitter` and replace that file.

## 📁 Project Structure

```
TourSplitter/
├── app/src/main/java/com/example/toursplitter/
│   ├── model/        # User, Tour, Member, Expense, Balance
│   ├── view/         # View contracts (interfaces) per screen
│   ├── presenter/    # MVP presenters — Firebase logic lives here
│   ├── *Activity.java # Login, Dashboard, CreateTour, ManageMembers,
│   │                   # AddExpense, ExpenseSummary, TourDetails, Register
│   └── ui/theme/     # Material 3 theme (Kotlin)
├── app/src/main/res/ # XML layouts, strings, colors, themes
└── build.gradle.kts  # app module config (Firebase BoM, Compose BOM)
```

## 📝 What I learned

My first real Android project end to end — structuring code with MVP, wiring Firebase Auth and Firestore into presenters, building RecyclerView lists, and surviving Gradle. The idea came from a real pain point: settling trip expenses in a group chat is a mess.

---

<div align="center">

Built by **Hussnain Ahmad** — [github.com/hussnainahmedd](https://github.com/hussnainahmedd)

</div>
