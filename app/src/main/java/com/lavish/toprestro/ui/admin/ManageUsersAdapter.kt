package com.lavish.toprestro.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardUserBinding
import com.lavish.toprestro.models.Profile

class ManageUsersAdapter(val type: String, val profiles : MutableList<Profile>) : RecyclerView.Adapter<UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(type,
                CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(profiles[position]) {
            profiles.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int = profiles.size

}