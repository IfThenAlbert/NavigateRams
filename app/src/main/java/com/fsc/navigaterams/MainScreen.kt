package com.fsc.navigaterams

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.jar.Manifest

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        // show the info
        POPULATE_LIST()
    }

    // UI events ==========================================================================================================================

    // when user tap on the CAMPUS MAP button
    fun clicick_campus_button(v: View) {



        // direct them to the map screen
        startActivity(Intent(applicationContext,CampusMapScreen::class.java))
    }

    // prevent user from going back using the device back button
    override fun onBackPressed() {

    }


    // CUSTOM ===============================================================================================================================
    // a function that shows the buildings and informations
    fun POPULATE_LIST() {
        var lst:ListView = findViewById(R.id.lst_buildings)

        val URL:String = getString(R.string.fsc_infos)

        // store all result
        var buildings_names = arrayListOf<String>()
        var buildings_desc = arrayListOf<String>()

        // have a queue object to get the json (VOLLEY)
        val queue: RequestQueue = Volley.newRequestQueue(applicationContext)

        // request to read the json
        val requestingToGet = JsonObjectRequest(Request.Method.GET,URL,null,{
                response->
            // go to the first level of the json
            val data = response.getJSONArray("fsc_info")

            // access the data of the first level (json array)
            for(i in 0 until data.length()) {
                val obj = data.getJSONObject(i)
                val building:String = obj.getString("BUILDING")
                val description:String = obj.getString("DESCRIPTION")
                buildings_names.add(building)
                buildings_desc.add(description)
            }

            var adap:BuildingListAdapter = BuildingListAdapter(this,buildings_names.toTypedArray(),buildings_desc.toTypedArray())
            lst.adapter = adap

        },{
                error->
        })

        queue.add(requestingToGet)

    }

}