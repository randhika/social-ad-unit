social-ad-unit
==============

Proof of concept for Social Ad Unit



![alt text](https://github.com/smanikandan14/social-ad-unit/blob/master/Snapshot1.png "")


![alt text](https://github.com/smanikandan14/social-ad-unit/blob/master/Snapshot2.png "")

Tables Created:

1- Device
+----------+-------------+------+-----+---------+----------------+
| Field    | Type        | Null | Key | Default | Extra          |
+----------+-------------+------+-----+---------+----------------+
| id       | int(11)     | NO   | PRI | NULL    | auto_increment |
| deviceid | varchar(50) | YES  |     | NULL    |                |
| os       | varchar(10) | YES  |     | NULL    |                |
+----------+-------------+------+-----+---------+----------------+

+----+-----------------+---------+
| id | deviceid        | os      |
+----+-----------------+---------+
|  1 | 358239056896642 | android |
|  2 | 355031040142321 | android |
+----+-----------------+---------+

2- accounts

+----------+-------------+------+-----+---------+----------------+
| Field    | Type        | Null | Key | Default | Extra          |
+----------+-------------+------+-----+---------+----------------+
| id       | int(11)     | NO   | PRI | NULL    | auto_increment |
| deviceid | int(11)     | YES  | MUL | NULL    |                |
| name     | varchar(50) | YES  |     | NULL    |                |
| type     | varchar(30) | YES  |     | NULL    |                |
+----------+-------------+------+-----+---------+----------------+

+----+----------+--------------------------------+----------+
| id | deviceid | name                           | type     |
+----+----------+--------------------------------+----------+
|  1 |        1 | mani.siddharth13@gmail.com     | gmail    |
|  2 |        1 | smanikandan14@gmail.com        | gmail    |
|  6 |        1 | smanikandan14@gmail.com        | gmail    |
|  7 |        2 | maheshwariramamurthy@gmail.com | gmail    |
+----+----------+--------------------------------+----------+


3 - appsinstalled
+----------+--------------+------+-----+---------+----------------+
| Field    | Type         | Null | Key | Default | Extra          |
+----------+--------------+------+-----+---------+----------------+
| id       | int(11)      | NO   | PRI | NULL    | auto_increment |
| deviceid | int(11)      | YES  | MUL | NULL    |                |
| name     | varchar(100) | YES  |     | NULL    |                |
| package  | varchar(100) | YES  |     | NULL    |                |
| appicon  | text         | YES  |     | NULL    |                |
+----------+--------------+------+-----+---------+----------------+

+-----+----------+------------------------------+------------------------------------------+---------+
| id  | deviceid | name                         | package                                  | appicon |
+-----+----------+------------------------------+------------------------------------------+---------+
|   1 |        1 | IMDb                         | com.imdb.mobile                          |         |
|   2 |        1 | com.qualcomm.timeservice     | com.qualcomm.timeservice                 |         |
|   3 |        1 | Spheres                      | com.negorp.spheres.lite                  |         |
|   4 |        1 | LinkedIn                     | com.linkedin.android                     |         |
| 102 |        2 | VLC Direct Pro Free          | com.vlcforandroid.vlcdirectprofree       |         |
+-----+----------+------------------------------+------------------------------------------+---------+

4 - Contacts
+-------------+--------------+------+-----+---------+----------------+
| Field       | Type         | Null | Key | Default | Extra          |
+-------------+--------------+------+-----+---------+----------------+
| id          | int(11)      | NO   | PRI | NULL    | auto_increment |
| deviceid    | int(11)      | YES  | MUL | NULL    |                |
| name        | varchar(100) | YES  |     | NULL    |                |
| email       | varchar(100) | YES  |     | NULL    |                |
| phonenumber | varchar(100) | YES  |     | NULL    |                |
| type        | varchar(100) | YES  |     | NULL    |                |
| skype_id    | varchar(100) | YES  |     | NULL    |                |
| thumbnail   | text         | YES  |     | NULL    |                |
+-------------+--------------+------+-----+---------+----------------+

+----+-------------------------------+--------------------------------+------------------+----------+-------------------------------+
| id | name                          | email                          | phonenumber      | type     | skype_id                      |
+----+-------------------------------+--------------------------------+------------------+----------+-------------------------------+
|  1 | Maheshwari                    | maheshwariramamurthy@gmail.com |                  | skype    | maheshwari0129                |
|  2 | Rajiv                         |                                | +919885350778    | skype    | rajivraog                     |
| 64 | rajeshd972@gmail.com          | rajeshd972@gmail.com           |                  | google+  | gprofile:-6226663321417474109 |
| 65 | sandep.amit@gmail.com         | sandep.amit@gmail.com          |                  | google+  | gprofile:-2639126960686381001 |
| 66 | Cliff Robins                  | cliffrobins@gmail.com          |                  | google+  | gprofile:59686722381612012    |
+----+-------------------------------+--------------------------------+------------------+----------+-------------------------------+

5 - relationship

+------------+---------+------+-----+---------+----------------+
| Field      | Type    | Null | Key | Default | Extra          |
+------------+---------+------+-----+---------+----------------+
| id         | int(11) | NO   | PRI | NULL    | auto_increment |
| deviceid1  | int(11) | YES  | MUL | NULL    |                |
| deviceid2  | int(11) | YES  | MUL | NULL    |                |
| contactsid | int(11) | YES  | MUL | NULL    |                |
+------------+---------+------+-----+---------+----------------+

+----+-----------+-----------+------------+
| id | deviceid1 | deviceid2 | contactsid |
+----+-----------+-----------+------------+
|  1 |         1 |         2 |          1 |
|  2 |         2 |         1 |         59 |
+----+-----------+-----------+------------+

REST Apis:

POST: /device

```{
    "deviceId": "aed233-92kskdj83",
    "os": "android"
}
```

POST: /account

```{
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
```{
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
}```

POST /contacts
```{
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
}```

GET /fetch_social_ad
{
"deviceId":"8"
}

