package com.surya.canadaholidays.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.surya.canadaholidays.R
import com.surya.canadaholidays.databinding.ItemHolidayBinding
import com.surya.canadaholidays.model.Holiday

class HolidaysListAdapter(private val holidaysList: ArrayList<Holiday>) :
    RecyclerView.Adapter<HolidaysListAdapter.HolidayViewHolder>() {

    class HolidayViewHolder(var view: ItemHolidayBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemHolidayBinding>(inflater,R.layout.item_holiday,parent,false)
        return HolidayViewHolder(view)
    }

    override fun getItemCount() = holidaysList.size

    override fun onBindViewHolder(holder: HolidayViewHolder, position: Int) {
        holder.view.holiday = holidaysList[position]
        holder.view.nextHoliday.text =  holder.view.nextHoliday.context.getString(R.string.holiday_date)+" "+holidaysList[position].date
    }

    fun updateHolidaysList(newProvincesList: List<Holiday>) {
        holidaysList.clear()
        holidaysList.addAll(newProvincesList)
        notifyDataSetChanged()
    }
}