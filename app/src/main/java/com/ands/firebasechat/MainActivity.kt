package com.ands.firebasechat


import android.graphics.drawable.BitmapDrawable
import android.icu.lang.UCharacter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
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
import java.lang.reflect.Array.get


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        auth = Firebase.auth

        val database = Firebase.database
        val myRef = database.getReference("message")
        binding.btnSend.setOnClickListener() {
            myRef.child(myRef.push().key ?: "bladasda").setValue(User(auth.currentUser?.displayName, binding.messageEditText.text.toString()))
        }
        onChangeListener(myRef)
        setUpActionBar()
        initRcView()
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    val list = ArrayList<User>()
                    for(s in snapshot.children) {
                        val user = s.getValue(User::class.java)
                        if (user != null)  list.add(user)

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
        adapter = UserAdapter(auth.currentUser?.displayName.toString())
        rcView.adapter = adapter
        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, true)
        rcView.layoutManager = layoutManager

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.signOutMenu) {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpActionBar() {
        val actionBar = supportActionBar
        Thread {
            val bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val dIcon = BitmapDrawable(resources, bMap)
            runOnUiThread {
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(dIcon)
                actionBar?.title = auth.currentUser?.displayName
            }

        }.start()

    }

}