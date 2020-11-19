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
        val formatter= SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss")
        val curDate =Date(System.currentTimeMillis())//获取当前时间
        val str = formatter.format(curDate)
        btnSubmit.setOnClickListener {
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