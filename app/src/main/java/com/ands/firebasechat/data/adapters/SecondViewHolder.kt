package com.ands.firebasechat.data.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ands.firebasechat.data.models.Messages
import com.ands.firebasechat.databinding.UserListItemBinding
import com.ands.firebasechat.databinding.UserListRightItemBinding

sealed class SecondViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    class LeftViewHolder(private val binding: UserListItemBinding): SecondViewHolder(binding) {
        fun bind(messages: Messages) = with(binding) {
            messageText.text = messages.message
            username.text = messages.user
        }
    }

    class RightViewHolder(private val binding: UserListRightItemBinding): SecondViewHolder(binding) {
        fun bind(messages: Messages) = with(binding) {
            messageText.text = messages.message
            username.text = messages.user
        }
    }

}