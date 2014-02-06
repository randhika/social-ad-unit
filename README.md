social-ad-unit
==============

Proof of concept for Social Ad Unit

Uses Node.js for the server. 

![alt text](https://raw2.github.com/smanikandan14/social-ad-unit/master/Snapshot1.png "")


![alt text](https://raw2.github.com/smanikandan14/social-ad-unit/master/Snapshot2.png "")

| Tables Created |
|--------------|
| device |
| accounts |
| appsinstalled |
| contacts |
| relationship |

REST Apis:

```
POST: /device

{
    "deviceId": "aed233-92kskdj83",
    "os": "android"
}


POST: /account

{
    "accounts": [
        {
            "name": "13202008820",
            "type": "whatsapp"
        },
        {
            "name": "smani14",
            "type": "skype"
        },
        {
            "name": "smanikandan14",
            "type": "twitter"
        },
    ],
    "deviceId": "8"
}```


POST /appsinstalled
{
    "deviceId": "8", 
    "installed": [
        {
            "appicon": "ncncnjdkeik-929kdkejdloo", 
            "name": "skype", 
            "package": "com.skype"
        }, 
        {
            "appicon": "mncbvbndjeikioqoq-1idue3jdjdj", 
            "name": "twitter", 
            "package": "com.twitter"
        }
    ]
}

POST /contacts
{
    "contacts": [
        {
            "email": "naveen4952@gmail.com",
            "imname": "gprofile:3983736737189282315",
            "name": "Aravind SN",
            "type": "google+",
            “bitmap”:”iVBORw0KGgoAAAANSUhEUgAAAGAAAABgCAIAAABt+uBvAAAAA3NCSVQICAjb4U”
        }
    ],
    "deviceId": "6"
}

GET /fetch_social_ad
{
"deviceId":"8"
}

response:
----------
[
    {
        "appinstalled": {
            "appicon": "",
            "deviceid": 1,
            "id": 5,
            "name": "Instagram",
            "package": "com.instagram.android"
        },
        "contact": {
            "contact_id": "1235",
            "contactsid": 123,
            "deviceid1": 3,
            "deviceid2": 1,
            "id": 6,
            "name": "Maheshwari"
        }
    },
    {
        "appinstalled": {
            "appicon": "",
            "deviceid": 4,
            "id": 171,
            "name": "Smart Measure",
            "package": "kr.sira.measure"
        },
        "contact": {
            "contact_id": "1236",
            "contactsid": 124,
            "deviceid1": 3,
            "deviceid2": 4,
            "id": 7,
            "name": "W3i"
        }
    }
]

```


