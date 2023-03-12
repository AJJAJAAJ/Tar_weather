package com.anjin.teststudy.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anjin.library.base.NetworkActivity
import com.anjin.teststudy.Constant
import com.anjin.teststudy.R
import com.anjin.teststudy.databinding.ActivityMainBinding
import com.anjin.teststudy.databinding.DialogDailyDetailBinding
import com.anjin.teststudy.databinding.DialogHourlyDetailBinding
import com.anjin.teststudy.datebase.bean.AirResponse
import com.anjin.teststudy.datebase.bean.DailyResponse
import com.anjin.teststudy.datebase.bean.HourlyResponse
import com.anjin.teststudy.datebase.bean.LifestyleResponse
import com.anjin.teststudy.location.GoodLocation
import com.anjin.teststudy.location.LocationCallback
import com.anjin.teststudy.ui.adapter.DailyAdapter
import com.anjin.teststudy.ui.adapter.HourlyAdapter
import com.anjin.teststudy.ui.adapter.LifestyleAdapter
import com.anjin.teststudy.ui.adapter.OnClickItemCallback
import com.anjin.teststudy.utils.CityDialog
import com.anjin.teststudy.utils.EasyDate
import com.anjin.teststudy.utils.GlideUtils
import com.anjin.teststudy.utils.MVUtils
import com.anjin.teststudy.viewmodel.MainViewModel
import com.baidu.location.BDLocation
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog


