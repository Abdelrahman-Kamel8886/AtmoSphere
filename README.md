
# AtmoSphere ☁️🌍

**AtmoSphere** is a modern weather application built with **Jetpack Compose** and **Kotlin**, delivering real-time, location-based forecasts through a beautiful, adaptive interface. It supports multi-language settings, animated weather backgrounds, offline caching, and personalized weather alerts — offering a complete weather experience.

---

## 🌟 Features

### 📍 Location-Based Weather
- Automatically detects your location using **GPS**
- Manually pick a location from the **map**
- **Home Screen** displays:
  - Current weather conditions
  - Hourly forecast
  - 5-day forecast

### ⭐ Favorite Locations
- Add favorite cities/places using the map
- View saved locations and their weather anytime

### ⏰ Weather Alerts
- Create custom alerts with:
  - Sound alarms or
  - Silent notifications
- Set active duration for alerts
- Managed via **Broadcast Receivers**

### ⚙️ Settings
- **Languages Supported:** Arabic 🇪🇬, English 🇺🇸, Spanish 🇪🇸  
  - Auto-updates based on **system language**
- **Temperature Units:** Celsius (°C), Fahrenheit (°F), Kelvin (K)
- **Wind Speed Units:** meters/second (m/s), miles/hour (mph)
- **Location Mode:** GPS or manual map selection
- **Animated Weather Backgrounds:** On/Off toggle
- Settings are persisted using **SharedPreferences**

### 🌐 Offline Mode
- View cached weather data when offline:
  - Current weather (home)
  - Favorite locations

### 🎨 Adaptive UI & Dynamic Visuals
- Interface changes based on:
  - Weather conditions (e.g., sunny, cloudy, rainy)
  - Time of day (day/night)
- Background animations reflect live weather
- **Sun Cycle Card** shows real-time sunrise and sunset updates

---

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **Core Libraries & Tools:**
  - **OpenWeatherMap API** – Weather data source ([Link](https://openweathermap.org/api))
  - **Retrofit** – API calls
  - **Room** – Local storage
  - **Coroutines** – Background threading
  - **SharedPreferences** – User preferences
  - **Broadcast Receivers** – Alerts and system listeners
  - **WorkManager / AlarmManager** – Scheduled tasks
  - **Google Maps API / GPS** – Location access

---

## 🚀 Getting Started

### Clone the repository:
```bash
git clone https://github.com/Abdelrahman-Kamel8886/AtmoSphere.git
```

### Open in Android Studio:
1. Launch Android Studio
2. Go to **File > Open**, and select the cloned folder
3. Wait for Gradle sync to finish

### Run the application:
- Connect an Android device or emulator
- Press **Run** ▶️ or use `Shift + F10`

---

## Contributing
Contributions are welcome! If you have suggestions or improvements, please fork the repository and submit a pull request. Ensure that your contributions adhere to the coding standards and guidelines of the project.

## Contact
For any questions or inquiries, please contact:
- ### Abdelrahman Kamel
  - **LinkedIn: [LinkedIn Profile](www.linkedin.com/in/abdelrahman-kamel-7a7457200)**
  - **Email: abdelrahmankamel8886@gmail.com**
