package com.example.klivvrproject

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View

import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klivvrproject.adapter.CityAdapter
import com.example.klivvrproject.databinding.ActivityMainBinding
import com.example.klivvrproject.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cityAdapter: CityAdapter
    private lateinit var cities: List<City>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureData()
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }
    }

    private fun configureData(){
        cityAdapter = CityAdapter(emptyList())
        isLoading()
        binding.searchInput.addTextChangedListener { text ->
            filterCities(text.toString())
        }
        getCities()
    }

    private fun getCities(){
        lifecycleScope.launch {
            try {
                cities = loadCitiesFromAssets(this@MainActivity).sortedBy { it.name.lowercase() }
                withContext(Dispatchers.Main) {
                    cityAdapter = CityAdapter(cities)
                    binding.rvCities.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvCities.adapter = cityAdapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("MainActivity", "Error loading cities", e)
                }
            }finally {
                hideLoading()
            }
        }
    }

    private fun loadCitiesFromAssets(context: Context): List<City> {
        val inputStream = context.assets.open("cities.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return Json.decodeFromString(jsonString)
    }

    private fun filterCities(prefix: String) {
        val citiesList :List<City>
        if (prefix.isEmpty()){
            citiesList = cities
        }else{
            val filteredCities = cities.filter {
                it.name.startsWith(prefix, ignoreCase = true)
            }
            val filteredCounter = cities.filter {
                it.country.startsWith(prefix, ignoreCase = true)
            }
            citiesList = filteredCities + filteredCounter
        }
        cityAdapter.updateData(citiesList)
        binding.rvCities.scrollToPosition(0)
    }

    private fun isLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("cities", Json.encodeToString(ListSerializer(City.serializer()), cities))
        outState.putString("searchQuery", binding.searchInput.text.toString())
    }

    private fun restoreState(savedInstanceState: Bundle) {
        val savedCities = savedInstanceState.getString("cities")
        val searchQuery = savedInstanceState.getString("searchQuery")

        if (savedCities != null) {
            cities = Json.decodeFromString(ListSerializer(City.serializer()), savedCities)
            cityAdapter.updateData(cities)
        }
        if (searchQuery != null) {
            binding.searchInput.setText(searchQuery)
            filterCities(searchQuery)
        }
    }

}
