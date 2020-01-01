package com.urgentx.recycledump.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import java.util.*
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.urgentx.recycledump.BuildConfig
import com.urgentx.recycledump.R
import com.urgentx.recycledump.util.firebase.MyFirebaseInstanceIdService


class LoginActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            sendTokenToServer()
            // already signed in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn.setOnClickListener{signIn()}
    }

    private fun signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(AuthUI.IdpConfig.EmailBuilder().build(),
                                        AuthUI.IdpConfig.GoogleBuilder().build(),
                                        AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            // Successfully signed in
            if (response != null && response.error == null) {
                sendTokenToServer()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                return
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, R.string.sign_in_cancelled, LENGTH_LONG).show()
                    return
                }

                if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, R.string.no_internet_connection, LENGTH_LONG).show()
                    return
                }

                if (response.error?.errorCode == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, R.string.unknown_error, LENGTH_LONG).show()
                    return
                }
            }
            Toast.makeText(this, R.string.unknown_error, LENGTH_LONG).show()
        }
    }

    private fun sendTokenToServer() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        FirebaseAuth.getInstance().currentUser?.let{
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(it.uid)
            userRef.child("fcm_token").setValue(refreshedToken)
        }
    }
}