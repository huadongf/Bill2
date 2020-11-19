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