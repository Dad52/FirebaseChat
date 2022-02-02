package com.ands.firebasechat


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ands.firebasechat.activities.ChatsActivity
import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.adapters.MessagesAdapter
import com.ands.firebasechat.data.adapters.SecondViewHolder
import com.ands.firebasechat.data.models.ChatInfo
import com.ands.firebasechat.data.models.Messages
import com.ands.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SecondViewHolder.LongClickMessages {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var friendUid: String
    private lateinit var adapter: MessagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        auth = Firebase.auth
        val database = Firebase.database


        val chatInfo: ChatInfo = intent.extras?.get(ChatsActivity.USER_INFO) as ChatInfo

        friendUid = chatInfo.usersInfo?.uid!!


        val correspondenceReference = database.getReference("correspondence")

        val userMessagesReference = correspondenceReference
            .child(auth.currentUser?.uid.toString())
            .child(friendUid)

        val friendMessagesReference = correspondenceReference
            .child(friendUid)
            .child(auth.currentUser?.uid.toString())

        val userChatInfoReference = database
                .getReference("chatsInfo")
                .child(auth.currentUser?.uid.toString())
                .child(friendUid)

        val friendChatInfoReference = database
                .getReference("chatsInfo")
                .child(friendUid)
                .child(auth.currentUser?.uid.toString())


        binding.btnSend.setOnClickListener() {

//            val uniqueMessageId = getUniqueMessageId(timeInMillis)

            val uniqueMessageId = userMessagesReference.push().key!!

            sendMessage(userMessagesReference, uniqueMessageId, userChatInfoReference)
            sendMessage(friendMessagesReference, uniqueMessageId, friendChatInfoReference)
            binding.messageEditText.text.clear()
        }

        onChangeListener(userMessagesReference)
        setUpActionBar(chatInfo)
        initRcView()
    }

    private fun sendMessage(reference: DatabaseReference, uniqueMessageId: String, chatInfoReference: DatabaseReference) {
        reference.child(uniqueMessageId).setValue(getMessage())

        chatInfoReference.child("lastMessage").setValue(binding.messageEditText.text.toString())
        chatInfoReference.child("lastMessageTime").setValue(GetCurrentTime.getCurrentDateInMillis())
        chatInfoReference.child("lastMessageUid").setValue(auth.currentUser?.uid.toString())

    }

    private fun getMessage(): Messages {
        return Messages(
            auth.currentUser?.displayName.toString(),
            auth.currentUser?.uid.toString(),
            binding.messageEditText.text.toString(),
            GetCurrentTime.getCurrentDateInMillis()
        )
    }

//    private fun getUniqueMessageId(timeInMillis: String): String {
//        val text  = auth.currentUser?.displayName.toString()
//        val bytes = text.toByteArray()
//        val md = MessageDigest.getInstance("SHA-1")
//        val digest = md.digest(bytes)
//        val uniqueId: String = ""
//        for (byte in digest) {uniqueId + "%02x".format(byte)}
//        return uniqueId
//    }

    private fun onChangeListener(myRef: DatabaseReference) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.apply {
                    val list = ArrayList<Messages>()
                    for(s in snapshot.children) {
                        val record = s.getValue(Messages::class.java)

                        if (record?.message != null) {

                            record.messageId = s.key
                            Log.e("TAG", s.key!!)

                            list.add(record)
                        }

                    }
                    list.reverse()

                    adapter.submitList(list)
                    if (rcView.adapter!!.itemCount != 0) {
                        rcView.smoothScrollToPosition(0)

                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun initRcView() = with(binding) {
        adapter = MessagesAdapter(auth.currentUser?.uid.toString(), this@MainActivity)
        rcView.adapter = adapter
        val layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, true)
        layoutManager.stackFromEnd = true
        rcView.layoutManager = layoutManager

    }


    private fun setUpActionBar(chatInfo: ChatInfo) {

        Picasso.with(binding.root.context).load(chatInfo.usersInfo?.photoUrl).into(binding.userIconTitle)
        binding.userNameTitle.text = chatInfo.displayName
        binding.backBtnTitle.setOnClickListener() {
            finish()
        }

    }

    override fun longClickDeleteMsg(msgId: String) {

        val database = Firebase.database

        database.getReference("correspondence")
            .child(auth.currentUser?.uid.toString())
            .child(friendUid)
            .child(msgId).removeValue()

        database.getReference("correspondence")
            .child(friendUid)
            .child(auth.currentUser?.uid.toString())
            .child(msgId).removeValue()
    }

}