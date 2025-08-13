package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemUserHeaderBinding
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

class UserProfileHeaderViewHolder private constructor(
    private val binding: ItemUserHeaderBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        with(binding) {
            ivBack.setOnClickListener {
                handler.onClickLogo()
            }

            ivSetting.setOnClickListener {
                handler.onClickSetting()
            }
        }
    }

    fun bind(item: ProfileItems.HeaderItem) {
        if (!item.isMyProfile) binding.ivSetting.visibility = View.INVISIBLE
    }

    companion object {
        fun UserProfileHeaderViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserProfileHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserHeaderBinding.inflate(inflater, parent, false)
            return UserProfileHeaderViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onClickSetting()

        fun onClickLogo()
    }
}
