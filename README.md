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

* [fill in your required user stories here]
* ...

**Optional Nice-to-have Stories**

* [fill in your required user stories here]
* ...

### 2. Screen Archetypes

* [list first screen here]
   * [list associated required story here]
   * ...
* [list second screen here]
   * [list associated required story here]
   * ...

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* [fill out your first tab]
* [fill out your second tab]
* [fill out your third tab]

**Flow Navigation** (Screen to Screen)

* [list first screen here]
   * [list screen navigation here]
   * ...
* [list second screen here]
   * [list screen navigation here]
   * ...

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
| exclude | Array | ingridients to exclude from search |
| cuisine | String[] | cuisine user prefer |
| bannedRecipes | Relation to recipes | recipes to exclude from search |
| wouldLike | Array | ingridients user like |
| wouldLikeToExlude | Array | ingridients user don't  like |
| ratedRecipes | relation to recipes | recipes user rated |
### Networking
Base uri https://spoonacular.com/
| HTTP VERB | Endpoint | Desciprtion|
|----------|------|------------|
| GET | complex-search | search |
