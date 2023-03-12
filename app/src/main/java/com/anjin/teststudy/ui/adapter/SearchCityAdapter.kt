package com.anjin.teststudy.ui.adapter

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.anjin.teststudy.R
import com.anjin.teststudy.WeatherApplication
import com.anjin.teststudy.databinding.ItemSearchCityRvBinding
import com.anjin.teststudy.datebase.bean.SearchCityResponse

class SearchCityAdapter(cities: List<SearchCityResponse.LocationBean>) :
    RecyclerView.Adapter<SearchCityAdapter.ViewHolder?>() {

    private val beans: List<SearchCityResponse.LocationBean>
    private lateinit var onClickItemCallback: OnClickItemCallback

    //关键字
    private lateinit var targetString: String

    init {
        beans = cities
    }

    fun setOnClickItemCallback(onClickItemCallback: OnClickItemCallback) {
        this.onClickItemCallback = onClickItemCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSearchCityRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        binding.root.setOnClickListener {
            onClickItemCallback.onItemClick(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = beans[position]
        val result = "${bean.name}，${bean.adm2}，${bean.adm1}，${bean.country}"
        if (targetString.isNotEmpty()) {
            holder.binding.tvCityName.text = matchSearchText(result, targetString)
        } else {
            holder.binding.tvCityName.text = result
        }
    }

    override fun getItemCount(): Int {
        return beans.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeTxColor(content: String) {
        targetString = content
        notifyDataSetChanged()
    }

    companion object {
        fun matchSearchText(string: String, keyWord: String): CharSequence {
            val spannableStringBuilder = SpannableStringBuilder(string)
            val index = string.indexOf(keyWord)
            if (index != -1) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(WeatherApplication.getContext(), R.color.yellow)
                    ),
                    index, index + keyWord.length, SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return spannableStringBuilder
        }
    }

    class ViewHolder(itemSearchCityRvBinding: ItemSearchCityRvBinding) :
        RecyclerView.ViewHolder(itemSearchCityRvBinding.root) {
        var binding: ItemSearchCityRvBinding

        init {
            binding = itemSearchCityRvBinding
        }
    }
}