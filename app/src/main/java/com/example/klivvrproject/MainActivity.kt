package com.example.klivvrproject

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.klivvrproject.adapter.CityAdapter
import com.example.klivvrproject.databinding.ActivityMainBinding
import com.example.klivvrproject.model.City
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {
    private lateinit var cities: List<City>
    private lateinit var binding: ActivityMainBinding
    private lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cities = loadCitiesFromAssets(this).sortedBy { it.name.lowercase()}
        binding.rvCities.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(cities)
        binding.rvCities.adapter = cityAdapter
    }
    
    private fun loadCitiesFromAssets(context: Context): List<City> {
        val inputStream = context.assets.open("cities.json")
        val jsonString = inputStream.bufferedReader().use {
            it.readText() }
        return Json.decodeFromString(jsonString)
    }

}