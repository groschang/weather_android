# Weather 

Let me introduce the weather application built with Jetpack Compose and Kotlin that allows users to view the current forecast for a specified city.

## Features

* Get current weather conditions
* View daily forecasts
* Search for weather by city name
* Manage weather locations
* Dynamic color themes
* Localization

## Getting Started

Just build the project and enjoy the weather forecast! All you need is an API key which should stored in in properties file. Check template `local.properties.template`

## Libraries

* **Retrofit:** Type-safe HTTP client for Android and Java
* **AccuWeather API:** Provides comprehensive weather data
* **Room Persistence Library:**  Lightweight and efficient database for data persistence
* **OkHttp:**  Efficient HTTP & HTTP/2 client for Android and Java applications

## Architecture

This application follows the Model-View-ViewModel (MVVM) architectural pattern, promoting separation of concerns and promotes a modular and maintainable codebase.

#### SOLID

**Single Responsibility Principle:** Each class has only one resposibility to exist.

**Abstraction:** Most of classes are defined uppon abstraction definition.

**Interface Segregation Principle:** Interfaces are simple and are grouped thematically

**Dependency Inversion Principle:** Variable values are passed into classes. Reference types can be easly swiped into correspondent versions, in case of need.

## License

[Specify the license for your project - e.g., MIT License]