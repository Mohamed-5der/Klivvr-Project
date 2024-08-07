package com.example.klivvrproject.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.klivvrproject.R
import com.example.klivvrproject.model.City


class CityAdapter(private var cities: List<City>) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_rv, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = cities[position]
        holder.bind(city)

    }

    override fun getItemCount(): Int = cities.size

    fun updateData(newCities: List<City>) {
        cities = newCities
        notifyDataSetChanged()
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.cityName)
        val cityCoordinates: TextView = itemView.findViewById(R.id.cityCoordinates)

        fun bind( city: City){
            cityName.text = "${city.name}, ${city.country}"
            cityCoordinates.text = "Lat: ${city.coordinates.lat} , Lon: ${city.coordinates.lon}"
            itemView.setOnClickListener {
                val uri = "geo:${city.coordinates.lat},${city.coordinates.lon}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                if (intent.resolveActivity(it.context.packageManager) != null) {
                    startActivity(it.context, intent, null)
                }else {
                    val browserUri = "https://www.google.com/maps/search/?api=1&query=${city.coordinates.lat},${city.coordinates.lon}"
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(browserUri))
                    startActivity(it.context,browserIntent,null)
                }
            }
        }
    }
}