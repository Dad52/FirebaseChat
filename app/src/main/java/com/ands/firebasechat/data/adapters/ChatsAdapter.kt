package com.ands.firebasechat.data.adapters



import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ands.firebasechat.R

import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.models.ChatInfo
import com.ands.firebasechat.databinding.CorrespondenceItemBinding
import com.squareup.picasso.Picasso

class ChatsAdapter(private val listener: ClickItem, private val currentUserUid: String): ListAdapter<ChatInfo, ChatsAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: CorrespondenceItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(chatInfo: ChatInfo, listener: ClickItem, currentUserUid: String) = with(binding) {

            userNameText.text = chatInfo.displayName
            if (currentUserUid == chatInfo.lastMessageUid.toString()) {
                lastMessageText.text = binding.root.context.getString(R.string.YouText, chatInfo.lastMessage)
            } else {
                lastMessageText.text = chatInfo.lastMessage
            }

            timeMsg.text = GetCurrentTime.getCurrentTimeString(chatInfo.lastMessageTime!!.toLong())

            Picasso.with(binding.root.context).load(chatInfo.usersInfo?.photoUrl).into(userIcon)

            itemView.setOnClickListener() {
                listener.onClickItem(chatInfo)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = CorrespondenceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), listener, currentUserUid = currentUserUid)
    }

    class ItemComparator() : DiffUtil.ItemCallback<ChatInfo>(){
        override fun areItemsTheSame(oldItem: ChatInfo, newItem: ChatInfo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChatInfo, newItem: ChatInfo): Boolean {
            return oldItem == newItem
        }

    }

    interface ClickItem {
        fun onClickItem(chatInfo: ChatInfo)
    }

}