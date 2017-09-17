// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.newPlaceAdded = functions.database.ref('/places/{pushId}')
    .onWrite(event => {
      // Grab the current value of what was written to the Realtime Database.
      const place = event.data.val();
      return event.data.ref.parent.child('test').set(place.lat);
    });
