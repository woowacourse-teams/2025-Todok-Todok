package com.team.todoktodok.presentation.view.profile.viewholder

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Support
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemUserInformationBinding
import com.team.todoktodok.databinding.PopupMenuReportBinding
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

class UserInformationViewHolder private constructor(
    private val binding: ItemUserInformationBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        with(binding) {
            ivProfile.setOnClickListener {
                handler.onClickProfileImage()
            }

            ivReport.setOnClickListener {
                showCustomPopupMenu(itemView.context, viewAnchor, binding.root)
            }
        }
    }

    private fun showCustomPopupMenu(
        context: Context,
        anchor: View,
        parent: ViewGroup,
    ) {
        val inflater = LayoutInflater.from(context)
        val binding = PopupMenuReportBinding.inflate(inflater, parent, false)

        val popupWindow =
            PopupWindow(
                binding.root,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true,
            )

        binding.tvReport.setOnClickListener {
            handler.onClickSupport(Support.REPORT)
            popupWindow.dismiss()
        }

        binding.tvBlock.setOnClickListener {
            handler.onClickSupport(Support.BLOCK)
            popupWindow.dismiss()
        }

        popupWindow.elevation = 8f
        popupWindow.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        popupWindow.showAsDropDown(anchor)
    }

    fun bind(item: ProfileItems.InformationItem) {
        val content = item.value
        with(binding) {
            val reportButtonVisibility = if (item.isMyProfile) View.GONE else View.VISIBLE

            ivReport.visibility = reportButtonVisibility
            tvNickname.text = content.nickname

            val message =
                if (content.message.isNullOrEmpty()) {
                    itemView.context.getString(R.string.profile_message_placeholder)
                } else {
                    content.message
                }

            tvDescription.text = message

//            Glide
//                .with(binding.root)
//                .load(content.profileImage)
//                .into(ivProfile)
        }
    }

    companion object {
        fun UserInformationViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserInformationViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserInformationBinding.inflate(inflater, parent, false)
            return UserInformationViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onClickProfileImage()

        fun onClickSupport(type: Support)
    }
}
