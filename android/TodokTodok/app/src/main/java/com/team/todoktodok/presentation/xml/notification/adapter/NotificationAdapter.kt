package com.team.todoktodok.presentation.xml.notification.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationInformationViewHolder.Companion.NotificationInformationViewHolder
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationViewHolder.Companion.NotificationViewHolder

class NotificationAdapter(
    val updateUnreadStatus: NotificationViewHolder.UpdateUnreadStatusClickListener,
) :
    androidx.recyclerview.widget.ListAdapter<NotificationGroup, RecyclerView.ViewHolder>(
        NotificationDiffUtil(),
    ) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is NotificationGroup.Information -> R.layout.item_notification_information
            is NotificationGroup.Notification -> R.layout.item_notification
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_notification_information -> NotificationInformationViewHolder(parent)
            R.layout.item_notification -> NotificationViewHolder(parent, updateUnreadStatus)
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is NotificationGroup.Information ->
                (holder as NotificationInformationViewHolder)

            is NotificationGroup.Notification -> (holder as NotificationViewHolder).bind(item.notification)
        }
    }
}
