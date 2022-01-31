package com.ands.firebasechat.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ands.firebasechat.R
import com.ands.firebasechat.data.models.Messages
import com.ands.firebasechat.databinding.UserListItemBinding
import com.ands.firebasechat.databinding.UserListRightItemBinding

class MessagesAdapter(private val currentUserUid: String, private val longClickListener: SecondViewHolder.LongClickMessages): ListAdapter<Messages, SecondViewHolder>(ItemComparator()) {


    class ItemComparator: DiffUtil.ItemCallback<Messages>(){
        override fun areItemsTheSame(oldItem: Messages, newItem: Messages): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Messages, newItem: Messages): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondViewHolder {
        return when (viewType) {
            R.layout.user_list_right_item -> SecondViewHolder.RightViewHolder(
                    UserListRightItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    ), longClickListener
            )
            R.layout.user_list_item -> SecondViewHolder.LeftViewHolder(
                    UserListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    ), longClickListener
            )
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
        }

    }

    override fun onBindViewHolder(holder: SecondViewHolder, position: Int) {
        when(holder) {
            is SecondViewHolder.LeftViewHolder -> holder.bind(getItem(position))
            is SecondViewHolder.RightViewHolder -> holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (getItem(position).userUid) {
            currentUserUid -> return R.layout.user_list_right_item
            else -> return R.layout.user_list_item
        }

    }


}