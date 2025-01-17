package com.example.klivvrproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.serialization.json.Json
import com.example.klivvrproject.model.City

class CityViewModel : ViewModel() {
    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> get() = _cities

    private val _filteredCities = MutableLiveData<List<City>>()
    val filteredCities: LiveData<List<City>> get() = _filteredCities

    fun loadCities(jsonString: String) {
        val citiesList = Json.decodeFromString<List<City>>(jsonString)
        _cities.value = citiesList
        _filteredCities.value = citiesList.sortedBy { it.name }
    }

    fun filterCities(prefix: String) {
        if(prefix.isEmpty()){
            _filteredCities.value = _cities.value?.sortedBy { it.name }
        }else{
            val filteredCities= _cities.value?.let { cities ->
                val citiesSearch = cities.filter { it.name.startsWith(prefix, ignoreCase = true) }
                val countriesSearch = cities.filter { it.country.startsWith(prefix, ignoreCase = true) }
                citiesSearch + countriesSearch
            } ?: emptyList()
            _filteredCities.value = filteredCities
        }

    }
}
