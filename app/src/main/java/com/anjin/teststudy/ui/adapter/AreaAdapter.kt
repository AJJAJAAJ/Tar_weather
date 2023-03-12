package com.anjin.teststudy.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.databinding.ItemTextRvBinding
import com.anjin.teststudy.datebase.bean.Province
import com.anjin.teststudy.utils.AdministrativeType

/**
 *
 * @description
 * @author Anjin
 * @date 2023-01-15
 *
 */
class AreaAdapter(areas: List<Province.City.Area>) :
    RecyclerView.Adapter<AreaAdapter.ViewHolder?>() {
    private val areas: List<Province.City.Area>
    private var administrativeClickCallback //视图点击
            : AdministrativeClickCallback? = null

    init {
        this.areas = areas
    }

    fun setAdministrativeClickCallback(administrativeClickCallback: AdministrativeClickCallback?) {
        this.administrativeClickCallback = administrativeClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTextRvBinding =
            ItemTextRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        //添加视图点击事件
        binding.root.setOnClickListener { v ->
            if (administrativeClickCallback != null) {
                administrativeClickCallback!!.onAdministrativeItemClick(
                    v,
                    viewHolder.adapterPosition,
                    AdministrativeType.AREA
                )
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvText.text = areas[position].areaName
    }

    override fun getItemCount(): Int {
        return areas.size
    }

    class ViewHolder(itemTextRvBinding: ItemTextRvBinding) :
        RecyclerView.ViewHolder(itemTextRvBinding.root) {
        var binding: ItemTextRvBinding

        init {
            binding = itemTextRvBinding
        }
    }
}