Original App Design Project - README Template
===

# APP_NAME_HERE

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
App that allows user to search recipes.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Education/Entertainment
- **Mobile:** Can use push to suggest the recipes, and camera to make photo
- **Story:** Allows user to search and share recipes
- **Market:** Anyone who likes to cook and wants to find new recipes
- **Habit:** It can be used any time someone wants to cook, and pushes can make it more habit-forming
- **Scope:** Feasible, with recommendations being the hardest part.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
  * User can login
  * User can register
  * User can view recipe
      * User can write review
  * User can like the recipe
      * User can view number of likes
      * User can get recommendations based on likes
  * User can search among the recipes in the db
      * User can use various filters, like rating, exclude certain ingrindients, time etc.
      * User can get hints when searhing		
  * User can update preferences(like "never include certain ingridient") in the profile
      * User can select diets, prefered cuisines, intolerances from list of common intolerances and banned ingredients. 
  * User can enter what ingridients they currently have, and get recipe suggestions
  * User can update profile picture
  * User can view other people profiles
* ...

**Optional Nice-to-have Stories**
 * User can view substitutes to the ingredients
    * When viewing the recipe, user can slide to see how the ingridient could be substituted
 * User can view published recipes
    * User can add recipe
 * Search includes the rating
 * User can take a photo of the result and share in the social networks
 * User can get pushes with recommendations
 * Gather recipes from the other sources, and parse them into application format
 * User can share recipes with nearby users via bluetooth
 * User can get recipes suggestions based on what users nearby like
 * User can save some recipe offline
 * User can select "read outloud" on menus (ScreenReader)
 * User can search with voice
* ...

### 2. Screen Archetypes

* Login screen
* Stream
   * Recipes feed screen
   * Search screen
   * Get suggestiongs from what I have
* Detail
   * Recipe details screen
* Creation
   * Create recipes screen
   * Write review screen 
   * Profile details
* Profile
* Settings
   * Preferences screen
### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Search screen
* Recipes feed screen
* Get recipes rom what I have

**Flow Navigation** (Screen to Screen)

* Login -> search screen, register screen
* Search, Feed -> details screen
* Details -> create review screen
* Profile from top tab
* Profile -> update preferences, add recipes, compose post

## Wireframes
<img src="https://github.com/akp5896/RecipeApp/blob/main/PXL_20220614_171340617.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
#### Recipe
| Property | Type | Desciprtion|
|----------|------|------------|
| body | String | The text of the recipe |
| ingridients | String[] | List of the ingridients |
| votesSum | Number | sum of votes |
| numVotes | Number | number of votes |
| cuisine | String | cuisine to which recipe belongs to |
| timeToCook | Number | time that recipe takes |
| author | Pointer to user | User who submitted the recipe |
| reviews | Relation to reviews | User reviews of recipe |
| ratedBy | Relation to User | users who rated the recipe |
#### Review
| Property | Type | Desciprtion|
|----------|------|------------|
| body | String | Text of the review |
| author | Pointer to user | User who submitted review |
| media | Array | Attached photos |
#### User
| Property | Type | Desciprtion|
|----------|------|------------|
| preferences | Pointer to preferences | User settings |
| profilePicture | File | User avatar |
| bio | String | User bio |
| recipes | Relation to recipes | Recipes submitted by user |
| reviews | Relation to reviews | Reviews writeen by user |
#### Preferences 
| Property | Type | Desciprtion|
|----------|------|------------|
| numberOfVotes | Array | ingridients to exclude from search |
| avgTime | Number | average time of liked recipes |
| avgPrice | Number | average price of liked recipes |
| avgHealth | Number | average heathiness of liked recipes |
| userTaste | Pointer to taste |  taste of user |
| stdTime | Number | standart deviation of time of liked recipes |
| diet | relation to dietCounter | diets, and how often they occur in user liked recipes |
| cuisine | relation to cuisineCounter | cuisines, and how often they occur in user liked recipes |
#### Taste
| Property | Type | Desciprtion|
|----------|------|------------|
| sweetness | Number | average sweetness of liked recipes |
| saltiness | Number | average saltiness of liked recipes |
| sourness | Number | average sourness of liked recipes |
| bitterness | Number | average bitterness of liked recipes |
| savoriness | Number |  average savoriness of liked recipes |
| fattiness | Number | average fattiness of liked recipes |
| spiciness | Number | average spiciness of liked recipes |
#### dietCounter
| Property | Type | Desciprtion|
|----------|------|------------|
| name | String | name of the diet |
| counter | Number | how often user used the diet |
#### cuisineCounter
| Property | Type | Desciprtion|
|----------|------|------------|
| name | String | name of the diet |
| counter | Number | how often user used the cuisine |
### Networking
Base uri https://spoonacular.com/
| HTTP VERB | Endpoint | Desciprtion|
|----------|------|------------|
| GET | complex-search | search |
