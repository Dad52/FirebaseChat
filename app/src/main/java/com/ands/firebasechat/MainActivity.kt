package com.ands.firebasechat


import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.firebasechat.activities.CorrespActivity
import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.adapters.UserAdapter
import com.ands.firebasechat.data.models.Corresps
import com.ands.firebasechat.data.models.Messages
import com.ands.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        auth = Firebase.auth
        val database = Firebase.database

        val friendUid = intent.extras?.get(CorrespActivity.USER_UID)
        val friendName = intent.extras?.get(CorrespActivity.USER_NAME).toString()

        val correspondenceReference = database.getReference("correspondence")

        val userFriendReference = correspondenceReference.child(auth.currentUser?.uid.toString())
        val userMsgsReference = userFriendReference.child(friendUid.toString())

        val friendReference = correspondenceReference.child(friendUid.toString())
        val friendMsgsReference = friendReference.child(auth.currentUser?.uid.toString())

        binding.btnSend.setOnClickListener() {
            makeMessage(userMsgsReference)
            makeMessage(friendMsgsReference)
            binding.messageEditText.text.clear()
        }



        onChangeListener(userMsgsReference)
        setUpActionBar(friendName)
        initRcView()
    }

    private fun makeMessage(reference: DatabaseReference) {
        reference.child(reference.push().key ?: "error").setValue(Messages(
            auth.currentUser?.displayName.toString(),
            auth.currentUser?.uid.toString(),
            binding.messageEditText.text.toString()
        ))
        reference.child("chatInfo").child("lastMessage").setValue(binding.messageEditText.text.toString())
        reference.child("chatInfo").child("time").setValue(GetCurrentTime.getCurrentTime())
    }

    private fun onChangeListener(myRef: DatabaseReference) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    val list = ArrayList<Messages>()
                    for(s in snapshot.children) {
                        val user = s.getValue(Messages::class.java)
                        if (user != null) list.add(user)

                    }
                    list.reverse()
                    adapter.submitList(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun initRcView() = with(binding) {
        adapter = UserAdapter(auth.currentUser?.uid.toString())
        rcView.adapter = adapter
        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, true)
        rcView.layoutManager = layoutManager

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpActionBar(friendName: String) {
        val actionBar = supportActionBar
        Thread {
            runOnUiThread {
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.title = friendName
            }

        }.start()

    }

}