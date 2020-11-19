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
        initFruits()
        return root
    }

    private fun refreshFruits(adapter: Homeadapter) {
        thread {
            Thread.sleep(700)
            activity?.runOnUiThread {
                initFruits()
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
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