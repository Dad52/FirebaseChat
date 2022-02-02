package com.ands.firebasechat.data.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ands.firebasechat.data.models.Users
import com.ands.firebasechat.databinding.NewContactItemBinding
import com.squareup.picasso.Picasso

class NewContactsAdapter(private val addNewContactListener: AddNewContactListener): ListAdapter<Users, NewContactsAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: NewContactItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(contact: Users, addContactListener: AddNewContactListener) = with(binding) {

//            contactEmailText.text = contact.email
            contactEmailText.text = "testEmail@gmail.com"
            contactNameText.text = contact.name

            Picasso.with(binding.root.context).load(contact.photoUrl).into(contactIcon)

            itemView.setOnClickListener() {
                addContactListener.addNewContact(contact)
            }

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
        holder.bind(getItem(position), addNewContactListener)
    }

    interface AddNewContactListener {
        fun addNewContact(user: Users)
    }

}