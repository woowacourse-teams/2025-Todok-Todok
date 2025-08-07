package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemUserInformationBinding
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
        }
    }

    fun bind(item: ProfileItems.InformationItem) {
        val content = item.value
        with(binding) {
            tvNickname.text = content.nickname
            tvDescription.text = content.description

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
    }
}
