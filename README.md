# City Filter Android Application

## Overview

This Android application is designed to efficiently load, filter, and display a list of cities from a JSON file containing around 200,000 entries. Each city entry includes the country, name, ID, and coordinates (longitude and latitude). The app supports responsive filtering by prefix string and displays cities in alphabetical order.

## Features

- Load a large list of cities from a JSON file.
- Filter results by a given prefix string with optimized search for fast runtime.
- Case-insensitive search that updates in real-time as the user types.
- Display cities in a scrollable list, sorted alphabetically by city and country.
- Each city entry shows the city and country code as the title, coordinates as the subtitle.
- Tap on a city to view its location on Google Maps.
- Supports screen rotation.
- Developed in Kotlin, compatible with Android 5.0+.

## Technical Details

### Architecture and Design Patterns

The application follows the MVVM (Model-View-ViewModel) architecture pattern to ensure separation of concerns, testability, and maintainability. 

### Data Representation

To optimize search efficiency, the list of cities is preprocessed and stored in a Trie data structure. This allows for faster prefix-based searches compared to linear searches.

### Dependencies

- JSON serialization: 
- Dependency Injection:
- Android Jetpack Suite:
  - LiveData
  - ViewModel
  - RecyclerView
  - Data Binding

## Setup and Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/Mohamed-5der/Klivvr-Project.git
