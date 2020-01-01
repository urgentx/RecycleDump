package com.urgentx.recycledump.util.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseInstanceIdService: FirebaseMessagingService() {

    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d("notifications", "Refreshed token: $refreshedToken")
        sendRegistrationToServer(refreshedToken)
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        FirebaseAuth.getInstance().currentUser?.let{
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(it.uid)
            userRef.child("fcm_token").setValue(refreshedToken)
        }
    }
}