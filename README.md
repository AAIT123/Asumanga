# 📚 Asumanga – Manga & Novel Tracker

## 📖 Description
Asumanga is an Android application that allows users to register and track the mangas and novels they are reading.  
The app focuses on simplicity and offline usage, letting users keep track of their reading progress, ratings, and basic information.

This project is also a learning exercise for Android development using Java and XML.

---

## 🎯 Goals
- Track reading progress (current chapter vs total chapters)
- Easily add and edit manga and novel entries
- Separate views for mangas and novels
- Show recently updated entries
- Support light and dark mode
- Keep the app simple and offline-first

---

## 📱 Platform & Tech Stack
- **Platform**: Android
- **Minimum SDK**: API 28 (Android 9)
- **Language**: Java
- **UI**: XML layouts
- **Architecture**: Single Activity with Fragments
- **Theme**: Material (DayNight)

---

## 🧭 App Navigation
The app uses a Bottom Navigation Bar with four sections:

- **Home**  
  Displays recently updated manga and novel entries.

- **Manga**  
  Displays a list of all manga entries.

- **Novel**  
  Displays a list of all novel entries.

- **Add**  
  Allows registering a new manga or novel.

---

## 🧱 Current Structure

MainActivity
├── HomeFragment
├── MangaFragment
├── NovelFragment
└── AddFragment


Each section is implemented as a Fragment and displayed inside a single Activity.

---

## 🗂 Data Model (Planned)

Entry

    id

    title

    type (MANGA / NOVEL)

    author

    totalChapters

    currentChapter

    rating (Good / Meh / Bad)

    description (optional)

    coverImagePath (optional)

    lastUpdated

    storyGroup (optional, future use)


---

## 🚧 Development Status

- [x] Project setup
- [x] Main navigation (Bottom Navigation)
- [x] Fragment structure
- [ ] Home screen UI
- [ ] Add / Edit entry UI
- [ ] Local data storage
- [ ] Edit & delete entries
- [ ] Sorting by recently updated
- [ ] UI polish & improvements

---

## 🔮 Planned Improvements
- Link different media of the same story (manga, novel, anime)
- Search functionality
- Filters and sorting options
- Better visual customization

---

## 🤝 Contribution
This project is intended as a learning project.  
Suggestions, improvements, and contributions are welcome.