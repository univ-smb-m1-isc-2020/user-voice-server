## `UserVoice-Server` 

### API Documentation
#### Users
`Register a user:`
url : localhost:8080/register

requestedJson:
```json
{
  "email":"docUser@gmail.com",
  "password":"1234",
  "username":"UserDoc"
}
```
Response : 
```json
{
  "id": 1,
  "content": "register succeed",
  "sucess": true
}
```

`Login:`

url : localhost:8080/login
Basic Authentication 
Response : 
```json
{
  "id": 9,
  "content": "User{id=57, username='UserDoc', email='docUser@gmail.com'}",
  "sucess": true
}
```
#### Site for APIKEY

`Create a site:`

url : localhost:8080/site/create
Basic Authentication
requestedJson:
```json
{
  "nameSite":"testSite"
}
```
Response :
```json
{
  "id": 1,
  "content": "WebSite{id=58, nameSite='SiteDoc', apiKey='60f75d42-fa60-40c7-ad08-7dd8e439b9f7', user=User{id=57, username='UserDoc', email='docUser@gmail.com'}}",
  "sucess": true
}
```

`Get all site of user:`

url : localhost:8080/site/get/user
Basic Authentication
Response :
```json
{
  "id": 3,
  "content": "[WebSite{id=58, nameSite='SiteDoc', apiKey='60f75d42-fa60-40c7-ad08-7dd8e439b9f7', user=User{id=57, username='UserDoc', email='docUser@gmail.com'}}]",
  "sucess": true
}
```

#### Features

`Add a feature:`

url : localhost:8080/features/create

requestedJson:
```json
{
  "apiKey":"60f75d42-fa60-40c7-ad08-7dd8e439b9f7",
  "feature":{
    "text":"Faire une meilleure doc",
    "emailAuthor":"test@gmail.com"
  }
}
```
Response : 
```json
{
  "id": 1,
  "content": "{id=59, text='Faire une meilleure doc', emailAuthor='test@gmail.com', ELO=1000, won=false, webSite={id=58, nameSite='SiteDoc', apiKey='60f75d42-fa60-40c7-ad08-7dd8e439b9f7', user={id=57, username='UserDoc', email='docUser@gmail.com'}}}",
  "sucess": true
}
```

`Get a Match of two randow features:`

url : localhost:8080/features/getMatch

requestedJson:
```json
{
  "apiKey":"d469c41a-b62a-4cee-9ed6-eeff5c6f4a14"
}
```
Response : 
```json
{
  "feature1": {
    "id": 33,
    "text": "Dark mode",
    "emailAuthor": "test@gmail.com",
    "won": false,
    "webSite": {
      "id": 31,
      "nameSite": "testSite2",
      "apiKey": "d469c41a-b62a-4cee-9ed6-eeff5c6f4a14",
      "owner": {
        "id": 26,
        "username": "Tom",
        "email": "tom@gmail.com",
        "password": "$2a$10$9TWp2eZ.IFTp6TwkSsKrsuv9oTF0SzVZzR7ndnq6iBCxWE/.wEabq"
      }
    },
    "elo": 970
  },
  "feature2": {
    "id": 32,
    "text": "Changer la police",
    "emailAuthor": "test@gmail.com",
    "won": false,
    "webSite": {
      "id": 31,
      "nameSite": "testSite2",
      "apiKey": "d469c41a-b62a-4cee-9ed6-eeff5c6f4a14",
      "owner": {
        "id": 26,
        "username": "Tom",
        "email": "tom@gmail.com",
        "password": "$2a$10$9TWp2eZ.IFTp6TwkSsKrsuv9oTF0SzVZzR7ndnq6iBCxWE/.wEabq"
      }
    },
    "elo": 1066
  }
}
```

url : localhost:8080/features/returnResultMatch

