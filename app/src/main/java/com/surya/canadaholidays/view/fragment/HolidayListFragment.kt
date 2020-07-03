package com.surya.canadaholidays.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.surya.canadaholidays.R
import com.surya.canadaholidays.model.Holiday
import com.surya.canadaholidays.model.Province
import com.surya.canadaholidays.view.adapter.HolidaysListAdapter
import com.surya.canadaholidays.view.adapter.ProvincesListAdapter
import com.surya.canadaholidays.viewmodel.HolidayListViewModel
import com.surya.canadaholidays.viewmodel.ProvinceListViewModel
import kotlinx.android.synthetic.main.fragment_holiday_list.*
import kotlinx.android.synthetic.main.fragment_provinces_list.*
import kotlinx.android.synthetic.main.fragment_provinces_list.errorText
import kotlinx.android.synthetic.main.fragment_provinces_list.progressbar
import kotlinx.android.synthetic.main.fragment_provinces_list.refreshLayout


/**
 * A simple [Fragment] subclass.
 * Use the [HolidayListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HolidayListFragment : Fragment() {

    private lateinit var viewModel: HolidayListViewModel
    private val listAdapter = HolidaysListAdapter(arrayListOf())
    private var provinceCode: String? = null

    private val holidaysListDataObserver = Observer<List<Holiday>> { list ->
        list?.let {
            holidaysRecyclerView.visibility = View.VISIBLE
            listAdapter.updateHolidaysList(it)
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            errorText.visibility = View.GONE
            holidaysRecyclerView.visibility = View.GONE
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
        return inflater.inflate(R.layout.fragment_holiday_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            provinceCode = HolidayListFragmentArgs.fromBundle(it).provinceCode
        }
        viewModel = ViewModelProviders.of(this).get(HolidayListViewModel::class.java)
        viewModel.holidays.observe(viewLifecycleOwner, holidaysListDataObserver)
        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        viewModel.refresh(provinceCode)

        holidaysRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }
        refreshLayout.setOnRefreshListener() {
            holidaysRecyclerView.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            errorText.visibility = View.GONE
            viewModel.refresh(provinceCode)
            animateHolidayList()
            refreshLayout.isRefreshing = false
        }
    }

    /**
     * To animate provinces recyclerview when data is refreshed
     */
    private fun animateHolidayList() {
        AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);
        holidaysRecyclerView.scheduleLayoutAnimation()
    }
}