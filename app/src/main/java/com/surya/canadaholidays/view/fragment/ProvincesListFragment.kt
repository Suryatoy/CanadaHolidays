package com.surya.canadaholidays.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.core.content.ContextCompat

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.surya.canadaholidays.R
import com.surya.canadaholidays.model.Province
import com.surya.canadaholidays.view.adapter.ProvincesListAdapter
import com.surya.canadaholidays.viewmodel.ProvinceListViewModel
import kotlinx.android.synthetic.main.fragment_provinces_list.*


/**
 * A simple [Fragment] subclass.
 * Use the [ProvincesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProvincesListFragment : Fragment() {

    private lateinit var viewModel: ProvinceListViewModel
    private val listAdapter = ProvincesListAdapter(arrayListOf())
    private val provincesListDataObserver = Observer<List<Province>> { list ->
        list?.let {
            provincesRecyclerView.visibility = View.VISIBLE
            listAdapter.updateProvincesList(it)
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            errorText.visibility = View.GONE
            provincesRecyclerView.visibility = View.GONE
        }
    }

    private val errorLiveDataObserver = Observer<Boolean> { isError ->
        errorText.visibility = if (isError) View.VISIBLE else View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_provinces_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProvinceListViewModel::class.java)
        viewModel.provinces.observe(viewLifecycleOwner, provincesListDataObserver)
        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        viewModel.refresh()

        provincesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }
        refreshLayout.setOnRefreshListener() {
            provincesRecyclerView.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            errorText.visibility = View.GONE
            viewModel.refresh()
            animateProvincesList()
            refreshLayout.isRefreshing = false
        }
        // colors for progress dialog
        refreshLayout?.setColorSchemeColors(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            ContextCompat.getColor(requireContext(), R.color.colorAccent),
            ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
        )
    }

    /**
     * To animate provinces recyclerview when data is refreshed
     */
    private fun animateProvincesList() {
        AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        provincesRecyclerView.scheduleLayoutAnimation()
    }
}