requestedJson:
```json
{
  "apiKey":"d469c41a-b62a-4cee-9ed6-eeff5c6f4a14",

  "matchFeatures":{
    "feature1": {
      "id": 32,
      "text": "Changer la police",
      "emailAuthor": "test@gmail.com",
      "won": true,
      "webSite": {
        "id": 31,
        "nameSite": "testSite2",
        "apiKey": "d469c41a-b62a-4cee-9ed6-eeff5c6f4a14",
        "owner": {
          "id": 26,
          "username": "Tom",
          "email": "tom@gmail.com",
          "password": "$2a$10$9TWp2eZ.IFTp6TwkSsKrsuv9oTF0SzVZzR7ndnq6iBCxWE/.wEabq"
        }
      },
      "elo": 1025
    },
    "feature2": {
      "id": 33,
      "text": "Dark mode",
      "emailAuthor": "test@gmail.com",
      "won": false,
      "webSite": {
        "id": 31,
        "nameSite": "testSite2",
        "apiKey": "d469c41a-b62a-4cee-9ed6-eeff5c6f4a14",
        "owner": {
          "id": 26,
          "username": "Tom",
          "email": "tom@gmail.com",
          "password": "$2a$10$9TWp2eZ.IFTp6TwkSsKrsuv9oTF0SzVZzR7ndnq6iBCxWE/.wEabq"
        }
      },
      "elo": 975
    }
  }
}
```
Response :
```json
{
  "id": 2,
  "content": "{\"feature1\":{\"id\":32,\"text\":\"Changer la police\",\"emailAuthor\":\"test@gmail.com\",\"ELO\":1046,\"won\":true,\"webSite\":{\"id\":31,\"nameSite\":\"testSite2\",\"apiKey\":\"d469c41a-b62a-4cee-9ed6-eeff5c6f4a14\",\"owner\":{\"id\":26,\"username\":\"Tom\",\"email\":\"tom@gmail.com\",\"password\":\"$2a$10$9TWp2eZ.IFTp6TwkSsKrsuv9oTF0SzVZzR7ndnq6iBCxWE/.wEabq\"}}},\"feature2\":{\"id\":33,\"text\":\"Dark mode\",\"emailAuthor\":\"test@gmail.com\",\"ELO\":950,\"won\":false,\"webSite\":{\"id\":31,\"nameSite\":\"testSite2\",\"apiKey\":\"d469c41a-b62a-4cee-9ed6-eeff5c6f4a14\",\"owner\":{\"id\":26,\"username\":\"Tom\",\"email\":\"tom@gmail.com\",\"password\":\"$2a$10$9TWp2eZ.IFTp6TwkSsKrsuv9oTF0SzVZzR7ndnq6iBCxWE/.wEabq\"}}}}",
  "sucess": true
}
```

`Get all proposed features of the same author on a site:`

url : localhost:8080/getFeatureByAuthor

requestedJson:
```json
{
    "email":"tom.kubasik@gmail.com",
    "tableName":"testfeatures"   
}
```
Response : 
```json
{
    "features": [
        {
            "id": 1,
            "textFeature": "Mettre un light mode",
            "authorEmail": "tom.kubasik@gmail.com",
            "elo": 0,
            "mmr": 0
        },
        {
            "id": 2,
            "textFeature": "Mettre un dark mode",
            "authorEmail": "tom.kubasik@gmail.com",
            "elo": 0,
            "mmr": 0
        }
    ]
}
```


`Get all proposed features on a site:`

url : localhost:8080/features/getAllFromWebSite

requestedJson:
```json
{
  "apiKey":"d469c41a-b62a-4cee-9ed6-eeff5c6f4a14"
}
```
Response : 
```json
{
  "id": 3,
  "content": "[{id=32, text='Changer la police', emailAuthor='test@gmail.com', ELO=1046, won=false, webSite=WebSite{id=31, nameSite='testSite2', apiKey='d469c41a-b62a-4cee-9ed6-eeff5c6f4a14', user={id=26, username='Tom', email='tom@gmail.com'}}}, {id=33, text='Dark mode', emailAuthor='test@gmail.com', ELO=950, won=false, webSite=WebSite{id=31, nameSite='testSite2', apiKey='d469c41a-b62a-4cee-9ed6-eeff5c6f4a14', user={id=26, username='Tom', email='tom@gmail.com'}}}]",
  "sucess": true
}
```

`Vote for a feature:`

url : localhost:8080/features/voteForFeature
NEED AUTHENTICATION

requestedJson: 
(id feature)
```json
{
  "id": 33
}
```
Response :
(if already voted)
```json
{
  "id": 3,
  "content": "AlreadyVoted",
  "sucess": true
}
```
(if voted too much)
```json
{
  "id": 3,
  "content": "Too much vote",
  "sucess": true
}
```
(if Succeed) need fix !
```json
{
  "id": 4,
  "content": "fail vote",
  "sucess": false
}
```


`Get number of vote today for the user:`

url : localhost:8080/features/getNumberVoteToday
NEED AUTHENTICATION

Response :
(if already voted)
```json
{
  "id": 3,
  "content": 1,
  "sucess": true
}
```







