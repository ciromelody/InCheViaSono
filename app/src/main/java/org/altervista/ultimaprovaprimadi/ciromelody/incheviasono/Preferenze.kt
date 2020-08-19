package org.altervista.ultimaprovaprimadi.ciromelody.incheviasono

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class Preferenze(context: Context) {
    private val sharedPreferences: SharedPreferences
    fun saveValue(latitudine: Double, longitudine: Double) {
        sharedPreferences.edit()
            .putString(LATITUDINE, java.lang.Double.toString(latitudine))
            .apply()
        sharedPreferences.edit()
            .putString(LONGITUDINE, java.lang.Double.toString(longitudine))
            .apply()
    }

    fun loadValue(): HashMap<String, Double> {
        val cordList =
            HashMap<String, Double>()
        cordList[LATITUDINE] = sharedPreferences.getString(LATITUDINE, "0.0")!!.toDouble()
        cordList[LONGITUDINE] = sharedPreferences.getString(LONGITUDINE, "0.0")!!.toDouble()
        return cordList
    }

    companion object {
        const val LATITUDINE = "latitudine"
        const val LONGITUDINE = "longitudine"
    }

    init {
        sharedPreferences = context.getSharedPreferences(
            Preferenze::class.java.name,
            Context.MODE_PRIVATE
        )
    }
}