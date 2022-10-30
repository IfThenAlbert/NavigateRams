package com.fsc.navigaterams

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class BuildingListAdapter(private val c: Activity,private val building_names:Array<String>, private val building_desc:Array<String> ):ArrayAdapter<String>(c,R.layout.building_listings,building_names) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = c.layoutInflater
        val rowView = inflater.inflate(R.layout.building_listings,null,true)

        val txt_b_name = rowView.findViewById(R.id.txt_building_name) as TextView
        val txt_b_desc = rowView.findViewById(R.id.txt_description) as TextView

        txt_b_name.setText(building_names.get(position))
        txt_b_desc.setText(building_desc.get(position))

        return  rowView
    }


}