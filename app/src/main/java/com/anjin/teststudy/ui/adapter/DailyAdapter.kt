package com.anjin.teststudy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.databinding.ItemDailyRvBinding
import com.anjin.teststudy.datebase.bean.DailyResponse
import com.anjin.teststudy.utils.EasyDate
import com.anjin.teststudy.utils.WeatherUtil

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
class DailyAdapter(dailyBeans: List<DailyResponse.DailyBean>) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder?>() {
    private val dailyBeans: List<DailyResponse.DailyBean>
    private lateinit var onClickItemCallback: OnClickItemCallback

    init {
        this.dailyBeans = dailyBeans
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDailyRvBinding =
            ItemDailyRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = ViewHolder(binding)
        binding.root.setOnClickListener {
            onClickItemCallback.onItemClick(holder.adapterPosition)
        }
        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyBean: DailyResponse.DailyBean = dailyBeans[position]
        holder.binding.tvDate.text = EasyDate.dateSplit(dailyBean.fxDate) + EasyDate.getDayInfo(
            dailyBean.fxDate
        )
        WeatherUtil.changeIcon(holder.binding.ivStatus, dailyBean.iconDay.toInt())
        holder.binding.tvHeight.text = " / " + dailyBean.tempMax + "℃"
        holder.binding.tvLow.text = dailyBean.tempMin.toString() + "℃"
    }

    override fun getItemCount(): Int {
        return dailyBeans.size
    }

    fun setOnClickItemCallback(onClickItemCallback: OnClickItemCallback) {
        this.onClickItemCallback = onClickItemCallback
    }

    class ViewHolder(itemTextRvBinding: ItemDailyRvBinding) :
        RecyclerView.ViewHolder(itemTextRvBinding.root) {
        var binding: ItemDailyRvBinding

        init {
            binding = itemTextRvBinding
        }
    }
}
