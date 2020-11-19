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