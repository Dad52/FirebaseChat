package com.ands.firebasechat

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ands.firebasechat.databinding.UserListItemBinding
import com.ands.firebasechat.databinding.UserListRightItemBinding

sealed class SecondViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    class LeftViewHolder(private val binding: UserListItemBinding): SecondViewHolder(binding) {
        fun bind(user: User) = with(binding) {
            messageText.text = user.message
            username.text = user.name
        }
    }

    class RightViewHolder(private val binding: UserListRightItemBinding): SecondViewHolder(binding) {
        fun bind(user: User) = with(binding) {
            messageText.text = user.message
            username.text = user.name
        }
    }

}