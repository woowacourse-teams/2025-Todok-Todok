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
            is NotificationGroup.Count -> R.layout.item_notification_count
            is NotificationGroup.Notification -> R.layout.item_notification
            is NotificationGroup.Information -> R.layout.item_notification_information
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_notification_count -> NotificationCountViewHolder.Companion.NotificationCountViewHolder(
                parent
            )

            R.layout.item_notification -> NotificationViewHolder(parent, updateUnreadStatus)
            R.layout.item_notification_information -> NotificationInformationViewHolder(parent)

            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is NotificationGroup.Count -> (holder as NotificationCountViewHolder).bind(item.count)
            is NotificationGroup.Notification -> (holder as NotificationViewHolder).bind(item.notification)
            is NotificationGroup.Information -> (holder as NotificationInformationViewHolder)
        }
    }
}
