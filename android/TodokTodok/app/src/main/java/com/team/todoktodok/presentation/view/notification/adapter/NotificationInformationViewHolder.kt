package com.team.todoktodok.presentation.view.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemNotificationInformationBinding

class NotificationInformationViewHolder(
    private val binding: ItemNotificationInformationBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun NotificationInformationViewHolder(parent: ViewGroup): NotificationInformationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationInformationBinding.inflate(layoutInflater, parent, false)
            return NotificationInformationViewHolder(binding)
        }
    }
}
