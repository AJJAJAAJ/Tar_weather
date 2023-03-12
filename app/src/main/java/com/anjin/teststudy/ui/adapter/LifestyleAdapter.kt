package com.anjin.teststudy.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.datebase.bean.LifestyleResponse
import com.anjin.teststudy.databinding.ItemLifestyleRvBinding

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
class LifestyleAdapter(dailyBeans: List<LifestyleResponse.DailyBean>) :
    RecyclerView.Adapter<LifestyleAdapter.ViewHolder?>() {
    private val dailyBeans: List<LifestyleResponse.DailyBean>

    init {
        this.dailyBeans = dailyBeans
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLifestyleRvBinding =
            ItemLifestyleRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyBean: LifestyleResponse.DailyBean = dailyBeans[position]
        holder.binding.tvLifestyle.text = dailyBean.name + "：" + dailyBean.text
    }

    override fun getItemCount(): Int {
        return dailyBeans.size
    }

    class ViewHolder(lifestyleRvBinding: ItemLifestyleRvBinding) :
        RecyclerView.ViewHolder(lifestyleRvBinding.root) {
        var binding: ItemLifestyleRvBinding

        init {
            binding = lifestyleRvBinding
        }
    }
}