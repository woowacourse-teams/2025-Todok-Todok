package com.team.ui_xml.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.ui_xml.databinding.ItemNotificationCountBinding

class NotificationCountViewHolder private constructor(
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
