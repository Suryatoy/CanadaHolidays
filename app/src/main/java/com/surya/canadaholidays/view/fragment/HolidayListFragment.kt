package com.surya.canadaholidays.view.fragment

import android.content.ComponentName
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.surya.canadaholidays.R
import com.surya.canadaholidays.model.Holiday
import com.surya.canadaholidays.model.Province
import com.surya.canadaholidays.util.isInternetAvailable
import com.surya.canadaholidays.util.showError
import com.surya.canadaholidays.view.adapter.HolidaysListAdapter
import com.surya.canadaholidays.viewmodel.HolidayListViewModel
import kotlinx.android.synthetic.main.fragment_holiday_list.*
import kotlinx.android.synthetic.main.fragment_provinces_list.*
import kotlinx.android.synthetic.main.fragment_provinces_list.progressbar
import kotlinx.android.synthetic.main.fragment_provinces_list.refreshLayout


/**
 * A simple [Fragment] subclass.
 * Use the [HolidayListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HolidayListFragment : Fragment(),HolidaysListAdapter.OnItemClickListener {

    private lateinit var viewModel: HolidayListViewModel
    private val listAdapter = HolidaysListAdapter(arrayListOf())
    private var province: Province? = null

    val CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome"
    private var mCustomTabsServiceConnection: CustomTabsServiceConnection? = null
    private var mClient: CustomTabsClient? = null
    private var mCustomTabsSession: CustomTabsSession? = null

    private val holidaysListDataObserver = Observer<List<Holiday>> { list ->
        list?.let {
            holidaysRecyclerView.visibility = View.VISIBLE
            listAdapter.updateHolidaysList(it)
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            holidaysRecyclerView.visibility = View.GONE
        }
    }

    private val errorLiveDataObserver = Observer<String> { errorMessage ->
        context?.let {
            showError(holidaysRecyclerView, it,errorMessage)
        }
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
            province = HolidayListFragmentArgs.fromBundle(it).province
        }
        viewModel = ViewModelProviders.of(this).get(HolidayListViewModel::class.java)
        viewModel.holidays.observe(viewLifecycleOwner, holidaysListDataObserver)
        viewModel.loading.observe(viewLifecycleOwner, loadingLiveDataObserver)
        viewModel.loadError.observe(viewLifecycleOwner, errorLiveDataObserver)
        if(isInternetAvailable(requireContext())) {
            viewModel.refresh(province?.id)
        }
        else
        {
            progressbar.visibility = View.GONE
            showError(holidaysRecyclerView,requireContext(),getString(R.string.Internet_error))
        }
        listAdapter.itemClickListener = this
        holidaysRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
        }

        refreshLayout.setOnRefreshListener() {
            holidaysRecyclerView.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            if(isInternetAvailable(requireContext())) {
                viewModel.refresh(province?.id)
            }
            else
            {
                progressbar.visibility = View.GONE
                showError(holidaysRecyclerView,requireContext(),getString(R.string.Internet_error))
            }
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


    override fun onItemClicked(holiday: Holiday) {
        warmUpBrowser()
        loadPage(holiday.nameEn)
    }


    private fun warmUpBrowser() {
        mCustomTabsServiceConnection = object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                componentName: ComponentName,
                customTabsClient: CustomTabsClient
            ) {
                //Pre-warming
                mClient = customTabsClient
                mClient?.warmup(0L)
                mCustomTabsSession = mClient?.newSession(null)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mClient = null
            }
        }

        context?.let {
            CustomTabsClient.bindCustomTabsService(
                it,
                CUSTOM_TAB_PACKAGE_NAME,
                mCustomTabsServiceConnection as CustomTabsServiceConnection
            )
        }
    }

    /**
     * To load details about holiday in wikipedia page
     */
    private fun loadPage(holidayName: String) {
        context?.let {
        val urlBuilder = StringBuilder()
        urlBuilder.append("https://en.wikipedia.org/wiki/")
            .append(holidayName.replace(" ", "_"))
        val customTabsIntent = CustomTabsIntent.Builder(mCustomTabsSession)
            .setToolbarColor(ContextCompat.getColor(it, R.color.colorPrimary))
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(it, Uri.parse(urlBuilder.toString())) }

    }
}