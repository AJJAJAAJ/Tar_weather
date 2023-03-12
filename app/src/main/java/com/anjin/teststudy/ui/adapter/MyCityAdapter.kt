package com.anjin.teststudy.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.Constant
import com.anjin.teststudy.databinding.ItemMyCityRvBinding
import com.anjin.teststudy.datebase.bean.MyCity
import com.anjin.teststudy.utils.MVUtils

class MyCityAdapter(private val cities: List<MyCity>) :
    RecyclerView.Adapter<MyCityAdapter.ViewHolder>() {
    private var onClickItemCallback //视图点击
            : OnClickItemCallback? = null

    fun setOnClickItemCallback(onClickItemCallback: OnClickItemCallback?) {
        this.onClickItemCallback = onClickItemCallback
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMyCityRvBinding =
            ItemMyCityRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        //添加视图点击事件
        binding.root.setOnClickListener { v ->
            if (onClickItemCallback != null) {
                onClickItemCallback!!.onItemClick(viewHolder.adapterPosition)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val cityName = cities[position].cityName
        val locationCity = MVUtils.getString(Constant.LOCATION_CITY)
        holder.binding.tvCityName.text = cityName
        holder.binding.ivLocation.visibility = if (cityName == locationCity) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class ViewHolder(@NonNull itemMyCityRvBinding: ItemMyCityRvBinding) :
        RecyclerView.ViewHolder(itemMyCityRvBinding.root) {
        var binding: ItemMyCityRvBinding

        init {
            binding = itemMyCityRvBinding
        }
    }
}
