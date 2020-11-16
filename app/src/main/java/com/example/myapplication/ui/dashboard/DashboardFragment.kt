package com.example.myapplication.ui.dashboard

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import com.example.myapplication.AppDatabase
import com.example.myapplication.Homeadapter
import com.example.myapplication.R
import com.example.myapplication.User
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
        adapter.setOnItemClickListener(object : Homeadapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
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

