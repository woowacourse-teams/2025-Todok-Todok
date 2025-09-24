package com.team.todoktodok.presentation.xml.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemNotificationCountBinding

class NotificationCountViewHolder(
    private val binding: ItemNotificationCountBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(count: Int) {
        binding.tvCount.text = count.toString()
    }

    companion object {
        fun NotificationCountViewHolder(parent: ViewGroup): NotificationCountViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationCountBinding.inflate(layoutInflater, parent, false)
            return NotificationCountViewHolder(binding)
        }
    }
}