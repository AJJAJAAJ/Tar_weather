package com.anjin.teststudy.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anjin.library.base.NetworkActivity
import com.anjin.teststudy.Constant
import com.anjin.teststudy.R
import com.anjin.teststudy.databinding.ActivityManageCityBinding
import com.anjin.teststudy.datebase.bean.MyCity
import com.anjin.teststudy.ui.adapter.MyCityAdapter
import com.anjin.teststudy.ui.adapter.OnClickItemCallback
import com.anjin.teststudy.utils.AddCityDialog
import com.anjin.teststudy.viewmodel.ManageCityViewModel

class ManageCityActivity : NetworkActivity<ActivityManageCityBinding?>() {

    private lateinit var viewModel: ManageCityViewModel
    private val myCityList = mutableListOf<MyCity>()
    private val myCityAdapter = MyCityAdapter(myCityList)


    override fun onCreate() {
        initView()
        viewModel = ViewModelProvider(this).get(ManageCityViewModel::class.java)
        viewModel.getAllCityData()
    }

    private fun initView() {
        backAndFinish(binding!!.toolbar)
        setStatusBar(true)
        myCityAdapter.setOnClickItemCallback(object : OnClickItemCallback {
            override fun onItemClick(position: Int) {
                setPageResult(myCityList[position].cityName)
            }
        })
        binding!!.rvCity.layoutManager = LinearLayoutManager(this@ManageCityActivity)
        binding!!.rvCity.adapter = myCityAdapter
        binding!!.btnAddCity.setOnClickListener {
            AddCityDialog.show(
                this@ManageCityActivity,
                Constant.CITY_ARRAY.asList()
            ) { cityName ->
                viewModel.addMyCityData(cityName)
                setPageResult(cityName)
            }
        }
        val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(0, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showDeleteCity(viewHolder.adapterPosition)
            }
        })
        helper.attachToRecyclerView(binding!!.rvCity)
    }

    private fun showDeleteCity(position: Int) {
        // 声明对象
        val dialog: AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setTitle("删除城市")
            .setIcon(R.drawable.ic_round_delete_forever_24)
            .setMessage("您确定要删除吗？")
            .setPositiveButton("确定") { dialog1, _ ->
                val myCity = myCityList[position]
                myCityList.removeAt(position)
                myCityAdapter.notifyItemRemoved(position)
                viewModel.deleteMyCityData(myCity)
                dialog1.dismiss()
            }
            .setNegativeButton("取消") { dialog12, _ ->
                myCityAdapter.notifyItemChanged(position)
                dialog12.dismiss()
            }
        dialog = builder.create()
        dialog.show()
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK)
    }


    private fun setPageResult(cityName: String) {
        val intent = Intent()
        intent.putExtra(Constant.CITY_RESULT, cityName)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onObserveData() {
        viewModel.listMutableLiveData.observe(
            this
        ) { myCites ->
            if (myCites != null && myCites.isNotEmpty()) {
                myCityList.clear()
                myCityList.addAll(myCites)
                myCityAdapter.notifyDataSetChanged()
            } else {
                showMsg("空空如也")
            }
        }
    }

    override fun onRegister() {}
}
