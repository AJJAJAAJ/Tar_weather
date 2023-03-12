package com.anjin.teststudy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.databinding.ItemHourlyRvBinding
import com.anjin.teststudy.datebase.bean.HourlyResponse.HourlyBean
import com.anjin.teststudy.utils.EasyDate
import com.anjin.teststudy.utils.WeatherUtil.changeIcon

class HourlyAdapter(private val hourlyBeans: List<HourlyBean>) :
    RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {
    private lateinit var onClickItemCallback: OnClickItemCallback

    fun setOnClickItemCallback(onClickItemCallback: OnClickItemCallback) {
        this.onClickItemCallback = onClickItemCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemHourlyRvBinding =
            ItemHourlyRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)
        binding.root.setOnClickListener {
            onClickItemCallback.onItemClick(holder.adapterPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hourlyBean = hourlyBeans[position]
        val time = EasyDate.updateTime(hourlyBean.fxTime)
        holder.binding.tvTime.text = String.format("%s%s", EasyDate.showTimeInfo(time), time)
        changeIcon(holder.binding.ivStatus, hourlyBean.icon.toInt())
        holder.binding.tvTemperature.text = String.format("%s℃", hourlyBean.temp)
    }

    override fun getItemCount(): Int {
        return hourlyBeans.size
    }

    class ViewHolder(itemHourlyRvBinding: ItemHourlyRvBinding) :
        RecyclerView.ViewHolder(itemHourlyRvBinding.root) {
        var binding: ItemHourlyRvBinding

        init {
            binding = itemHourlyRvBinding
        }
    }
}
