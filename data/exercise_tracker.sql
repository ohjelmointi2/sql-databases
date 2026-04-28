-- These SQL commands and example data have been generated with ChatGPT.
DROP DATABASE IF EXISTS ExerciseTracker;

CREATE DATABASE ExerciseTracker;

USE ExerciseTracker;

CREATE TABLE ExerciseLog (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    date DATE,
    sport TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL,
    location TEXT,
    notes TEXT
);

INSERT INTO ExerciseLog (date, sport, duration_minutes, location, notes) VALUES
    ('2027-04-01', 'running', 30, 'park', 'morning run, felt good'),
    ('2027-04-02', 'gym', 45, 'fitness center', 'upper body workout'),
    ('2027-04-03', 'biking', 60, 'city', 'commute to work'),
    ('2027-04-04', 'football', 90, 'sports field', 'evening match with friends'),
    ('2027-04-05', 'walking', 25, 'forest', 'relaxing walk, light rain');
