package com.surya.canadaholidays.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.surya.canadaholidays.R
import com.surya.canadaholidays.databinding.ItemProvinceBinding
import com.surya.canadaholidays.model.Province
import com.surya.canadaholidays.util.Constants
import com.surya.canadaholidays.util.convertTimestamp
import com.surya.canadaholidays.util.getStringPreference
import com.surya.canadaholidays.view.fragment.ProvincesListFragmentDirections
import com.surya.canadaholidays.view.interfaces.ProvinceClickListener


class ProvincesListAdapter(private val provincesList: ArrayList<Province>) :
    RecyclerView.Adapter<ProvincesListAdapter.ProvinceViewHolder>(), ProvinceClickListener {

    class ProvinceViewHolder(var view: ItemProvinceBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemProvinceBinding>(
            inflater,
            R.layout.item_province,
            parent,
            false
        )
        return ProvinceViewHolder(view)
    }

    override fun getItemCount() = provincesList.size

    override fun onBindViewHolder(holder: ProvinceViewHolder, position: Int) {
        holder.view.province = provincesList[position]
        holder.view.nextHoliday.text =
            holder.view.nextHoliday.context.getString(R.string.next_holiday_placeholder) + " " + provincesList[position].nextHoliday.nameEn + " (" + convertTimestamp(
                provincesList[position].nextHoliday.date
            ) + ")"
        holder.view.listener = this
        if (!getStringPreference(Constants.PROVINCE_ID, "").isNullOrBlank()) {
            val preferredProvinceId = getStringPreference(Constants.PROVINCE_ID, "")
            if (preferredProvinceId.equals(provincesList[position].id)) {
                holder.view.cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        holder.itemView.context,
                        R.color.recyclerview_item_highlight
                    )
                )
            } else {
                holder.view.cardView.setCardBackgroundColor(Color.WHITE)
            }
        } else {
            holder.view.cardView.setCardBackgroundColor(Color.WHITE)
        }
    }

    fun updateProvincesList(newProvincesList: List<Province>) {
        provincesList.clear()
        provincesList.addAll(newProvincesList)
        notifyDataSetChanged()
    }

    override fun onClick(view: View) {
        for (province in provincesList) {
            if (view.tag == province.nameEn) {
                val action =
                    ProvincesListFragmentDirections.holidayListAction(province)
                Navigation.findNavController(view).navigate(action)
            }
        }
    }
}