package com.ands.firebasechat.data.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.models.Messages
import com.ands.firebasechat.databinding.UserListItemBinding
import com.ands.firebasechat.databinding.UserListRightItemBinding

sealed class SecondViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    class LeftViewHolder(private val binding: UserListItemBinding, private val longClickListener: LongClickMessages): SecondViewHolder(binding) {
        fun bind(messages: Messages) = with(binding) {

            messageText.text = messages.message
            username.text = messages.user

            if (messages.time == null) {
                messageTimeLeft.text = "00:00"
            } else {
                messageTimeLeft.text = GetCurrentTime.getCurrentTimeString(messages.time.toLong())
            }

            itemView.setOnLongClickListener {
                longClickListener.longClickDeleteMsg(messages.messageId.toString())
                return@setOnLongClickListener true
            }

        }
    }

    class RightViewHolder(private val binding: UserListRightItemBinding, private val longClickListener: LongClickMessages): SecondViewHolder(binding) {
        fun bind(messages: Messages) = with(binding) {
            messageText.text = messages.message
            username.text = messages.user

            if (messages.time == null) {
                messageTimeRight.text = "00:00"
            } else {
                messageTimeRight.text = GetCurrentTime.getCurrentTimeString(messages.time.toLong())
            }

            itemView.setOnLongClickListener {
                longClickListener.longClickDeleteMsg(messages.messageId.toString())
                return@setOnLongClickListener true
            }

        }
    }

    interface  LongClickMessages{
        fun longClickDeleteMsg(msgId: String)
    }

}