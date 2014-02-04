social-ad-unit
==============

Proof of concept for Social Ad Unit



![alt text](https://raw2.github.com/smanikandan14/social-ad-unit/master/Snapshot1.png "")


![alt text](https://raw2.github.com/smanikandan14/social-ad-unit/master/Snapshot2.png "")

Tables Created:
1- device
2- accounts
3- appsinstalled
4- contacts
5- relationship

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
```


