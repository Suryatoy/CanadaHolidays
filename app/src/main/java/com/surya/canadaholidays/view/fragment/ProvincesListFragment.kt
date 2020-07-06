package com.surya.canadaholidays.view.fragment

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.surya.canadaholidays.R
import com.surya.canadaholidays.model.Province
import com.surya.canadaholidays.util.Constants
import com.surya.canadaholidays.util.addPreference
import com.surya.canadaholidays.util.isInternetAvailable
import com.surya.canadaholidays.util.showError
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
    private var provinceList = listOf<Province>()
    private val provincesListDataObserver = Observer<List<Province>> { list ->
        list?.let {
            provincesRecyclerView.visibility = View.VISIBLE
            listAdapter.updateProvincesList(it)
            provinceList = it
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            provincesRecyclerView.visibility = View.GONE
        }
    }

    private val errorLiveDataObserver = Observer<String> { errorMessage ->
        context?.let {
            showError(provincesRecyclerView, it, errorMessage)
        }
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
        if(isInternetAvailable(requireContext())) {
            viewModel.refresh()
        }
        else
        {
            progressbar.visibility = View.GONE
            showError(provincesRecyclerView,requireContext(),getString(R.string.Internet_error))
        }

        provincesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
             ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(provincesRecyclerView);
        }
        refreshLayout.setOnRefreshListener() {
            provincesRecyclerView.visibility = View.GONE
            progressbar.visibility = View.VISIBLE
            if(isInternetAvailable(requireContext())) {
                viewModel.refresh()
            }
            else
            {
                progressbar.visibility = View.GONE
                showError(provincesRecyclerView,requireContext(),getString(R.string.Internet_error))
            }
            animateProvincesList()
            refreshLayout.isRefreshing = false
        }
    }

    /**
     * To animate provinces recyclerview when data is refreshed
     */
    private fun animateProvincesList() {
        AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        provincesRecyclerView.scheduleLayoutAnimation()
    }

    /**
     * To provide swipe to left|right functionality to recyclerview
     */

    private var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            private val background = ColorDrawable(Color.BLUE)

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val provinceId = provinceList[viewHolder.adapterPosition].id
                addPreference(Constants.PROVINCE_ID,provinceId)
                listAdapter.notifyDataSetChanged()
                animateProvincesList()
            }

            override fun onChildDraw(
                @NonNull c: Canvas, @NonNull recyclerView: RecyclerView,
                @NonNull viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                actionState: Int, isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20
                if (dX > 0) { // Swiping to the right
                    background.setBounds(
                        itemView.left, itemView.top,
                        itemView.left + dX.toInt() + backgroundCornerOffset,
                        itemView.bottom
                    )
                } else if (dX < 0) { // Swiping to the left
                    background.setBounds(
                        itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom
                    )
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0)
                }
                background.draw(c)
            }
        }


}