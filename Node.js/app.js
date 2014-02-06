var fs = require('fs');
var http = require('http');
var express = require('express');
var app = express();
var sql_model = require('./sql_helper');
var url = require('url');
var server = http.createServer(app);

server.listen(3000);

app.configure(function(){
  app.use(express.bodyParser());
  app.use(express.methodOverride());
  app.use(express.static(__dirname + '/'));
});


app.post('/device', function(req, res) {

  // Store the user details.
  console.log(" ######### POST /device ########### "+req.body.deviceId+" : "+req.body.os);
  var device = {
    deviceid:req.body.deviceId, os: req.body.os
  };

  //Add to sql_model.
  sql_model.add_device(device,function(err,result) {
    if( err) {
        res.send(JSON.stringify(err));
    } else {
    	res.json({"id":result});
    }
  });
});


app.post('/account', function(req, res) {

	// Store the user details.
	console.log(" ######### POST /account ########### "+req.body.deviceId+" : "+req.body.accounts);

	for(var i = 0; i < req.body.accounts.length; ++i) {
		var name;
		var type;
	   for(key in req.body.accounts[i]) {
	  		if( key == "name") {
	  			name = req.body.accounts[i][key];
	  		} if ( key == "type") {
	  			type = req.body.accounts[i][key];
	  		} 
	  		console.log("######## Key Value ###########"+key +" : "+ req.body.accounts[i][key]);
		}  	
	  	var account = {
			deviceid:req.body.deviceId, name:name, type:type 
		};
		
		// Add to sql_model.
		sql_model.add_account(account,function(err,result) {
		    console.log(" ######### add user ####### "+ result);
		    if( err) {
		        res.send(JSON.stringify(err));
		    } else {
		    	res.json({"id":result});
		    }
		}); 
	} 
  
});


app.post('/appsinstalled', function(req, res) {

	// Store the user details.
	console.log(" ######### POST /account ########### "+req.body.deviceId+" : "+req.body.installed);

	for(var i = 0; i < req.body.installed.length; ++i) {
		var name;
		var packageName;
		var icon="";
	   for(key in req.body.installed[i]) {
	  		if( key == "package") {
	  			packageName = req.body.installed[i][key];
	  		} if ( key == "name") {
	  			name = req.body.installed[i][key];
	  		} if( key =="appicon") {
	  			icon = req.body.installed[i][key];
	  		}
	  		console.log("######## Key Value ###########"+key +" : "+ req.body.installed[i][key]);
		}  	

	  	var appinstalled = {
			deviceid:req.body.deviceId, name:name, pkg:packageName, icon:icon
		};
		
		// Add to sql_model.
		sql_model.add_appsinstalled(appinstalled,function(err,result) {
		    console.log(" ######### add user ####### "+ result);
		    if( err) {
		        res.send(JSON.stringify(err));
		    } else {
		    	
		    }
		}); 
	} 
  
  res.json({});
});


app.post('/contacts', function(req, res) {

	// Store the user details.
	console.log(" ######### POST /account ########### "+req.body.deviceId+" : "+req.body.contacts);

	for(var i = 0; i < req.body.contacts.length; ++i) {
		var name="";
		var email="";
		var phnumber="";
		var type="";
		var skype_id="";
		var contact_id="";
	   for(key in req.body.contacts[i]) {
	  		if( key == "name") {
	  			name = req.body.contacts[i][key];
	  		} else if( key == "email") {
	  			email = req.body.contacts[i][key];
	  		} else if ( key == "phonenumber") {
	  			phnumber = req.body.contacts[i][key];
	  		} else if ( key == "type") {
	  			type = req.body.contacts[i][key];
	  		} else if ( key == "imname") {
	  			skype_id = req.body.contacts[i][key];
	  		} else if ( key == "contact_id") {
	  			contact_id = req.body.contacts[i][key];
	  		}
	  		console.log("######## Key Value ###########"+key +" : "+ req.body.contacts[i][key]);
		}  	

	  	var contact = {
			deviceid:req.body.deviceId, name:name, email:email, ph:phnumber, type:type, skypeid:skype_id, contact_id:contact_id
		};
		
		// Add to sql_model.
		sql_model.add_contact(contact,function(err,result) {
		    console.log(" ######### add user ####### "+ result);
		    if( err) {
		        res.send(JSON.stringify(err));
		    } else {
		    	
		    }
		}); 
	} 

	res.json({});
});

app.post('/fb_contacts', function(req, res) {

	// Store the user details.
	console.log(" ######### POST /fb_contacts ########### "+req.body.deviceId+" : "+req.body.contacts);

	for(var i = 0; i < req.body.contacts.length; ++i) {
		var name="";
		var facebookId="";
	   for(key in req.body.contacts[i]) {
	  		if( key == "name") {
	  			name = req.body.contacts[i][key];
	  		} else if( key == "facebookId") {
	  			email = req.body.contacts[i][key];
	  		} 
	  		console.log("######## Key Value ###########"+key +" : "+ req.body.contacts[i][key]);
		}  	

	  	var contact = {
			deviceid:req.body.deviceId, name:name, facebookId:facebookId
		};
		
		// Add to sql_model.
		sql_model.add_facebook_contact(contact,function(err,result) {
		    console.log(" ######### add user ####### "+ result);
		    if( err) {
		        res.send(JSON.stringify(err));
		    } else {
		    	
		    }
		}); 
	} 

	res.json({});
});

app.get('/fetch_social_ad', function(req, res) {

	var queryData = url.parse(req.url, true).query;

	if (queryData.deviceId) {
		console.log(" ######### GET /fetchAd ########### "+queryData.deviceId);
	}
	
	sql_model.getAppRecommendation(queryData.deviceId,function(err,result) {
	    console.log(" ######### getAppRecommendation ####### "+ result);
	    if( err) {
	        res.send(JSON.stringify(err));
	    } else {
			res.json(result);    	
	    }		
	});
	
});
