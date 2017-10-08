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
  // return event.data.ref.parent.child('test').set(place.lat);
  console.log("Querying geofire with coords " + place.lat + ", " + place.long);
  var geoQuery = geoFire.query({
    center: [place.lat, place.long],
    radius: 100
  });

  var onKeyEnteredRegistration = geoQuery.on("key_entered", function(key, location){
    admin.database().ref('/users/' + key).once('value').then(function(snapshot) {
      var userToken = (snapshot.val().fcm_token);
      const payload = {
        notification: {
          title: "Test notification",
          body: "Test"
        }};
        console.log('notifying ' + key + ' with token ' + userToken);
        admin.messaging().sendToDevice(userToken, payload)
        .then(function(response){
          console.log("Successfully sent message: ", response);
        })
        .catch(function (error){
          console.log("Error sending message: ", error);
        })
      });
    });

    var onReadyRegistration = geoQuery.on("ready", function(){
      console.log("ready event fired, cancelling query.");
      geoQuery.cancel();
    });
  });
