package com.ands.firebasechat

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ands.firebasechat.databinding.UserListItemBinding
import com.ands.firebasechat.databinding.UserListRightItemBinding

class UserAdapter(private val currentUserName: String): ListAdapter<User, SecondViewHolder>(ItemComparator()) {


    class ItemComparator: DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
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
                    )
            )
            R.layout.user_list_item -> SecondViewHolder.LeftViewHolder(
                    UserListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                    )
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
        when (getItem(position).name) {
            currentUserName -> return R.layout.user_list_right_item
            else -> return R.layout.user_list_item
        }

    }


}