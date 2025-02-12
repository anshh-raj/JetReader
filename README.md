# JetReader
The app developed using **Jetpack Compose** and **Kotlin** offers an intuitive and feature-rich platform for users to explore, track, and manage their reading lists. The app integrates with the **Google Books API** using **Retrofit** to fetch a wide variety of book data, including titles, authors, and descriptions, allowing users to discover new books with ease.

Key features of the app include:

1. **Book Discovery**: The app fetches book data from the Google Books API and displays it in a visually appealing and easy-to-navigate layout, powered by **Jetpack Compose** for a modern and responsive user interface.
   
2. **Book Tracking**: Users can save books from the available list and organize them into three sections:
   - **Started Reading**: Books that users have just begun reading.
   - **Reading**: Books actively being read.
   - **Completed Reading**: Books that users have finished reading.
   
   This app ensures that users can track their reading progress easily and stay motivated.

3. **User Authentication**: The app integrates **Firebase Authentication**, allowing users to securely sign in using email/password. This ensures a personalized experience and gives users access to their own reading list.

4. **Data Storage with Firestore**: All user-specific book data (including book status and progress) is stored in **Firebase Firestore**, providing real-time syncing across devices and ensuring that user data is always up-to-date and accessible. This seamless cloud integration means that users can switch devices without losing their data.

5. **MVVM Architecture & State Management**: The app follows the **MVVM (Model-View-ViewModel)** architectural pattern, providing a clear separation of concerns for better maintainability and scalability. **State management** is implemented to handle loading states, data errors, and UI updates efficiently, ensuring smooth performance even during network requests.

By using **Jetpack Compose** for UI development and **Kotlin** for concise and powerful code, the app offers an efficient, user-friendly interface. The integration of **Firebase** for authentication and data storage ensures a secure and synchronized experience across platforms. The **MVVM** architecture and **state management** provide a robust and scalable structure for future updates and new features.

Overall, the app empowers users to not only manage their reading progress but also personalize their reading journey while ensuring their data is safe and accessible anywhere.
