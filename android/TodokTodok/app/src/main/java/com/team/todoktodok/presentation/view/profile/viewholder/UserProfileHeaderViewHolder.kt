package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemUserHeaderBinding

class UserProfileHeaderViewHolder private constructor(
    binding: ItemUserHeaderBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        with(binding) {
            ivLogo.root.setOnClickListener {
                handler.onClickLogo()
            }

            ivSetting.setOnClickListener {
                handler.onClickSetting()
            }
        }
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
