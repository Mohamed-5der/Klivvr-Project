package com.example.klivvrproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klivvrproject.adapter.CityAdapter
import com.example.klivvrproject.databinding.ActivityMainBinding
import com.example.klivvrproject.viewmodel.CityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cityAdapter: CityAdapter
    private val cityViewModel: CityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureData()
        if (cityViewModel.cities.value == null) {
            loadCities()
        }else{
            hideLoading()
        }
    }

    private fun configureData(){
        showLoading()
        cityAdapter = CityAdapter(emptyList())
        binding.rvCities.layoutManager = LinearLayoutManager(this)
        binding.rvCities.adapter = cityAdapter
        cityViewModel.filteredCities.observe(this, Observer { filteredCities ->
            cityAdapter.updateData(filteredCities)
            binding.rvCities.scrollToPosition(0)
        })

        binding.searchInput.addTextChangedListener { text ->
            cityViewModel.filterCities(text.toString())
        }
    }

    private fun loadCities() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val jsonString = loadCitiesFromAssets(this@MainActivity)
                withContext(Dispatchers.Main) {
                    cityViewModel.loadCities(jsonString)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MainActivity", "Error loading cities", e)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    hideLoading()
                }
            }
        }
    }

    private fun loadCitiesFromAssets(context: Context): String {
        val inputStream = context.assets.open("cities.json")
        return inputStream.bufferedReader().use { it.readText() }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }
}
