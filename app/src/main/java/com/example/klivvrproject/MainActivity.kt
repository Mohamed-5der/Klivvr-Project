package com.example.klivvrproject

import android.content.Context
import android.os.Bundle
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

        cityAdapter = CityAdapter(emptyList())


        lifecycleScope.launch {
            try {
                cities = loadCitiesFromAssets(this@MainActivity).sortedBy { it.name.lowercase() }
                withContext(Dispatchers.Main) {
                    cityAdapter = CityAdapter(cities)
                    binding.rvCities.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rvCities.adapter = cityAdapter
                    binding.progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

             binding.searchInput.addTextChangedListener {text ->
                filterCities(text.toString())
             }
    }

    private fun loadCitiesFromAssets(context: Context): List<City> {
        val inputStream = context.assets.open("cities.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return Json.decodeFromString(jsonString)
    }



    private fun filterCities(prefix: String) {
        val filteredCities = cities.filter {
            it.name.startsWith(prefix, ignoreCase = true)
        }
        cityAdapter.updateData(filteredCities)
    }

}
