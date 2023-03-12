package com.anjin.teststudy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.databinding.ItemRecommendCityBinding

class RecommendCityAdapter(private val cities: List<String>) :
    RecyclerView.Adapter<RecommendCityAdapter.ViewHolder>() {
    private var onClickItemCallback //视图点击
            : OnClickItemCallback? = null

    fun setOnClickItemCallback(onClickItemCallback: OnClickItemCallback?) {
        this.onClickItemCallback = onClickItemCallback
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecommendCityBinding =
            ItemRecommendCityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        holder.binding.tvRecommendCityName.text = cities[position]
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    class ViewHolder(@NonNull itemRecommendCityBinding: ItemRecommendCityBinding) :
        RecyclerView.ViewHolder(itemRecommendCityBinding.root) {
        var binding: ItemRecommendCityBinding

        init {
            binding = itemRecommendCityBinding
        }
    }
}