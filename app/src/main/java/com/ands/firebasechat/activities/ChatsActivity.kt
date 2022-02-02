package com.ands.firebasechat.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.firebasechat.MainActivity
import com.ands.firebasechat.R
import com.ands.firebasechat.data.adapters.ChatsAdapter
import com.ands.firebasechat.data.models.ChatInfo
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.ActivityChatsBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatsBinding.inflate(layoutInflater).also { setContentView(it.root) }

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

        val database = Firebase.database
        val reference = database.getReference("users")

        defaultReference.orderByChild("/lastMessageTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshotChats: DataSnapshot) {

                if (snapshotChats.childrenCount.toInt() == 0) {
                    showHide(binding.noChatsText, true)
                } else {

                    showHide(binding.noChatsText, false)
                    val list = ArrayList<ChatInfo>()

                    for (s in snapshotChats.children) {

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
                Toast.makeText(this@ChatsActivity, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showHide(view: View, visibility: Boolean) {
        view.visibility = if (visibility) {
            View.VISIBLE
        } else {
            View.GONE
        }
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