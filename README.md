　　完整代码[https://github.com/huadongf/Bill2](https://github.com/huadongf/Bill2)  
　　纪念一下个人独立开发的功能全面的一款app——雨燕记账。这可能是你见过的最好看的记账app！该项目后续会继续完善并上架应用商店。  
　　完成的软件功能包括：（1）分类记录日常消费数据，包括金额，用途，日期，分类等信息，（2） 显示消费流水记录，（3）按天、周、月、年进行汇总分析形成结果。（4）能够根据输入条件查询，包括金额范围，用途，日期范围，分类等；（5）具备友好的用户数据输入界面和查询界面。  
　　为了保证软件与用户的交互友好性，本软件采用了底部菜单作为导航栏，并使用了jetpack的navigation组件来处理fragment之间的跳转，使用扇形图来汇总分析数据，使用对话框处理备注的输入和日期的输入，使用数据库来实现增删改查。  
　　软件中类的说明表如下

| 类  |  说明 |
| :------------: | :------------: |
|  DashboardFragment.kt |  “统计”页面的fragment |
|  DashboardViewModel.kt | 用来存放“统计”页面数据的viewmodel  |
|  HomeFragment.kt | “首页”的fragment  |
|  HomeViewModel.kt | 用来存放“首页”页面数据的viewmodel  |
|  NotificationsFragment.kt |  “我的”页面的fragment |
|  NotificationsViewModel.kt |  “我的”页面的viewmodel |
|  AddActivity.kt |  添加页面的activity |
|  AppDatabase.kt  |  提供直接访问底层数据库实现，我们应该从里面拿到具体的Dao,进行数据库操作。 |
|  Cate.kt | 用来保存分类的类  |
| DetailActivity.kt  |  详情页的activity |
| Homeadapter.kt  |  首页recyclerview的适配器 |
|  MainActivity.kt |  首页的activity |
| MyAdapter.kt  | 添加页面的recyclerview适配器  |
|  SearchableActivity.kt |  搜索页面的activity |
| Searchadapter.kt  | 搜索页面的recyclerview适配器  |
| User.kt  |  数据库具体的bean实体，会与数据库表column进行映射 |
| UserDao.kt  | 数据库访问对象，实现具体的增删改查  |
　　　　　　　　　　　　　　　　　　　　　　　　项目结构如下
![](/upload/20210131_11352720.png)
　　　　　　　　　　　　　　　　　　　　　　　　首页
![](/upload/20210131_1135592.png)
　　　　　　　　　　　　　　　　　　　　　　　　统计页
![](/upload/20210131_11361546.png)
　　　　　　　　　　　　　　　　　　　　　　　　添加页
![](/upload/20210131_1136407.png)
　　　　　　　　　　　　　　　　　　　　　　　　详情页
![](/upload/20210131_1136569.png)
核心代码
# DashboardFragment.kt
```cpp
package com.example.myapplication.ui.dashboard
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*
class DashboardFragment : Fragment() {
    private lateinit var dashboardViewModel: DashboardViewModel
    private val results = ArrayList<User>()
    private lateinit var adapter: Homeadapter
    private var mYear:Int=2020
    private  var mMonth:Int=11
    private  var mDay:Int=13
    private lateinit var io:TextView
    private lateinit var aaChartModel : AAChartModel
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardViewModel = ViewModelProvider(requireActivity()).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val spi=root.findViewById<Spinner>(R.id.spinner)
        val reccc=root.findViewById<RecyclerView>(R.id.rec3)
        io=root.findViewById(R.id.chart_tv_out)
        val lay = GridLayoutManager(requireContext(), 1)
        reccc.layoutManager = lay
        adapter = Homeadapter(requireContext(), results)
        reccc.adapter = adapter
        val intent= Intent(requireContext(), DetailActivity::class.java)
        adapter.setOnItemClickListener(object : Homeadapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                intent.putExtra(DetailActivity.IDD, results[position].idd)
                startActivity(intent)
            }
        })
        spi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                val tv = view as TextView
                //  tv.textSize = 20f //设置大小
                tv.gravity = Gravity.CENTER_HORIZONTAL //设置居中
                val a = resources.getStringArray(R.array.sortt)
                when(a[pos]) {
                    "日汇总" -> {
                        dashboardViewModel.year.observe(viewLifecycleOwner){yea->
                            chart_tv_date.text=yea.toString()+"年"+dashboardViewModel.month.value+"月"+dashboardViewModel.day.value+"日"
                        }
                        dashboardViewModel.month.observe(viewLifecycleOwner){mon->
                            chart_tv_date.text=dashboardViewModel.year.value+"年"+mon.toString()+"月"+dashboardViewModel.day.value+"日"
                        }
                        dashboardViewModel.day.observe(viewLifecycleOwner){da->
                            chart_tv_date.text=dashboardViewModel.year.value+"年"+dashboardViewModel.month.value+"月"+da.toString()+"日"
                        }
                        val aaChartView=root.findViewById<AAChartView>(R.id.aa_chart_view)
                        val datee=root.findViewById<ImageView>(R.id.datee)
                        val calendarr = Calendar.getInstance()
                        dashboardViewModel.changeyear(calendarr.get(Calendar.YEAR).toString())
                        dashboardViewModel.changemonth((calendarr.get(Calendar.MONTH).toString().toInt()+1).toString())
                        dashboardViewModel.changeday(calendarr.get(Calendar.DAY_OF_MONTH).toString())
                        val ca = Calendar.getInstance()
                        mYear = ca.get(Calendar.YEAR)
                        mMonth=ca.get(Calendar.MONTH)
                        mDay = ca.get(Calendar.DAY_OF_MONTH)
                        datee.setOnClickListener {
                            mYear = ca.get(Calendar.YEAR)
                            mMonth=ca.get(Calendar.MONTH)
                            mDay = ca.get(Calendar.DAY_OF_MONTH)
                            val datePickerDialog = DatePickerDialog(
                                    requireContext(),
                                    { _, year, month, dayOfMonth ->
                                        mYear = year
                                        mMonth = month
                                        mDay = dayOfMonth
                                        da()
                                        aaChartView.aa_drawChartWithChartModel(aaChartModel)
                                        dashboardViewModel.changeyear(year.toString())
                                        dashboardViewModel.changemonth((month+1).toString())
                                        dashboardViewModel.changeday((dayOfMonth).toString())
                                    },
                                    mYear, mMonth, mDay
                            )
                            val datePicker = datePickerDialog.datePicker
                            datePicker.maxDate= Date().time //限制最大时间
                            datePickerDialog.show()

                        }
                        da()
                        aaChartView.aa_drawChartWithChartModel(aaChartModel)
                    }
                    "月汇总" -> {
                        dashboardViewModel.year.observe(viewLifecycleOwner){yea->
                            chart_tv_date.text=yea.toString()+"年"+dashboardViewModel.month.value+"月"
                        }
                        dashboardViewModel.month.observe(viewLifecycleOwner){mon->
                            chart_tv_date.text=dashboardViewModel.year.value+"年"+mon.toString()+"月"
                        }
                        val aaChartView=root.findViewById<AAChartView>(R.id.aa_chart_view)
                        val datee=root.findViewById<ImageView>(R.id.datee)
                        val calendarr = Calendar.getInstance()
                        dashboardViewModel.changeyear(calendarr.get(Calendar.YEAR).toString())
                        dashboardViewModel.changemonth((calendarr.get(Calendar.MONTH).toString().toInt()+1).toString())
                        val calendar = Calendar.getInstance()
                        datee.setOnClickListener {
                            mYear = calendar.get(Calendar.YEAR)
                            mMonth = calendar.get(Calendar.MONTH)
                            mDay  = calendar.get(Calendar.DAY_OF_MONTH)
                            //一定要设置 DatePickerDialog 的 style
                            val datePickerDialog= DatePickerDialog(requireContext(), DatePickerDialog.THEME_HOLO_LIGHT,{ _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth,0,0)
                                mYear=year
                                mMonth=month
                                ba()
                                aaChartView.aa_drawChartWithChartModel(aaChartModel)
                                dashboardViewModel.changeyear(year.toString())
                                dashboardViewModel.changemonth((month+1).toString())
                            }, mYear, mMonth, mDay)
                            val datePicker = datePickerDialog.datePicker
                            datePicker.maxDate= Date().time //限制最大时间
                            datePickerDialog.show()
                            ((datePicker.getChildAt(0) as ViewGroup?)?.getChildAt(0)as ViewGroup?)?.getChildAt(2)?.visibility = View.GONE

                        }
                        ba()
                        aaChartView.aa_drawChartWithChartModel(aaChartModel)
                    }
                    "年汇总" -> {
                        dashboardViewModel.year.observe(viewLifecycleOwner){yea->
                            chart_tv_date.text=yea.toString()+"年"
                        }
                        val aaChartView=root.findViewById<AAChartView>(R.id.aa_chart_view)
                        val datee=root.findViewById<ImageView>(R.id.datee)
                        val calendarr = Calendar.getInstance()
                        dashboardViewModel.changeyear(calendarr.get(Calendar.YEAR).toString())
                        val calendar = Calendar.getInstance()
                        datee.setOnClickListener {
                            mYear = calendar.get(Calendar.YEAR)
                            mMonth = calendar.get(Calendar.MONTH)
                            mDay  = calendar.get(Calendar.DAY_OF_MONTH)
                            //一定要设置 DatePickerDialog 的 style
                            val datePickerDialog= DatePickerDialog(requireContext(), DatePickerDialog.THEME_HOLO_LIGHT,{ _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth,0,0)
                                mYear=year
                                ba()
                                aaChartView.aa_drawChartWithChartModel(aaChartModel)
                                dashboardViewModel.changeyear(year.toString())
                            }, mYear, mMonth, mDay)
                            val datePicker = datePickerDialog.datePicker
                            datePicker.maxDate= Date().time //限制最大时间
                            datePickerDialog.show()
                            ((datePicker.getChildAt(0) as ViewGroup?)?.getChildAt(0)as ViewGroup?)?.getChildAt(2)?.visibility = View.GONE
                            ((datePicker.getChildAt(0) as ViewGroup?)?.getChildAt(0)as ViewGroup?)?.getChildAt(1)?.visibility = View.GONE
                        }
                        ka()
                        aaChartView.aa_drawChartWithChartModel(aaChartModel)
                    }

                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return root
    }
    @SuppressLint("SetTextI18n")
    fun da()
    {
        val aa=LongArray(9)
        var cc =0
        var dk:Long=0
        val userdao = AppDatabase.getDatabase(requireContext()).userDao()
        results.clear()
        for(i in userdao.day(mYear.toString(),(mMonth+1).toString(),mDay.toString())) {
            aa[i.id] += i.Amount
            cc++
            dk+=i.Amount
            results.add(i)
        }
        results.reverse()
        adapter.notifyDataSetChanged()
        io.text="共"+cc.toString()+"笔支出,"+"￥"+dk.toString()
        aaChartModel= AAChartModel()
                .chartType(AAChartType.Pie)
                .title("日消费记录")
                .backgroundColor("#FFFFFF")
                .dataLabelsEnabled(true)
                .colorsTheme(arrayOf("#fe117c", "#ffc069", "#06caf4", "#7dffc0","#8CE6DD","#5417B4","#FFEB3B","#FF5722"))
                .series(
                        arrayOf(
                                AASeriesElement()
                                        .borderWidth(0f)
                                        .innerSize("50%")
                                        .data(
                                                arrayOf(
                                                        arrayOf("餐饮", aa[1]), arrayOf("日用", aa[2]), arrayOf("娱乐", aa[3]), arrayOf("学习", aa[4]), arrayOf("医疗", aa[5]),
                                                        arrayOf("服饰",aa[6]),arrayOf("交通",aa[7]), arrayOf("其他",aa[8])
                                                )
                                        ),

                                )
                )
    }
    @SuppressLint("SetTextI18n")
    fun ba()
    {
        val aa=LongArray(9)
        var dk:Long=0
        var cc =0
        val userdao = AppDatabase.getDatabase(requireContext()).userDao()
        results.clear()
        for(i in userdao.yue(mYear.toString(),(mMonth+1).toString())) {
            aa[i.id] += i.Amount
            cc++
            dk+=i.Amount
            results.add(i)
        }
        results.reverse()
        adapter.notifyDataSetChanged()
        io.text="共"+cc.toString()+"笔支出,"+"￥"+dk.toString()
        aaChartModel= AAChartModel()
                .chartType(AAChartType.Pie)
                .title("月消费记录")
                .backgroundColor("#FFFFFF")
                .dataLabelsEnabled(true)
                .colorsTheme(arrayOf("#fe117c", "#ffc069", "#06caf4", "#7dffc0","#8CE6DD","#5417B4","#FFEB3B","#FF5722"))
                .series(
                        arrayOf(
                                AASeriesElement()
                                        .borderWidth(0f)
                                        .innerSize("50%")
                                        .data(
                                                arrayOf(
                                                        arrayOf("餐饮", aa[1]), arrayOf("日用", aa[2]), arrayOf("娱乐", aa[3]), arrayOf("学习", aa[4]), arrayOf("医疗", aa[5]),
                                                        arrayOf("服饰",aa[6]),arrayOf("交通",aa[7]), arrayOf("其他",aa[8])
                                                )
                                        ),

                                )
                )
    }
    @SuppressLint("SetTextI18n")
    fun ka()
    {
        val aa=LongArray(9)
        var dk:Long=0
        var cc =0
        val userdao = AppDatabase.getDatabase(requireContext()).userDao()
        results.clear()
        for(i in userdao.nian(mYear.toString())) {
            aa[i.id] += i.Amount
            cc++
            dk+=i.Amount
            results.add(i)
        }
        results.reverse()
        adapter.notifyDataSetChanged()
        io.text="共"+cc.toString()+"笔支出,"+"￥"+dk.toString()
        aaChartModel= AAChartModel()
                .chartType(AAChartType.Pie)
                .title("年消费记录")
                .backgroundColor("#FFFFFF")
                .dataLabelsEnabled(true)
                .colorsTheme(arrayOf("#fe117c", "#ffc069", "#06caf4", "#7dffc0","#8CE6DD","#5417B4","#FFEB3B","#FF5722"))
                .series(
                        arrayOf(
                                AASeriesElement()
                                        .borderWidth(0f)
                                        .innerSize("50%")
                                        .data(
                                                arrayOf(
                                                        arrayOf("餐饮", aa[1]), arrayOf("日用", aa[2]), arrayOf("娱乐", aa[3]), arrayOf("学习", aa[4]), arrayOf("医疗", aa[5]),
                                                        arrayOf("服饰",aa[6]),arrayOf("交通",aa[7]), arrayOf("其他",aa[8])
                                                )
                                        ),

                                )
                )
    }
}

```
# DashboardViewModel.kt
```cpp
package com.example.myapplication.ui.dashboard
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class DashboardViewModel : ViewModel() {
    private val _year = MutableLiveData<String>().apply {
        value = "2020"
    }
    private val _month = MutableLiveData<String>().apply {
        value = "11"
    }
    private val _day = MutableLiveData<String>().apply {
        value = "13"
    }
    val month: LiveData<String> = _month
    val year: LiveData<String> = _year
    val day: LiveData<String> = _day
    fun changeyear(a:String){
        _year.value=a
    }
    fun changemonth(a:String){
        _month.value=a
    }
    fun changeday(a:String){
        _day.value=a
    }
}

```
# HomeFragment.kt
```cpp
package com.example.myapplication.ui.home
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
class HomeFragment : Fragment() {
    private lateinit var homeViewModel: HomeViewModel
    private val results = ArrayList<User>()
    private lateinit var adapter: Homeadapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        initFruits()
        val fabb=root.findViewById<FloatingActionButton>(R.id.fab)
        val a=root.findViewById<TextView>(R.id.expenditure)
        val b=root.findViewById<TextView>(R.id.eat)
        val c=root.findViewById<TextView>(R.id.others)
        val dd=root.findViewById<TextView>(R.id.lifess)
        val fushi=root.findViewById<TextView>(R.id.clothes)
        val game=root.findViewById<TextView>(R.id.games)
        val study=root.findViewById<TextView>(R.id.studys)
        val yiliao=root.findViewById<TextView>(R.id.hospticals)
        val jiaotong=root.findViewById<TextView>(R.id.cars)
        setHasOptionsMenu(true)
        val mToolbarContact = root.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbarContact)
        fabb.setOnClickListener {
            val intent= Intent(requireContext(), AddActivity::class.java)
            startActivity(intent)
        }
        homeViewModel.jiaotong.observe(viewLifecycleOwner){ d->
           jiaotong.text=d.toString()
        }
        homeViewModel.yiliao.observe(viewLifecycleOwner){ d->
            yiliao.text=d.toString()
        }
        homeViewModel.study.observe(viewLifecycleOwner){ d->
            study.text=d.toString()
        }
        homeViewModel.game.observe(viewLifecycleOwner){ d->
            game.text=d.toString()
        }
        homeViewModel.fushi.observe(viewLifecycleOwner){ d->
            fushi.text=d.toString()
        }
        homeViewModel.zhichu.observe(viewLifecycleOwner){ d->
           a.text=d.toString()
        }
        homeViewModel.eat.observe(viewLifecycleOwner){ d->
            b.text=d.toString()
        }
        homeViewModel.riyong.observe(viewLifecycleOwner){ d->
            dd.text=d.toString()
        }
        homeViewModel.qitaa.observe(viewLifecycleOwner){ d->
            c.text=d.toString()
        }
        val reccc=root.findViewById<RecyclerView>(R.id.recc)
        val userdao = AppDatabase.getDatabase(requireContext()).userDao()
        results.clear()
        for (user in userdao.loadAllUsers())
            results.add(user)
        val lay = GridLayoutManager(requireContext(), 1)
        reccc.layoutManager = lay
        adapter = Homeadapter(requireContext(), results)
        reccc.adapter = adapter
        val intent= Intent(requireContext(), DetailActivity::class.java)
        adapter.setOnItemClickListener(object : Homeadapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                intent.putExtra(DetailActivity.IDD, results[position].idd)
                startActivity(intent)
            }
        })
        val ss=root.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        ss.setOnRefreshListener {
            refreshFruits(adapter)
        }

        return root
    }

    private fun refreshFruits(adapter: Homeadapter) {
        thread {
            Thread.sleep(700)
            activity?.runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                swipeRefresh?.isRefreshing = false
            }
        }
    }
    private fun initFruits() {
        val ca: Calendar = Calendar.getInstance()
        val mYear= ca.get(Calendar.YEAR).toString()
        val mMonth= (ca.get(Calendar.MONTH)+1).toString()
        val userdao = AppDatabase.getDatabase(requireContext()).userDao()
        var k:Long=0
        var a:Long=0
        var c:Long=0
        var o:Long=0
        var g:Long=0
        var s:Long=0
        var y:Long=0
        var f:Long=0
        var j:Long=0
        for(i in userdao.yue(mYear, mMonth)) {
            k += i.Amount
            when(i.Categroy){
                "餐饮" -> a += i.Amount
                "日用" -> c += i.Amount
                "娱乐" -> g+=i.Amount
                "学习" -> s+=i.Amount
                "医疗" -> y+=i.Amount
                "服饰" -> f+=i.Amount
                "交通" ->j+=i.Amount
                else -> o+=i.Amount
            }
        }
        homeViewModel.changejiaotong(j.toString()+"元")
        homeViewModel.changefushi(f.toString()+"元")
        homeViewModel.changeyiliao(y.toString()+"元")
        homeViewModel.changestudy(s.toString()+"元")
        homeViewModel.changegame(g.toString() + "元")
        homeViewModel.changeeat(a.toString() + "元")
        homeViewModel.changeriyong(c.toString() + "元")
        homeViewModel.changeqita(o.toString() + "元")
        homeViewModel.changezhichu(k.toString() + "元")
        results.clear()
        for (user in userdao.loadAllUsers())
            results.add(user)
        results.reverse()
    }

    override fun onStart() {
        super.onStart()
        initFruits()
        adapter.notifyDataSetChanged()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.options_menu, menu)
        val searchManager= activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.menu_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
            setIconifiedByDefault(false)
        }
    }
}

```
# HomeViewModel.kt
```cpp
package com.example.myapplication.ui.home
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class HomeViewModel : ViewModel() {
    private val _zhichu = MutableLiveData<String>().apply {
    }
    private val _riyong = MutableLiveData<String>().apply {
    }
    private val _qita = MutableLiveData<String>().apply {
    }
    private val _eat = MutableLiveData<String>().apply {
    }
    private val _game = MutableLiveData<String>().apply {
    }
    private val _study = MutableLiveData<String>().apply {
    }
    private val _yiliao = MutableLiveData<String>().apply {
    }
    private val _fushi = MutableLiveData<String>().apply {
    }
    private val _jiaotong = MutableLiveData<String>().apply {
    }
    val zhichu: LiveData<String> = _zhichu
    val game :LiveData<String> = _game
    val study:LiveData<String> = _study
    val yiliao:LiveData<String> = _yiliao
    val fushi:LiveData<String> = _fushi
    val jiaotong:LiveData<String> = _jiaotong
    val riyong: LiveData<String> = _riyong
    val eat: LiveData<String> = _eat
    val qitaa: LiveData<String> = _qita
    fun changezhichu(a:String){
        _zhichu.value=a
    }
    fun changeriyong(a:String){
        _riyong.value=a
    }
    fun changeqita(a:String){
        _qita.value=a
    }
    fun changeeat(a:String){
        _eat.value=a
    }
    fun changegame(a:String){
        _game.value=a
    }
    fun changestudy(a:String){
        _study.value=a
    }
    fun changeyiliao(a:String){
        _yiliao.value=a
    }
    fun changefushi(a:String){
        _fushi.value=a
    }
    fun changejiaotong(a:String){
        _jiaotong.value=a
    }
}

```
# NotificationsFragment.kt
```cpp
package com.example.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        notificationsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
        return root
    }
}

```
# NotificationsViewModel.kt
```cpp
package com.example.myapplication.ui.notifications
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class NotificationsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "你的专属记账本!"
    }
    val text: LiveData<String> = _text
}

```
# AddActivity.kt
```cpp
package com.example.myapplication
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.buy_item.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
class AddActivity : AppCompatActivity() {
    private lateinit var adapter: MyAdapter
    private var mMeetName:String=""
    var ik:String="餐饮"
    var ikk:Int=1
    var ic=R.drawable.food
    private val kapians= mutableListOf(
            Cate("餐饮", R.drawable.food, 1),
            Cate("日用", R.drawable.life2, 2),
            Cate("娱乐", R.drawable.game, 3),
            Cate("学习", R.drawable.study, 4),
            Cate("医疗", R.drawable.hospital, 5),
            Cate("服饰", R.drawable.clothes, 6),
            Cate("交通", R.drawable.car, 7),
            Cate("其他", R.drawable.other, 8)
    )
    private val cateList = ArrayList<Cate>()

    @SuppressLint("SetTextI18n")
    private fun showInputDialog() {
        val inputServer = EditText(this)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("请输入备注").setIcon(R.drawable.bbbb).setView(inputServer)
                .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        builder.setPositiveButton("确定") { _, _ ->
            mMeetName = inputServer.text.toString()
            textView.text = "备注:$mMeetName"
            //do something...
        }
        builder.show()
    }
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val userdao = AppDatabase.getDatabase(this).userDao()
        for(i in 0..7)
            cateList.add(kapians[i])
        val layoutManager=LinearLayoutManager(this)
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
       // val lay= GridLayoutManager(this, 4)
        rec.layoutManager=layoutManager
        adapter=MyAdapter(this, cateList)
        rec.adapter=adapter
        adapter.setOnItemClickListener(object : MyAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                ic = cateList[position].imageId
                ik = cateList[position].name
                ikk = cateList[position].id
                itemm3.text = cateList[position].name
                Glide.with(this@AddActivity).load(cateList[position].imageId).into(tupian)
            }
        })
        button.setOnClickListener {
            showInputDialog()
        }
         btn0.setOnClickListener {
             itemm.text=itemm.text.toString()+"0"
         }
        btn1.setOnClickListener {
            itemm.text=itemm.text.toString()+"1"
        }
        btn2.setOnClickListener {
            itemm.text=itemm.text.toString()+"2"
        }
        btn3.setOnClickListener {
            itemm.text=itemm.text.toString()+"3"
        }
        btn4.setOnClickListener {
            itemm.text=itemm.text.toString()+"4"
        }
        btn5.setOnClickListener {
            itemm.text=itemm.text.toString()+"5"
        }
        btn6.setOnClickListener {
            itemm.text=itemm.text.toString()+"6"
        }
        btn7.setOnClickListener {
            itemm.text=itemm.text.toString()+"7"
        }
        btn8.setOnClickListener {
            itemm.text=itemm.text.toString()+"8"
        }
        btn9.setOnClickListener {
            itemm.text=itemm.text.toString()+"9"
        }
        btnClear.setOnClickListener {
            itemm.text=""
            textView.text="备注:"
        }
        btnSubmit.setOnClickListener {
            val formatter= SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss")
            val curDate =Date(System.currentTimeMillis())//获取当前时间
            val str = formatter.format(curDate)
            if(itemm.text.toString().isEmpty())
                Toast.makeText(this, "请输入金额!", Toast.LENGTH_SHORT).show()
            else {
            val ca: Calendar = Calendar.getInstance()
            val mYear= ca.get(Calendar.YEAR).toString()
            val mMonth= (ca.get(Calendar.MONTH)+1).toString()
            val mDay= ca.get(Calendar.DAY_OF_MONTH).toString()
                val aa = itemm.text.toString().toLong()
                val ne = User(ik, aa, mYear, mMonth, mDay, ic, ikk, str,mMeetName)
                userdao.insertUser(ne)
                Toast.makeText(this, "添加账单成功!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}

```
# AppDatabase.kt
```cpp
package com.example.myapplication
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(version =1, entities = [User::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "appp_database")
                    .allowMainThreadQueries()
                    .build().apply {
                        instance = this
                    }
        }
    }
}

```
# Cate.kt
```cpp
package com.example.myapplication
class Cate(val name: String, val imageId: Int,val id:Int)

```
# DetailActivity.kt
```cpp
package com.example.myapplication
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*
class DetailActivity : AppCompatActivity() {
    companion object {
        const val IDD = "yi"
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar31)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val o:Long=0
        val ok=intent.getLongExtra(IDD, o)
        val userdao = AppDatabase.getDatabase(this).userDao()
        if (ok != o)
            for (user in userdao.chazhao(ok)) {
                Glide.with(this).load(user.Tupianid).into(tupian21)
                   textView21.text="金额: "+user.Amount.toString()+"元"
                   textView31.text="时间: "+user.times
                   textView1.text="备注: "+user.ba
                   itemm4.text=user.Categroy
            }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

```
# Homeadapter.kt
```cpp
package com.example.myapplication
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class Homeadapter(private val context: Context, private val fruitList: ArrayList<User>) : RecyclerView.Adapter<Homeadapter.ViewHolder>() {
    private lateinit var  onItemClickListener: OnItemClickListener
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeImage: ImageView = view.findViewById(R.id.tupiann)
        val hometime: TextView = view.findViewById(R.id.timm)
        val mon: TextView = view.findViewById(R.id.jine)
        val z: TextView = view.findViewById(R.id.zhonglei)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            onItemClickListener.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            val userdao = AppDatabase.getDatabase(context as MainActivity).userDao()
            AlertDialog.Builder(context).apply {
                setTitle("删除")
                setMessage("确定删除这条账单?")
                setCancelable(false)
                setPositiveButton("删除") { _, _ ->
                    userdao.deleteUserbyidd(fruitList[position].idd)
                    fruitList.remove(fruitList[position])
                    notifyItemRemoved(position)
                }
                setNegativeButton("取消")
                { _, _ -> }
                show()
            }
            false
        }
        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.mon.text=fruit.Amount.toString()
        holder.hometime.text=fruit.times
        holder.z.text=fruit.Categroy
        Glide.with(context).load(fruit.Tupianid).into(holder.homeImage)
    }

    override fun getItemCount() = fruitList.size
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}

```
# MainActivity.kt
```cpp
package com.example.myapplication
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        setSupportActionBar(toolbar)
    }
}

```
# MyAdapter.kt
```cpp
package com.example.myapplication
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class MyAdapter(private val context: Context, private val fruitList: List<Cate>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private lateinit var  onItemClickListener: OnItemClickListener
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val fruitImage: ImageView = view.findViewById(R.id.cateImage)
        val fruitName: TextView = view.findViewById(R.id.cateName)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cate_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            onItemClickListener.onItemClick(position)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.fruitName.text = fruit.name
        Glide.with(context).load(fruit.imageId).into(holder.fruitImage)
    }
    override fun getItemCount() = fruitList.size
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}

```
# SearchableActivity.kt
```cpp
package com.example.myapplication
import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_searchable.*
class SearchableActivity : AppCompatActivity() {
    private val results = ArrayList<User>()
    private lateinit var adapter: Searchadapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val userdao = AppDatabase.getDatabase(this).userDao()
        val lay = GridLayoutManager(this, 1)
        rec2.layoutManager = lay
        adapter = Searchadapter(this, results)
        rec2.adapter = adapter
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                for(i in userdao.chaxun(query))
                    results.add(i)
                    adapter.notifyDataSetChanged()
            }
        }
        val intent= Intent(this, DetailActivity::class.java)
        adapter.setOnItemClickListener(object : Searchadapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                intent.putExtra(DetailActivity.IDD, results[position].idd)
                startActivity(intent)
            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

```
# Searchadapter.kt
```cpp
package com.example.myapplication
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
class Searchadapter(private val context: Context, private val fruitList: ArrayList<User>) : RecyclerView.Adapter<Searchadapter.ViewHolder>() {
    private lateinit var  onItemClickListener: OnItemClickListener
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val homeImage: ImageView = view.findViewById(R.id.tupiann)
        val hometime: TextView = view.findViewById(R.id.timm)
        val mon: TextView = view.findViewById(R.id.jine)
        val z: TextView = view.findViewById(R.id.zhonglei)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.home_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            onItemClickListener.onItemClick(position)
        }
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            val userdao = AppDatabase.getDatabase(context as SearchableActivity).userDao()
            AlertDialog.Builder(context).apply {
                setTitle("删除")
                setMessage("确定删除这条账单?")
                setCancelable(false)
                setPositiveButton("删除") { _, _ ->
                    userdao.deleteUserbyidd(fruitList[position].idd)
                    fruitList.remove(fruitList[position])
                    notifyItemRemoved(position)
                }
                setNegativeButton("取消")
                { _, _ -> }
                show()
            }
            false
        }
        return holder
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fruit = fruitList[position]
        holder.mon.text=fruit.Amount.toString()
        holder.hometime.text=fruit.times
        holder.z.text=fruit.Categroy
        Glide.with(context).load(fruit.Tupianid).into(holder.homeImage)
    }
    override fun getItemCount() = fruitList.size
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }
}

```
# User.kt
```cpp
package com.example.myapplication
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(var Categroy:String, var Amount:Long,var Year: String,var Month:String,var Day:String,var Tupianid:Int,var id:Int,var times:String,var ba:String) {
    @PrimaryKey(autoGenerate = true)
    var idd: Long = 0
}

```
# UserDao.kt
```cpp
package com.example.myapplication
import androidx.room.*
@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User): Long
    @Update
    fun updateUser(newUser: User)
    @Query("select * from User")
    fun loadAllUsers(): List<User>
    @Query("select * from User where idd = :idd")
    fun chazhao(idd: Long): List<User>
    @Delete
    fun deleteUser(user: User)
    @Query("delete from User where idd = :idd")
    fun deleteUserbyidd(idd: Long): Int
    @Query("select * from User where Year=:aa")
    fun nian(aa:String):List<User>
    @Query("select * from User where Year=:aa and Month=:bb")
    fun yue(aa:String,bb:String):List<User>
    @Query("select * from User where Year=:aa and Month=:bb and Day=:cc" )
    fun day(aa:String,bb:String,cc:String):List<User>
    @Query("select * from User where Categroy like'%'||:aa||'%' or Amount like'%'||:aa||'%' or times like'%'||:aa||'%'")
    fun chaxun(aa:String): List<User>
}

```
