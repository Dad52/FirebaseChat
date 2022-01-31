package com.ands.firebasechat.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.firebasechat.MainActivity
import com.ands.firebasechat.R
import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.adapters.CorrespAdapter
import com.ands.firebasechat.data.models.Corresps
import com.ands.firebasechat.data.models.Messages
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.ActivityCorrespBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CorrespActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCorrespBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: CorrespAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrespBinding.inflate(layoutInflater).also { setContentView(it.root) }

        auth = Firebase.auth

        val database = Firebase.database

        val defaultReference = database.getReference("correspondence").child(auth.currentUser!!.uid)

        initRcView()
        onChangeCorrespondence(defaultReference)

    }

    private fun getNewContact() {
        if (auth.currentUser?.uid.toString() != "CtX7DZXHpHNrAz1ITHe21XHkKBJ2") {

            val database = Firebase.database

            val correspondenceReference = database.getReference("correspondence")

            val userFriendReference = correspondenceReference.child(auth.currentUser?.uid.toString())
            val userMsgsReference = userFriendReference.child("CtX7DZXHpHNrAz1ITHe21XHkKBJ2").child("chatInfo")

            val friendReference = correspondenceReference.child("CtX7DZXHpHNrAz1ITHe21XHkKBJ2")
            val friendMsgsReference = friendReference.child(auth.currentUser?.uid.toString()).child("chatInfo")

            userMsgsReference.child("lastMessage").setValue("Напишите что-нибудь..")
            userMsgsReference.child("time").setValue(GetCurrentTime.getCurrentDateInMillis())
            userMsgsReference.child("userName").setValue("Андрюша")
            userMsgsReference.child("userUid").setValue("CtX7DZXHpHNrAz1ITHe21XHkKBJ2")
            userMsgsReference.child("photoUrl").setValue("https://lh3.googleusercontent.com/a-/AOh14GhkJYr7dMpLuG1rTdf6HjmUvedPTaBlqbbnjEVVbQ=s96-c")

            friendMsgsReference.child("lastMessage").setValue("Напишите что-нибудь..")
            friendMsgsReference.child("time").setValue(GetCurrentTime.getCurrentDateInMillis())
            friendMsgsReference.child("userName").setValue(auth.currentUser?.displayName.toString())
            friendMsgsReference.child("userUid").setValue(auth.currentUser?.uid.toString())
            friendMsgsReference.child("photoUrl").setValue(auth.currentUser?.photoUrl.toString())

        }
    }

    private fun initRcView() = with(binding) {
        adapter = CorrespAdapter(object: CorrespAdapter.ClickItem{
            override fun onClickItem(uid: String, name: String, photoUrl: String) {
                val i = Intent(this@CorrespActivity, MainActivity::class.java).apply {
                    putExtra(USER_UID, uid)
                    putExtra(USER_NAME, name)
                    putExtra(USER_PHOTO_URL, photoUrl)
                }
                startActivity(i)
            }

        })
        correspsRcView.adapter = adapter
        correspsRcView.layoutManager = LinearLayoutManager(this@CorrespActivity)
    }

    private fun onChangeCorrespondence(defaultReference: DatabaseReference) {

        defaultReference.orderByChild("chatInfo/time").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount.toInt() == 0) {
                    Toast.makeText(this@CorrespActivity, "Добавляю Андрюшу...", Toast.LENGTH_SHORT).show()
                    getNewContact()
                } else {

                    val list = ArrayList<Corresps>()
                    for (s in snapshot.children) {

                        val newItem = snapshot.child(s.key.toString()).child("chatInfo").getValue(Corresps::class.java)
                        list.add(newItem!!)

                    }

                    list.reverse()
                    adapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOutMenu) {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val USER_UID = "user_uid"
        const val USER_NAME = "user_name"
        const val USER_PHOTO_URL = "user_photo_url"
    }

}