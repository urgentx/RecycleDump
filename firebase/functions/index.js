// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');
const GeoFire = require('geofire');
// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.newPlaceAdded = functions.database.ref('/places/{pushId}')
.onWrite(event => {
  // Grab the current value of what was written to the Realtime Database.
  var geoFire = new GeoFire(admin.database().ref('/userlocations'));
  const place = event.data.val();
  var placeCategories = place.categories;
  // return event.data.ref.parent.child('test').set(place.lat);
  console.log("Querying geofire with coords " + place.lat + ", " + place.long);
  var geoQuery = geoFire.query({
    center: [place.lat, place.long],
    radius: 100
  });

  var onKeyEnteredRegistration = geoQuery.on("key_entered", function(key, location){
    admin.database().ref('/users/' + key).once('value').then(function(snapshot) {
      var items = [];
      var itemsProcessed = 0;
      var numItems = snapshot.child('items').numChildren();
      var userToken = (snapshot.val().fcm_token);
      snapshot.child('items').forEach(function (itemSnapshot) {
        if(placeCategories.indexOf(itemSnapshot.val()) > -1 ) {
          console.log("Querying db at items/" + itemSnapshot.key);
          admin.database().ref('/items/' + itemSnapshot.key).once('value').then(function(itemDetailSnapshot) {
            items.push(itemDetailSnapshot.val().name);
            itemsProcessed++;
            if(itemsProcessed >= numItems){
              sendNotification(userToken, items);
            }
          });
        }
      });
    });
  });

  var onReadyRegistration = geoQuery.on("ready", function(){
    console.log("ready event fired, cancelling query.");
    geoQuery.cancel();
  });
});

function sendNotification(token, items) {
  console.log("items length: " + items.length);
  var notificationSuffix = ""
  for (var i = 0; i < items.length; i++) {
    console.log("item is: " + items[i]);
    notificationSuffix = notificationSuffix + items[i];
    if (i < items.length - 1) {
      notificationSuffix = notificationSuffix + ","
    }
  }

  const payload = {
    notification: {
      title: "Good news!",
      body: "We've found a disposal place for your " + notificationSuffix + "."
    }};
    console.log('notifying token ' + token);
    admin.messaging().sendToDevice(token, payload)
    .then(function(response){
      console.log("Successfully sent message: ", response);
    })
    .catch(function (error){
      console.log("Error sending message: ", error);
    });
  }
