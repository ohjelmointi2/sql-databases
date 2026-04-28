# SQL databases for the exercises

This folder contains several assets for the exercises in this repository. Some of the files are a required part of the exercise, while others are optional and can be used for more advanced exercises.


## Chinook Database

The Chinook Database is a sample database that contains data about a digital media store. It includes tables for artists, albums, tracks, customers, invoices, and more. The database is often used for learning SQL and practicing database queries.

This folder contains two versions of the Chinook Database. The `Chinook_Sqlite.sqlite` file is a complete SQLite database file that can be used with SQLite tools or from your Java application. The `Chinook_MySql_AutoIncrementPKs.sql` file is a text file containing SQL statements to create the database schema and insert sample data, which can be executed in a MySQL database.


## Docker Compose File

The `docker-compose.yml` file is a configuration file for Docker Compose, which allows you to easily set up and run a MySQL database in a Docker container. To use this file, you need to have Docker and Docker Compose installed on your machine. Alternatively, you can use a cloud based development environment that supports Docker, such as GitHub Codespaces.

The file defines a service named `db` that uses the official MySQL image from Docker Hub. It also sets up environment variables, port mappings, and volume mounts for data persistence and initialization scripts.

Docker is not required for the exercises, but it can be a convenient way to set up a MySQL database without needing to install it directly on your machine. It also allows initializing the database with the provided SQL scripts, which makes the setup process even easier.

If you choose to use Docker, you can run the MySQL container with the command `docker-compose up` in the same directory as the `docker-compose.yml` file. This will start the MySQL database, and you can connect to it using the credentials specified in the file. To remove the container and its data, you can run `docker-compose down --volumes`.


## Exercise Tracker SQL Script

The exercise tracker script (`exercise_tracker.sql`) is a SQL file that contains statements to create a simple database for tracking exercise activities. This file is not required for the exercises, but it is here for demonstration purposes and can be used on lecture demos.
