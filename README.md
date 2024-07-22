# Android Movies Info App 🎬

Welcome to the Android Movies Info App! This project is designed to provide a seamless movie browsing experience with features such as searching for movies, viewing detailed information, and managing favorite movies. Below you will find all the necessary information to set up and run the project.
[YOUTUBE LINK](https://www.youtube.com/watch?v=qkJQTSvL674)

## Prerequisites 🛠️

Ensure you have the following installed:

- Android Studio
- Kotlin

## Installation 💻

1. Clone the repository:
    ```bash
    git clone https://github.com/dorontayar/Android-Movies-Info-App.git
    cd Android-Movies-Info-App
    ```

2. Open the project in Android Studio.

3. Sync the project with Gradle files to install dependencies.

## Configuration ⚙️

1. Download the `google-services.json` file from your Firebase project and place it in the `app` directory.

2. Create a `local.properties` file in the root directory with the following content:
    ```properties
    TMDB_API_KEY=tmdb_key
    YOUTUBE_API_KEY=youtube_key
    ```

3. Ensure you replace `tmdb_key` and `youtube_key` with your actual TMDB and YouTube API keys.

## Usage 📖

To run the app:

1. Connect an Android device or start an emulator.
2. Click on the "Run" button in Android Studio.

The app should start on your device/emulator, and you can begin browsing movies.

## Features ✨

- **Explore:** Browse upcoming and top-rated movies.
- **Search Movies:** Search for movies using the TMDB API.
- **Movie Details:** View detailed information about each movie.
- **Favorites:** Save movies to your favorites list for easy access.
- **User Authentication:** Register and log in using Firebase Authentication.

## Architecture 🏛️

The app follows the MVVM (Model-View-ViewModel) architecture pattern for clean and maintainable code. Key components include:

- **Repository:** Handles data operations.
- **ViewModel:** Prepares data for the UI.
- **LiveData:** Observes data changes and updates the UI.
- **Room Database:** Manages local data storage.
- **Retrofit:** Handles network requests.
- **Dagger Hilt:** Manages dependency injection.
- **Coroutines:** Manages background threads for asynchronous tasks.

## Code Overview 👨‍💻

### Main Components

- **MainActivity:** Hosts the fragments and handles navigation.

### Fragments

- **ExploreFragment:** Displays a list of popular and top-rated movies.
- **MovieDetailFragment:** Shows detailed information about a selected movie.
- **FavoritesFragment:** Displays the user's favorite movies.
- **SearchFragment:** Allows users to search for movies.
- **ProfileFragment:** Manages user profile information.
- **RegisterFragment:** Handles user registration.
- **LoginFragment:** Handles user login.

### ViewModels

- **ExploreFragment:** Manages popular and top-rated movies data.
- **MovieDetailModel:** Manages a movie data.
- **FavoritesViewModel:** Manages favorite movies data.
- **SearchViewModel:** Handles movie search functionality.
- **ProfileViewModel:** Manages user profile data.
- **RegisterViewModel:** Manages user registration process.
- **LoginViewModel:** Manages user login process.

### Repositories

- **MovieRepository:** Handles operations related to movie data, both from the network and local database.
- **AuthRepository:** Manages user authentication and profile information.

### Additional Components

- **AppModule:** Provides dependency injection configurations using Dagger Hilt.
- **AppDatabase:** Manages the Room database instance for storing favorite movies and other local data.

## Contributing 🙌

1. Fork the project.
2. Create your feature branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -m 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request.


---

Feel free to contribute to this project by adding features, fixing bugs, or improving documentation.

For any questions or issues, please open an issue on GitHub.

---

