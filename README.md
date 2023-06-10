# Music Playlist Service

The Music Playlist Service is a robust RESTful API project that utilizes Java, AWS Lambda, and API Gateway to manage music playlists stored in DynamoDB tables. This service provides a scalable and efficient solution for creating, updating, and retrieving playlists and album tracks.

## Key Features

- **Playlist Creation:** Customers can create a new, empty playlist by providing a name and a list of tags.
- **Playlist Retrieval:** Customers can retrieve their playlist by specifying the playlist ID.
- **Playlist Name Update:** Customers have the ability to update the name of their playlist.
- **Song Addition (End of Playlist):** Customers can add a song to the end of their playlist.
- **Song Addition (Beginning of Playlist):** Customers can add a song to the beginning of their playlist.
- **Song Retrieval in Custom Order:** Customers can retrieve all songs in their playlist in a specific order, including ascending order, descending order, or shuffled order.

