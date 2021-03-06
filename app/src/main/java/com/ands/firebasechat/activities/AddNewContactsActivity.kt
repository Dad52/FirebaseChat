package com.ands.firebasechat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.firebasechat.R
import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.adapters.NewContactsAdapter
import com.ands.firebasechat.data.models.ChatInfo
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.ActivityAddNewCorrespBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddNewContactsActivity : AppCompatActivity(), NewContactsAdapter.AddNewContactListener {

    private lateinit var binding: ActivityAddNewCorrespBinding
    lateinit var auth: FirebaseAuth
    private lateinit var adapter: NewContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewCorrespBinding.inflate(layoutInflater).also { setContentView(it.root) }

        val database = Firebase.database
        auth = Firebase.auth

        val newContactsReference = database.getReference("users")
        val chatsInfoReference = database.getReference("chatsInfo").child(auth.currentUser?.uid.toString())

        initRcView()
        onChangeNewContacts(newContactsReference, getAllUsersChats(chatsInfoReference))

    }

    private fun onChangeNewContacts(contactsReference: DatabaseReference, allUsersChats: ArrayList<String>) {

        contactsReference.addValueEventListener(object : ValueEventListener {

            val list = ArrayList<Users>()

            override fun onDataChange(snapshot: DataSnapshot) {
                for (contacts in snapshot.children) {
                    val contact = contacts.getValue(Users::class.java)

                    if (contact != null && contact.uid != auth.currentUser?.uid && contact.uid !in allUsersChats) {
                        list.add(contact)
                    }
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AddNewContactsActivity, "???????????? ???? ????????????, ???????????????????? ??????????", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //?????????????????????? ?? ???????????? ?????????? ?????? ??????
    private fun getAllUsersChats(chatsInfoReference: DatabaseReference): ArrayList<String> {

        val chatsList = ArrayList<String>()

        chatsInfoReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (chats in snapshot.children) {
                    val chat = chats.getValue(ChatInfo::class.java)
                    if (chat != null) {
                        chatsList.add(chats.key.toString())
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        return chatsList
    }

    private fun initRcView() {
        adapter = NewContactsAdapter(this)
        binding.rcNewUsers.adapter = adapter
        binding.rcNewUsers.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun addNewContact(user: Users) {

        //?????????????? ???????????????? ???? ????, ?????????????? ???? ?????????????? ?????????? ?? ?????? ?????? ???????????? ?????????????? ?????????? ???? ????????????

            val database = Firebase.database

            database.getReference("chatsInfo")
                    .child(auth.currentUser?.uid.toString())
                    .child(user.uid.toString())
                    .setValue(ChatInfo(
                            user.name.toString(),
                            "*?????????????????? ??????*",
                            GetCurrentTime.getCurrentDateInMillis(),
                            auth.currentUser?.uid.toString()
                    ))
            database.getReference("chatsInfo")
                    .child(user.uid.toString())
                    .child(auth.currentUser?.uid.toString())
                    .setValue(ChatInfo(
                            auth.currentUser?.displayName.toString(),
                            "*?????????????????? ??????*",
                            GetCurrentTime.getCurrentDateInMillis(),
                            auth.currentUser?.uid.toString()
                    ))
    finish()
    }

}