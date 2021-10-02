
#MineSweeperAPI: Classic Windows Game 

###Introduction

Even though I played this game as a child, back when Windows XP was the most popular OS around, I just randomly
clicked on cells to see if I get the win by luck, I knew numbers should mean something but as a child a never
felt interested in finding out more, now I regret because it was obviously a great game for a child and I did
not got the best out of it.

###Project Description

This projects aims to replicate the minesweeper game behavior, it exposes a rest api layer which expect a json
object representation. 

###Project Solution Considerations

By no mean I believe this solution is at his best, there is a lot I'd like to fix at the moment, and lot of work
to do mostly in the security layer, I know plain token for authentication is not secure enough, If I get more
time with this project I'd add encryption (Asymmetric Encryption).

In the MineSweeperComponent, there are methods that need to be decoupled a bit more to look cleaner, if I get the
time to improve that, maybe using the adapter patter to decouple son components and its functions would be a good 
choice.

The most difficult part for me was finding all connected blank/empty cells, this was challenging because in bigger
boards with few bombs the number of iterations to find all spaces increase drastically, I'm aware that if the board
grows too big and place too few bombs it could become slow or consume to much server resources, something I would
do to fix this is to use the future interface to send a new thread to each board direction and the join the result.

I expect whoever is reading this to find my solution interesting and be open to give me any feedback to help me
keep growing as a developer, no matter how rude it can be, I want to hear it.

###How to use

This document describes the backend service interface available.

Notes to be aware of:
* This is a Restful JSON API web-service, which conforms to the constraints and characteristic of the Rest architectural style and uses JSON as its data representational format.
* There is console output on the backend that replicates the intended behaviour expected from the fron end
* The following server request-response description, in the same order should work with curl or postman.

 ----  SignUp: Receives an username and password to hash the password and inserted in a database
 

*/userSignUp* [GET]
 + request 
```
{
    "username": "admin",
    "password": "admin"
}
```
+ Response 200 (json)
```
SignUpDTO{username='admin', password='**********'
}
```

 ----  SignIn: Receives an username and password to match a previous registration made by signUpMethod
 Response an empty json body with a header jwt token to sent as a bearer token in  all subsequent service calls
 
 */userSignIn* [GET]
 + request 
```
{
    "username": "admin",
    "password": "admin"
}

```
+ Response 200 (json)
(Headers)
```
Token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJcImFkbWluOjIwMjAxMTg4XCIiLCJleHAiOjE2MzMxMzk1MDd9.3JajjMRfKnipIuFmd8jdo52WViyt_VTO7aFQif66SgQ
```


---- StartNewGame: Method to star a new mineSweeperGame
notes: if there is a previous mine sweeper game not saved, it will replace that

*/startNewGame* [GET]
+ Request
```
Authorization: Token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJcImFkbWluOjIwMjAxMTg4XCIiLCJleHAiOjE2MzMxMzk1MDd9.3JajjMRfKnipIuFmd8jdo52WViyt_VTO7aFQif66SgQ
```
+ Response 200 (json)
```
    New Game has been created
```

---- makeMove: Method to make a single move on mineSweeper game board

*/makeMove* [GET]
+ Request
```
Authorization: Token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJcImFkbWluOjIwMjAxMTg4XCIiLCJleHAiOjE2MzMxMzk1MDd9.3JajjMRfKnipIuFmd8jdo52WViyt_VTO7aFQif66SgQ
{
    "columnIndex":"6",
    "rowIndex":"3"
}  
```
+ Response 200 (json)
```
{
    "duration": "Time Elapsed: 42 Seconds",
    "emptyCellList": [
        {
            "columnIndex": 6,
            "rowElement": 4,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 5,
            "rowElement": 3,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 5,
            "rowElement": 4,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 6,
            "rowElement": 5,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 6,
            "rowElement": 3,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 5,
            "rowElement": 5,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 4,
            "rowElement": 3,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 6,
            "rowElement": 6,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        },
        {
            "columnIndex": 5,
            "rowElement": 6,
            "rowValue": 0,
            "gameRole": "Empty",
            "seen": true
        }
    ],
    "numberCellList": [
        {
            "columnIndex": 6,
            "rowElement": 2,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 5,
            "rowElement": 2,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 4,
            "rowElement": 2,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 4,
            "rowElement": 4,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 4,
            "rowElement": 5,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 4,
            "rowElement": 6,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 3,
            "rowElement": 3,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 3,
            "rowElement": 2,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        },
        {
            "columnIndex": 3,
            "rowElement": 4,
            "rowValue": 1,
            "gameRole": "Number",
            "seen": true
        }
    ],
    "bombCellList": [],
    "boardMoveResponseType": "GAME_INFO"
```
----  SaveGame: Save the current game status

 */saveGame* [GET]
 + request 
```
Authorization: Token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJcImFkbWluOjIwMjAxMTg4XCIiLCJleHAiOjE2MzMxMzk1MDd9.3JajjMRfKnipIuFmd8jdo52WViyt_VTO7aFQif66SgQ
```


+ Response 200 (json)
(Headers)
```
    Game has been saved
```
----  resumeGame: Retrieve last saved game
notes: this action will replace and discard any current game on memory

 */resumeGame* [GET]
 + request 
```
Authorization: Token: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJcImFkbWluOjIwMjAxMTg4XCIiLCJleHAiOjE2MzMxMzk1MDd9.3JajjMRfKnipIuFmd8jdo52WViyt_VTO7aFQif66SgQ
```


+ Response 200 (json)
(Headers)
```
    Game resumed succesfully
```


## Contact
Feel free to contact me - robertorojas20@gmail.com
