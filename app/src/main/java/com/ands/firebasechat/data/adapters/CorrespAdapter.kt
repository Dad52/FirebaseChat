package com.ands.firebasechat.data.adapters


import android.graphics.drawable.BitmapDrawable
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ands.firebasechat.R
import com.ands.firebasechat.data.GetCurrentTime
import com.ands.firebasechat.data.models.Corresps
import com.ands.firebasechat.databinding.CorrespondenceItemBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class CorrespAdapter(private val listener: ClickItem): ListAdapter<Corresps, CorrespAdapter.ItemHolder>(ItemComparator()) {

    class ItemHolder(private val binding: CorrespondenceItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(corresps: Corresps, listener: ClickItem) = with(binding) {
            userNameText.text = corresps.userName
            lastMessageText.text = corresps.lastMessage

            timeMsg.text = GetCurrentTime.getCurrentTimeString(corresps.time!!.toLong())

            Picasso.with(binding.root.context).load(corresps.photoUrl).into(userIcon)
            itemView.setOnClickListener() {
                listener.onClickItem(corresps.userUid!!, corresps.userName!!, corresps.photoUrl!!)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view = CorrespondenceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class ItemComparator() : DiffUtil.ItemCallback<Corresps>(){
        override fun areItemsTheSame(oldItem: Corresps, newItem: Corresps): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Corresps, newItem: Corresps): Boolean {
            return oldItem == newItem
        }

    }

    interface ClickItem {
        fun onClickItem(uid: String, name: String, photoUrl: String)
    }

}