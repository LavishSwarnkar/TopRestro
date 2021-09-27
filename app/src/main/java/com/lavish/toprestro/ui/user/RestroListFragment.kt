package com.lavish.toprestro.ui.user

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavish.toprestro.App
import com.lavish.toprestro.R
import com.lavish.toprestro.databinding.FragmentRestroListBinding
import com.lavish.toprestro.dialogs.ErrorDialog
import com.lavish.toprestro.models.Restaurant
import com.lavish.toprestro.other.LogoutHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RestroListFragment: Fragment() {

    //ListAdapter filter, multiple view types workaround
    //Loading dialog
    //RoomDB

    val userViewModel: UserViewModel by activityViewModels()

    lateinit var b: FragmentRestroListBinding
    private lateinit var app: App

    var restros: MutableLiveData<MutableList<Restaurant>> = MutableLiveData()
    private lateinit var adapter: RestaurantsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("RestroDebug", "onCreateView()")
        app = activity?.applicationContext as App

        b = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_restro_list,
            container,
            false
        )

        setHasOptionsMenu(true)

        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            fetchRestros()
        }
    }

    suspend fun fetchRestros() {
        if (!(userViewModel.restroListUiState.value is UiState.ShowData<*>))
            userViewModel.fetchRestros()

        userViewModel.restroListUiState.collect {
            when(it) {
                //Loading
                is UiState.Loading -> {
                    app.showLoadingDialog(requireContext())
                }

                //Loading complete
                is UiState.ShowData<*> -> {
                    app.hideLoadingDialog()

                    restros = it.data as MutableLiveData<MutableList<Restaurant>>
                    setupAdapter(restros)
                }

                //User offline
                is UiState.Offline -> {
                    app.hideLoadingDialog()
                    onOffline()
                }

                //Error
                is UiState.Error -> {
                    app.hideLoadingDialog()
                    ErrorDialog(requireContext()).show(it.e)
                }

                //Account deleted, Access Denied
                is UiState.AccessDenied -> {
                    app.hideLoadingDialog()
                    onAccessDenied()
                }
            }
        }
    }

    private fun onOffline() {
        val onOfflineRetry: () -> Unit = {
            userViewModel.fetchRestros()
        }

        val onOfflineClose: () -> Unit = {
            activity?.finish()
        }

        ErrorDialog(requireContext()).show(
            isOfflineError = true,
            retryCallback = onOfflineRetry,
            closeCallback = onOfflineClose
        )
    }

    private fun onAccessDenied() {
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(false)
            .setTitle("Access Denied!")
            .setMessage("Your account has been deleted. You have been logged out!")
            .setPositiveButton("OKAY") { dialog, which ->
                dialog.dismiss()
                LogoutHelper().logout(requireContext())
            }.show()
    }


    //On Data fetched

    private fun setupAdapter(restros: MutableLiveData<MutableList<Restaurant>>) {
        if(restros.value?.size == 0){
            app.hideLoadingDialog()
            b.nothingFound.visibility = View.VISIBLE
            b.filterHeader.visibility = View.GONE
            return
        }

        val onNothingFound: (Boolean) -> Unit = {
            view?.findViewById<TextView>(R.id.nothingFound)?.visibility = if(it) VISIBLE else GONE
        }

        adapter = RestaurantsAdapter(restros.value!!, onNothingFound)

        restros.observe(viewLifecycleOwner) {
            Log.d("TRDebug", "observer called!")
        }

        //Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        b.recyclerView.layoutManager = layoutManager

        //Set adapter
        b.recyclerView.adapter = adapter

        setupFilterHandler()
        app.hideLoadingDialog()

        restros.observe(viewLifecycleOwner) {
            Log.d("MyRestroAdapter", "observer invoked!")
            adapter.updateList(it)
        }
    }

    private fun setupFilterHandler() {
        b.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            adapter.currMinStarRating = rating.toInt()
        }
    }


    //Logout Options Menu

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            LogoutHelper().logout(requireContext())
        }

        return super.onOptionsItemSelected(item)
    }



}