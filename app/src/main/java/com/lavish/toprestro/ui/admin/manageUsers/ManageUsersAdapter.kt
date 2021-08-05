package com.lavish.toprestro.ui.admin.manageUsers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.lavish.toprestro.databinding.CardUserBinding
import com.lavish.toprestro.models.Profile
import com.lavish.toprestro.other.Constants.TYPE_ADMIN
import com.lavish.toprestro.other.LogoutHelper

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
            //Admin delete self
            if(visibleProfiles[position].emailId == FirebaseAuth.getInstance().currentUser!!.email
                    && type == TYPE_ADMIN){
                LogoutHelper().logout(holder.b.root.context)
                return@bind
            }

            //Remove finally
            profiles.remove(visibleProfiles[position])
            visibleProfiles.removeAt(position)
            notifyItemRemoved(position)

            //No users
            if(profiles.size == 0) showNoUsersView(true)
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