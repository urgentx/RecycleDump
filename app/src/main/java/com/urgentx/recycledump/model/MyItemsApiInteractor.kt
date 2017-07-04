package com.urgentx.recycledump.model

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.urgentx.recycledump.model.callbacks.MyItemsCallback
import com.urgentx.recycledump.util.Item


class MyItemsApiInteractor {

    private val database = FirebaseDatabase.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser


    fun getItems(callback: MyItemsCallback) {
        val usersReference = database.getReference("users")

        val uid = user!!.uid

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val itemNames = ArrayList<String>()
                for(itemSnapshot in dataSnapshot.children){
                    var itemName = itemSnapshot.key
                    Log.d("Firebase Database", "Item name is: $itemName")
                    itemNames.add(itemName)
                }
                retrieveItems(itemNames, callback)
            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        }
        usersReference.child(uid).child("items").addListenerForSingleValueEvent(postListener)
    }

    private fun  retrieveItems(itemNames: ArrayList<String>, callback: MyItemsCallback) {
        val itemsReference = database.getReference("items")

        val items = ArrayList<Item>()

        var i = 0

        for(itemName in itemNames){
            val itemListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var item = dataSnapshot.getValue(Item::class.java)
                    if (item != null) {
                        Log.d("Firebase", "Item weight: ${item?.weight}")
                        items.add(item)
                    }
                    if(i >= itemNames.size - 1){
                        callback.itemsRetrieved(items)
                    } else {
                        i++
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {


                }
            }
            itemsReference.child(itemName).addListenerForSingleValueEvent(itemListener)
        }

    }
}