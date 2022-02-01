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
import com.ands.firebasechat.data.adapters.ChatsAdapter
import com.ands.firebasechat.data.models.ChatInfo
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.ActivityCorrespBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCorrespBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCorrespBinding.inflate(layoutInflater).also { setContentView(it.root) }

        auth = Firebase.auth

        val database = Firebase.database

        val defaultReference = database.getReference("chatsInfo").child(auth.currentUser!!.uid)

        binding.addNewCorresp.setOnClickListener() {
            val intent = Intent(this, AddNewContactsActivity::class.java)
            startActivity(intent)
        }

        initRcView()
        onChangeCorrespondence(defaultReference)

    }

    private fun restoreAllData() {
        val database = Firebase.database

        val chatsInfoRef = database.getReference("chatsInfo").child(auth.currentUser!!.uid)

        database.getReference("users").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (s in snapshot.children) {

                    if (s.key.toString() != auth.currentUser!!.uid) {

                        val friendBranch = chatsInfoRef.child(s.key.toString())
                        val chatsInfo = s.getValue(ChatInfo::class.java)
//                        friendBranch.child("userUid").setValue(users?.uid.toString())
//                        friendBranch.setValue(chatsInfo)
//                        Log.e("Tag", users?.photoUrl.toString())

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
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
        adapter = ChatsAdapter(object: ChatsAdapter.ClickItem{
            override fun onClickItem(chatInfo: ChatInfo) {
                val i = Intent(this@ChatsActivity, MainActivity::class.java).apply {
                    putExtra(USER_INFO, chatInfo)
                }
                startActivity(i)
            }

        }, auth.currentUser?.uid.toString())
        correspsRcView.adapter = adapter
        correspsRcView.layoutManager = LinearLayoutManager(this@ChatsActivity)
    }

    private fun onChangeCorrespondence(defaultReference: DatabaseReference) {

        defaultReference.orderByChild("/lastMessageTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshotChats: DataSnapshot) {
                if (snapshotChats.childrenCount.toInt() == 0) {
                    Toast.makeText(this@ChatsActivity, "Добавляю Андрюшу...", Toast.LENGTH_SHORT).show()
//                    getNewContact()
                } else {

                    val list = ArrayList<ChatInfo>()

                    for (s in snapshotChats.children) {

                        val database = Firebase.database
                        val reference = database.getReference("users")


                        reference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshotUser: DataSnapshot) {

                                val newChat = s.getValue(ChatInfo::class.java)
                                for (user in snapshotUser.children) {
                                    val userData = user.getValue(Users::class.java)
                                    if (userData?.uid.toString() == s.key) {
                                        newChat?.usersInfo = userData
                                        list.add(newChat!!)
                                    }
                                }

                                adapter.submitList(list.reversed())
                            }

                            override fun onCancelled(errorUser: DatabaseError) {
                                Toast.makeText(this@ChatsActivity, "Error", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOutMenu) {
            auth.signOut()
            GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build()
            ).signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        const val USER_INFO = "user_info"
    }

}