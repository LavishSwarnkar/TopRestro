package com.lavish.toprestro.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lavish.toprestro.databinding.CardUserBinding
import com.lavish.toprestro.models.Profile

class ManageUsersAdapter(val type: String, val profiles : MutableList<Profile>
        , val showNoUsersView : (Boolean) -> Unit)
            : RecyclerView.Adapter<UsersViewHolder>() {

    var visibleProfiles: MutableList<Profile> = profiles.toMutableList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(type,
                CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(visibleProfiles[position]) {
            profiles.remove(visibleProfiles[position])
            visibleProfiles.removeAt(position)
            notifyItemRemoved(position)

            if(profiles.size == 0) showNoUsersView(false)
        }
    }

    override fun getItemCount(): Int = visibleProfiles.size

    fun filter(query: String?) {
        visibleProfiles = mutableListOf()

        if(query != null){
            profiles.forEach {
                if(it.name!!.contains(query))
                    visibleProfiles.add(it)
            }
        }

        showNoUsersView(visibleProfiles.size == 0)

        notifyDataSetChanged()
    }

    fun add(t: Profile) {
        visibleProfiles.add(0, t)
        profiles.add(0, t)
        notifyItemInserted(0)
    }

}