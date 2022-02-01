package com.ands.firebasechat.data.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.NewContactItemBinding
import com.squareup.picasso.Picasso

class NewContactsAdapter: ListAdapter<Users, NewContactsAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: NewContactItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Users) = with(binding) {

            contactEmailText.text = contact.email
            contactNameText.text = contact.name

            Picasso.with(binding.root.context).load(contact.photoUrl).into(contactIcon)
        }

    }

    class ItemComparator: DiffUtil.ItemCallback<Users>(){
        override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = NewContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

}