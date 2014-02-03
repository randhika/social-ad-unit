var mysql = require('mysql');

// init;
var client = mysql.createConnection({
  user: 'root',
  password: 'joliee@22',
  hostname: 'localhost'  
}); 

client.query('USE Social_Ad');


exports.add_device = function(device,callback) {
    console.log(" ####### SQL add device ########### "+device.deviceid+" ::  "+device.os);
    client.query("insert into device (deviceid,os) values (?,?)", 
        [device.deviceid, device.os], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
          }
    });
}


exports.add_account = function(account,callback) {
      console.log(" ####### SQL add device ########### "+account.deviceid+" : "+account.name+" : "+account.type);
      client.query("insert into accounts (deviceid,name,type) values (?,?,?)", 
        [account.deviceid, account.name,account.type], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### ADD_ACCOUNT SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
            if(account.type == "skype") {

              findContactBySkypeName(account.name,function(err,result) {
                console.log(" ######### findContactByEmail ####### "+ result);
                if( err) {
                  console.log(" ######### add user error ####### "+JSON.stringify(err));
                } else {
                  // Add to relationship table.
                  console.log(" ######### SUCCESS 11 ####### "+JSON.stringify(result));
                  if( result.length > 0) {
                    console.log(" ######### SUCCESS 33 ####### "+account.deviceid);
                    add_relationship(account.deviceid,result[0].deviceid,result[0].id,function(err,result) {
                        console.log(" ######### add_relationship ####### "+ result);
                        if( err) {
                            console.log(" ######### add user ####### "+ JSON.stringify(err));
                        } else {
                          console.log(" ######### RELATIONSHIP SUCCESS ####### ");
                        }
                    });
                  }
                }
              }); 

            } else {
              findContactByEmail(account.name,function(err,result) {
                console.log(" ######### findContactByEmail ####### "+ result);
                if( err) {
                  console.log(" ######### add user error ####### "+JSON.stringify(err));
                } else {
                  // Add to relationship table.
                  console.log(" ######### SUCCESS 11 ####### "+JSON.stringify(result));
                  if( result.length > 0) {
                    console.log(" ######### SUCCESS 33 ####### "+account.deviceid);
                    add_relationship(account.deviceid,result[0].deviceid,result[0].id,function(err,result) {
                        console.log(" ######### add_relationship ####### "+ result);
                        if( err) {
                            console.log(" ######### add user ####### "+ JSON.stringify(err));
                        } else {
                          console.log(" ######### RELATIONSHIP SUCCESS ####### ");
                        }
                    });
                  }
                }
              }); 
           }
          }
      });
}

exports.add_appsinstalled = function(installed,callback) {
      console.log(" ####### SQL add device ########### "+installed.deviceid+" : "+installed.name+" : "+installed.pkg);
      client.query("insert into appsinstalled (deviceid,name,package,appicon) values (?,?,?,?)", 
        [installed.deviceid, installed.name,installed.pkg,installed.icon], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
          }
      });
}

exports.add_contact = function(contact,callback) {
      console.log(" ####### SQL add device ########### "+contact.deviceid+" : "+contact.name+" : "+contact.email+" : "+contact.ph+" : "+contact.skypeid);
      client.query("insert into contacts (deviceid,name,email,phonenumber,type,skype_id,thumbnail) values (?,?,?,?,?,?,?)", 
        [contact.deviceid, contact.name, contact.email, contact.ph, contact.type, contact.skypeid, contact.bitmap],
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
          }
      });
}

exports.getAppRecommendation = function(deviceid,callback) {
console.log(" ####### getAppRecommendation ######## "+ deviceid);
  client.query("select * from relationship where deviceid1=?",[deviceid],
  function(error, result) {
      if( error) {
        console.log(" ####### ERROR ######## "+JSON.stringify(error));
        callback(error,null);
      } else {
        console.log(" ####### SUCESS ######## "+JSON.stringify(result));
        console.log(" ####### SUCESS ######## "+result.length);
        var index = Math.floor((Math.random()*result.length)+1);
        console.log(" ####### SUCESS index ######## "+ index);
        var deviceid1 = result[index-1].deviceid1;
        var deviceid2 = result[index-1].deviceid2;
        var contactsid = result[index-1].contactsid;

        console.log(" ####### SUCESS deviceid2 ######## "+ deviceid2);

        findAppsInstalled(deviceid1,deviceid2,function(error1,result1) {

          if( error1) {
            console.log(" ####### appsInstalled Error ######## "+JSON.stringify(error1));
          } else {
            var index = Math.floor((Math.random()*result1.length)+1);
            console.log(" ####### App on Random is  ######## "+ result1[index-1].package+" : "+result1[index-1].name);

            getContact(contactsid, function ( error2,result2) {
              if( error2 ) {
                console.log(" ####### getContact Error ######## "+JSON.stringify(error2));
                callback(error2,null);
              } else {
                console.log(" ####### getContact Success ######## "+JSON.stringify(result2));
                var jsonResponse = [{status:'404'},{errorcode:'3002',error:"Invalid accesscode"}];
                //callback(null,info.insertId);
              }
            });
          }

        });
      }
  });

}

add_relationship = function(deviceid1,deviceid2,contactsid,callback) {
      console.log(" ####### SQL add_relationship ########### "+deviceid1+" : "+deviceid2+" : "+contactsid);
      client.query("insert into relationship (deviceid1,deviceid2,contactsid) values (?,?,?)", 
        [deviceid1,deviceid2,contactsid], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
          }
      });
}

getContact = function(contactsid,callback) {
      client.query("select * from contacts where id=?",[contactsid], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### getContact SUCESS ######## "+JSON.stringify(info));
            callback(err,info);
          }
      });
}

findAppsInstalled = function(deviceid1,deviceid2,callback) {
      client.query("select * from appsinstalled where deviceid=? and name not in (select name from appsinstalled where deviceid=?);",
        [deviceid2,deviceid1], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### findAppsInstalled SUCESS ######## "+JSON.stringify(info));
            callback(err,info);
          }
      });
}

findContactByEmail = function(email,callback) {
      client.query("select * from contacts where email=?",[email], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### findContactByEmail SUCESS ######## "+JSON.stringify(info));
            callback(err,info);
          }
      });
}

findContactByPhoneNumber = function(phonenumber) {
      client.query("select * from contacts where phonenumber=?", 
        [phonenumber], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
          }
      });
}

findContactBySkypeName = function(skypename,callback) {
      client.query("select * from contacts where skype_id=?", 
        [skypename], 
        function(err, info) {
          // callback function returns last insert id
          if( err) {
            console.log(" ####### ERROR ######## "+JSON.stringify(err));
            callback(err,null);
          } else {
            console.log(" ####### SUCESS ######## "+JSON.stringify(info));
            callback(err,info.insertId);
          }
      });
}