class MainActivity : NetworkActivity<ActivityMainBinding>(), LocationCallback,
    CityDialog.SelectedCityCallback {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    /**
     * 城市名称，定位和切换城市都会重新赋值。
     */
    private var mCityName: String? = null

    /**
     * 是否正在刷新
     */
    private var isRefresh: Boolean = false

    /**
     * 菜单
     */
    private lateinit var mMenu: Menu

    /**
     * 城市切换来源标志 0：定位 1：切换城市
     */
    private var cityFlag = 0


    /**
     * 权限数组
     */
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /**
     * 请求权限意图
     */
    private lateinit var requestPermissionIntent: ActivityResultLauncher<Array<String>>

    /**
     * viewModel
     */
    private lateinit var viewModel: MainViewModel

    private lateinit var jumpActivityIntent: ActivityResultLauncher<Intent>

    /**
     * 定位
     */
    private lateinit var goodLocation: GoodLocation

    private val dailyBeanList = mutableListOf<DailyResponse.DailyBean>()
    private val dailyAdapter = DailyAdapter(dailyBeanList)

    private val lifeStyleList = mutableListOf<LifestyleResponse.DailyBean>()
    private val lifeStyleAdapter = LifestyleAdapter(lifeStyleList)

    private val hourlyBeanList = mutableListOf<HourlyResponse.HourlyBean>()
    private val hourlyAdapter = HourlyAdapter(hourlyBeanList)

    private lateinit var cityDialog: CityDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate() {
        setFullScreenImmersion()
        initLocation()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission()
        }
        initView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getAllCity()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        setToolbarMoreIconCustom(binding?.materialToolbar)
        binding?.rvDaily?.layoutManager = LinearLayoutManager(this)
        binding?.rvDaily?.adapter = dailyAdapter

        binding?.rvLifestyle?.layoutManager = LinearLayoutManager(this)
        binding?.rvLifestyle?.adapter = lifeStyleAdapter

        val hourlyLayoutManager = LinearLayoutManager(this)
        hourlyLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding!!.rvHourly.layoutManager = hourlyLayoutManager
        binding!!.rvHourly.adapter = hourlyAdapter


        binding?.layRefresh?.setOnRefreshListener {
            if (mCityName == null) {
                binding?.layRefresh?.isRefreshing = false
                return@setOnRefreshListener
            }
            isRefresh = true
            viewModel.searchCity(mCityName, true)
        }

        //滑动监听
        binding?.layScroll?.setOnScrollChangeListener { _: View?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (scrollY > oldScrollY) {
                //getMeasuredHeight() 表示控件的绘制高度
                if (scrollY > binding!!.layScrollHeight.measuredHeight) {
                    binding!!.materialToolbar.title = if (mCityName == null) "城市天气" else mCityName
                }
            } else if (scrollY < oldScrollY) {
                if (scrollY < binding!!.layScrollHeight.measuredHeight) {
                    //改回原来的
                    binding!!.materialToolbar.title = "城市天气"
                }
            }
        }

        dailyAdapter.setOnClickItemCallback(object : OnClickItemCallback {
            override fun onItemClick(position: Int) {
                showDailyDetailDialog(dailyBeanList[position])
            }
        })

        hourlyAdapter.setOnClickItemCallback(object : OnClickItemCallback {
            override fun onItemClick(position: Int) {
                showHourlyDetailDialog(hourlyBeanList[position])
            }
        })
    }

    @SuppressLint("NewApi")
    fun setToolbarMoreIconCustom(toolbar: MaterialToolbar?) {
        if (toolbar == null) {
            return
        }
        toolbar.title = "城市天气"
        val toolbarIcon = ContextCompat.getDrawable(toolbar.context, R.drawable.ic_round_add_32)
        if (toolbarIcon != null) {
            toolbar.overflowIcon = toolbarIcon
        }
        setSupportActionBar(toolbar)
    }

    override fun onRegister() {
        requestPermissionIntent =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                fun onActivityResult(result: Map<String?, Boolean?>?) {
                    val fineLocation =
                        java.lang.Boolean.TRUE == result!![Manifest.permission.ACCESS_FINE_LOCATION]
                    val writeStorage =
                        java.lang.Boolean.TRUE == result[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    if (fineLocation && writeStorage) {
                        //权限已经获取到，开始定位
                        startLocation()
                    }
                }
            }

        jumpActivityIntent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val city = result.data!!.getStringExtra(Constant.CITY_RESULT)
                    //检查返回的城市 , 如果返回的城市是当前定位城市，并且当前定位标志为0，则不需要请求
                    if (city!! == MVUtils.getString(Constant.LOCATION_CITY) && cityFlag == 0) {
                        Log.d(TAG, "onRegister: 管理城市页面返回不需要进行天气查询");
                        return@registerForActivityResult;
                    }
                    //反之就直接调用选中城市的方法进行城市天气搜索
                    Log.d(TAG, "onRegister: 管理城市页面返回进行天气查询");
                    selectedCity(city)
                }
            }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onObserveData() {
        /*城市数据返回*/
        viewModel.searchCityResponseMutableLiveData.observe(
            this
        ) { searchCityResponse ->
            val location = searchCityResponse?.location
            if (location != null && location.size > 0) {
                val locationId = location[0].id
                Log.i(TAG, "locationId is $locationId")
                mMenu.findItem(R.id.item_relocation).isVisible = cityFlag == 1
                if (isRefresh) {
                    showLongMsg("刷新完成")
                    binding?.layRefresh?.isRefreshing = false
                    isRefresh = false
                }
                if (locationId.isNotEmpty()) {
                    //通过ID查询城市
                    viewModel.nowWeather(locationId)
                    //查询天气预报
                    viewModel.dailyWeather(locationId)
                    //查询生活建议
                    viewModel.lifeStyle(locationId)
                    //查询逐小时天气预报
                    viewModel.hourlyWeather(locationId)
                    //通过城市ID查询空气质量
                    viewModel.airWeather(locationId)
                }
            }
        }

        /*实时天气返回*/
        viewModel.nowResponseMutableLiveData.observe(
            this
        ) { nowResponse ->
            val now = nowResponse?.now
            if (now != null) {
                Log.i(TAG, "nowResponse")
                binding?.tvWeek?.text = EasyDate.getTodayOfWeek()
                binding!!.tvWeatherInfo.text = now.text
                binding?.tvTemp?.text = now.temp

                //精简更新时间
                val time = EasyDate.updateTime(nowResponse.updateTime)
                binding!!.tvUpdateTime.text = "最近更新时间：${nowResponse.updateTime}$time"
                binding?.tvWindDirection?.text = "风向     ${now.windDir}"//风向
                binding?.tvWindPower?.text = "风力     ${now.windScale}级"//风力
                binding?.wwBig?.startRotate()//大风车开始转动
                binding?.wwSmall?.startRotate()//小风车开始转动
            }
        }

        /*天气预报返回*/
        viewModel.dailyResponseMutableLiveData.observe(
            this
        ) { dailyResponse ->
            val daily = dailyResponse?.daily
            if (daily != null) {
                Log.i(TAG, "dailyResponse")
                if (dailyBeanList.size > 0) {
                    dailyBeanList.clear()
                }
                dailyBeanList.addAll(daily)
                dailyAdapter.notifyDataSetChanged()
                binding!!.tvHeight.text = "${daily[0].tempMax}℃"
                binding!!.tvLow.text = "${daily[0].tempMin}℃"
            }
        }

        /*生活建议返回*/
        viewModel.lifeStyleResponseMutableLiveData.observe(
            this
        ) { lifeStyleResponse ->
            val daily = lifeStyleResponse?.daily
            if (daily != null) {
                if (lifeStyleList.size > 0) {
                    lifeStyleList.clear()
                }
                lifeStyleList.addAll(daily)
                lifeStyleAdapter.notifyDataSetChanged()
            }
        }

        viewModel.airMutableLiveData.observe(
            this
        ) { airResponse ->
            dismissLoadingDialog()
            val now: AirResponse.NowBean? = airResponse?.now ?: return@observe
            binding!!.rpbAqi.setMaxProgress(300)                                //最大进度便于计算
            binding!!.rpbAqi.setMinText("0")                                    //最小显示值
            binding!!.rpbAqi.setMinTextSize(32f)
            binding!!.rpbAqi.setMaxText("300")                                  //设置显示最大值
            binding!!.rpbAqi.setMaxTextSize(32f)
            binding!!.rpbAqi.setProgress(now!!.aqi.toFloat())           //当前进度
            binding!!.rpbAqi.setArcBgColor(getColor(R.color.arc_bg_color))                //圆弧的颜色
            binding!!.rpbAqi.setProgressColor(getColor(R.color.arc_progress_color))             //进度圆弧的颜色
            binding!!.rpbAqi.setFirstText(now.category)                       //空气质量描述 取值范围：优，良，轻度污染，中度污染，重度污染，严重污染
            binding!!.rpbAqi.setFirstTextSize(44f)                              //第一行文本的字体大小
            binding!!.rpbAqi.setSecondText(now.aqi)                             //空气质量值
            binding!!.rpbAqi.setSecondTextSize(64f)//第二行文本的字体大小
            binding!!.rpbAqi.setMinText("0")
            binding!!.rpbAqi.setMinTextColor(getColor(R.color.arc_progress_color))
            binding!!.tvAirInfo.text = String.format("空气${now.category}")
            binding!!.tvPm10.text = now.pm10                                    //PM10
            binding!!.tvPm25.text = now.pm2p5                                   //PM2.5
            binding!!.tvNo2.text = now.no2                                      //二氧化氮
            binding!!.tvSo2.text = now.so2                                      //二氧化硫
            binding!!.tvO3.text = now.o3                                        //臭氧
            binding!!.tvCo.text = now.co                                        //一氧化碳
        }

        /*初始化城市*/
        viewModel.cityMutableLiveData.observe(
            this
        ) { province ->
            cityDialog = CityDialog.getInstance(this, province)
            cityDialog.setSelectedCityCallback(this)
        }

        viewModel.hourlyMutableLiveData.observe(
            this
        ) {
            val hourly = it?.hourly
            if (hourly != null) {
                if (hourlyBeanList.size > 0) {
                    hourlyBeanList.clear()
                }
                hourlyBeanList.addAll(hourly)
                hourlyAdapter.notifyDataSetChanged()
            }
        }

        viewModel.failed.observe(this, this::showLongMsg)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermission() {
        //因为项目的最低版本API是23，所以肯定需要动态请求危险权限，只需要判断权限是否拥有即可
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //开始权限请求
            requestPermissionIntent.launch(permissions)
            return
        }
        //开始定位
        startLocation()
    }


    private fun initLocation() {
        goodLocation = GoodLocation.getInstance(this)
        goodLocation.setCallback(this)
    }

    private fun startLocation() {
        cityFlag = 0
        goodLocation.startLocation()
    }

    override fun onReceiveLocation(bdLocation: BDLocation) {
        showLoadingDialog()
        //获取区县信息
        val district = bdLocation.district
        //获取详细地址
        val address = bdLocation.addrStr
        Log.i(TAG, "address is $address")
        mCityName = district
        MVUtils.put(Constant.LOCATION_CITY, district)
        viewModel.addMyCityData(district)
        binding!!.tvCity.text = district

        if (district != null) {
            viewModel.searchCity(district, isExact = true)
        } else {
            Log.e(TAG, "district is null")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (menu != null) {
            this.mMenu = menu
        }
        mMenu.findItem(R.id.item_relocation).isVisible = cityFlag == 1
        mMenu.findItem(R.id.item_bing).isChecked = MVUtils.getBoolean(Constant.USED_BING)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_switching_cities -> cityDialog.show()
            R.id.item_relocation -> startLocation()
            R.id.item_bing -> {
                item.isChecked = !item.isChecked
                MVUtils.put(Constant.USED_BING, item.isChecked)
                val bingUrl = MVUtils.getString(Constant.BING_URL)
                updateImage(item.isChecked, bingUrl)
            }
            R.id.item_manage_city -> jumpActivityIntent.launch(Intent(this, ManageCityActivity::class.java))
        }
        return true
    }

    override fun selectedCity(cityName: String?) {
        cityFlag = 1
        if (cityName != null) {
            mCityName = cityName
        }
        //搜索城市
        viewModel.searchCity(cityName, isExact = true)
        //显示所选城市
        binding?.tvCity?.text = cityName
    }

    private fun updateImage(usedBing: Boolean, bingUrl: String) {
        if (usedBing && bingUrl.isNotEmpty()) {
            Log.i(TAG, "updateImage")
            GlideUtils.loadImg(this, bingUrl, binding?.layRoot)
        } else {
            binding?.layRoot?.background = ContextCompat.getDrawable(this, R.drawable.main_bg)
        }
    }

    fun showDailyDetailDialog(dailyBean: DailyResponse.DailyBean) {
        val dialog = BottomSheetDialog(this@MainActivity)
        val detailBinding: DialogDailyDetailBinding =
            DialogDailyDetailBinding.inflate(LayoutInflater.from(this@MainActivity), null, false)
        //关闭弹窗
        detailBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        //设置数据显示
        detailBinding.toolbarDaily.title = String.format(
            "%s   %s",
            dailyBean.fxDate,
            EasyDate.getWeek(dailyBean.fxDate)
        )
        detailBinding.toolbarDaily.subtitle = "天气预报详情"
        detailBinding.tvTmpMax.text = String.format("%s℃", dailyBean.tempMax)
        detailBinding.tvTmpMin.text = String.format("%s℃", dailyBean.tempMin)
        detailBinding.tvUvIndex.text = dailyBean.uvIndex
        detailBinding.tvCondTxtD.text = dailyBean.textDay
        detailBinding.tvCondTxtN.text = dailyBean.textNight
        detailBinding.tvWindDeg.text = String.format("%s°", dailyBean.wind360Day)
        detailBinding.tvWindDir.text = dailyBean.windDirDay
        detailBinding.tvWindSc.text = String.format("%s级", dailyBean.windScaleDay)
        detailBinding.tvWindSpd.text = String.format("%s公里/小时", dailyBean.windSpeedDay)
        detailBinding.tvCloud.text = String.format("%s%%", dailyBean.cloud)
        detailBinding.tvHum.text = String.format("%s%%", dailyBean.humidity)
        detailBinding.tvPres.text = String.format("%shPa", dailyBean.pressure)
        detailBinding.tvPcpn.text = String.format("%smm", dailyBean.precip)
        detailBinding.tvVis.text = String.format("%skm", dailyBean.vis)
        dialog.setContentView(detailBinding.root)
        dialog.show()
    }

    fun showHourlyDetailDialog(hourlyBean: HourlyResponse.HourlyBean) {
        val dialog = BottomSheetDialog(this@MainActivity)
        val detailBinding: DialogHourlyDetailBinding =
            DialogHourlyDetailBinding.inflate(LayoutInflater.from(this@MainActivity), null, false)
        //关闭弹窗
        detailBinding.ivClose.setOnClickListener {
            dialog.dismiss()
        }
        //设置数据显示
        val time = EasyDate.updateTime(hourlyBean.fxTime)
        detailBinding.toolbarHourly.title = EasyDate.showTimeInfo(time) + time
        detailBinding.toolbarHourly.subtitle = "逐小时预报详情"
        detailBinding.tvTmp.text = String.format("%s℃", hourlyBean.temp)
        detailBinding.tvCondTxt.text = hourlyBean.text
        detailBinding.tvWindDeg.text = String.format("%s°", hourlyBean.wind360)
        detailBinding.tvWindDir.text = hourlyBean.windDir
        detailBinding.tvWindSc.text = String.format("%s级", hourlyBean.windScale)
        detailBinding.tvWindSpd.text = String.format("公里/小时%s", hourlyBean.windSpeed)
        detailBinding.tvHum.text = String.format("%s%%", hourlyBean.humidity)
        detailBinding.tvPres.text = String.format("%shPa", hourlyBean.pressure)
        detailBinding.tvPop.text = String.format("%s%%", hourlyBean.pop)
        detailBinding.tvDew.text = String.format("%s℃", hourlyBean.dew)
        detailBinding.tvCloud.text = String.format("%s%%", hourlyBean.cloud)
        dialog.setContentView(detailBinding.root)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        updateImage(MVUtils.getBoolean(Constant.USED_BING), MVUtils.getString(Constant.BING_URL))
    }
}