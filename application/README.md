Database Schema Overview

This document explains how the various tables in our schema interrelate and map the JSON structure into a normalized relational design.


Core Tables and Their Relationships

Users

Description:
Stores user profile and authentication details.
Relationship:
Each user can have multiple itineraries. The itineraries table references this table using the user's unique ID.

Itineraries

Description:
Contains the main itinerary information such as title, status, destination, dates, etc.
Relationships:
Belongs to a single user (via user_id).
One-to-many with Days (each itinerary can have multiple days).
One-to-one (or one-to-many if tracking changes) with Budgets for overall and category-specific budget allocations.
Many-to-many with Tags via the join table (itinerary_tags) for metadata and categorization.
Has multiple shared users defined in Shared_access.

Budgets

Description:
Tracks the overall trip budget along with breakdowns for sub-categories like accommodation, activities, dining, and transport.
Relationship:
Associated with a specific itinerary through its itinerary_id.

Days

Description:
Represents a breakdown of the itinerary by day (including day number, date, notes, and daily budgets).
Relationships:
Belongs to one itinerary (via itinerary_id).
Can have multiple associated Activities (covering hotels, restaurants, etc.).
May include travel information via the Travel_routes table.

Activities

Description:
Represents individual events such as visits to places (museums, restaurants, hotels, etc.).
Relationships:
Linked to a day (via day_id).
References a reusable Place (via place_id) that stores location information.
Ordered by a sequence_number to define the activity order for the day.

Places

Description:
Centralized table for storing reusable location data (hotels, restaurants, attractions).
Relationships:
Referenced by Activities to pull details like location, description, and ratings.
Linked to Operating_hours and Photos for additional information (opening times and images).

Operating_hours

Description:
Stores operating times for a place for each day of the week.
Relationship:
Each record is linked to a specific place (via place_id).

Photos

Description:
Maintains external images related to places or activities.
Relationship:
Each photo record ties back to a place (via place_id).

Travel_routes

Description:
Holds travel information between two activities in the same day, including distance, duration, and mode of travel.
Relationships:
References a day (via day_id).
Connects two activities using the from_activity_id and to_activity_id fields.

Shared_access

Description:
Provides sharing details for each itinerary, specifying who can view or edit.
Relationship:
Connects an itinerary (via itinerary_id) with a user (via user_id) along with the pertinent permission level.

Supporting Tables and Their Relationships

Tags

Description:
Stores a list of unique tags for categorizing itineraries.
Relationship:
Linked to itineraries using a many-to-many relationship through the itinerary_tags join table.

Itinerary_tags

Description:
Join table linking itineraries and tags.
Relationship:
Each record pairs an itinerary with a tag, allowing multiple tags per itinerary and vice versa.

Mapping the JSON Structure

Itinerary Data:
Top-level JSON elements (e.g., id, userId, title, status, visibility, createdAt, updatedAt, and trip details such as destination and dates) map directly to the Itineraries table.

Budget Details:
The budget information under the JSON node tripDetails.budget maps to the Budgets table with fields for total and sub-category amounts.

Days:
Each day in the JSON (with attributes like date, dayNumber, and daily budgets) is captured in the Days table in a one-to-many relationship with Itineraries.

Activities and Places:

Each day’s sections (for instance, hotels, activities, restaurants) are stored in the Activities table, linked to a day.
Each activity references a Place which contains the normalized JSON data (location, description, operating hours, photos, etc.).
The detailed place data is further split into the Places, Operating_hours, and Photos tables.
Travel Information:
Travel-related JSON data (e.g., routes between places) is mapped to the Travel_routes table using references to activities (from_activity_id and to_activity_id).

Shared Access:
Shared items in the JSON directly map to the Shared_access table, defining additional users (besides the owner) who have access to each itinerary along with the permission levels.

Metadata and Tags:
Additional metadata such as tags and language information from the JSON are stored:

Language and version details directly in the Itineraries table.
Tag associations in the Tags and Itinerary_tags tables.

Recap of Table Relationships

users → itineraries: One-to-many (a user can have many itineraries).
itineraries → days: One-to-many (an itinerary contains multiple days).
itineraries → budgets: One-to-one (or one-to-many for historical tracking).
days → activities: One-to-many (each day can have many activities).
activities → places: Many-to-one (multiple activities can reference the same place).
places → operating_hours & photos: One-to-many (a place can have several operating hours and photos records).
days → travel_routes: One-to-many (each day can involve multiple travel legs).
itineraries → shared_access: One-to-many (an itinerary can be shared with multiple users).
itineraries → itinerary_tags → tags: Many-to-many (for metadata categorization).