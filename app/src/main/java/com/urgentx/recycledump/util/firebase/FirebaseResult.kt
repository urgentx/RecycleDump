package com.urgentx.recycledump.util.firebase

sealed class FirebaseResult {

    class Success(val result: Any) : FirebaseResult()
    class Error(val type: Int) : FirebaseResult()

    companion object {
        const val NETWORK_ERROR = 0
        const val STRUCTURAL_ERROR = 1
        const val AUTH_ERROR = 2
    }

}