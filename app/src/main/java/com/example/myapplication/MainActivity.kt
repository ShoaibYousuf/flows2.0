package com.example.myapplication


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding:ActivityMainBinding
    private var recyclerView: RecyclerView? = null
    private lateinit var adapter: Adapter
    lateinit var linearLayoutManager: LinearLayoutManager
    var arrayList: ArrayList<DataClass> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerview.layoutManager = linearLayoutManager
        adapter = Adapter(this,arrayList)
        recyclerView!!.adapter = adapter

        binding.fetchDataBtn.setOnClickListener {
            viewModel.fetchData()
        }

        lifecycleScope.launch {
            viewModel.loadingState.collect {
                binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
            }
        }

//        lifecycleScope.launch {
//            viewModel.data.collect { data -> binding.dataTv.text = data.toString()
//            }
//        }
    }
}
