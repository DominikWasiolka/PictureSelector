package com.example.pictureselector

import androidx.fragment.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.info_fragment.view.*
import kotlinx.android.synthetic.main.list_item.view.*

class InfoFragment : Fragment(){
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("CUSTOM","load info fragment layout")

        val view = inflater!!.inflate(R.layout.info_fragment, container, false)
        if (arguments != null) {
            val img = arguments!!.getSerializable("imageCard") as ImageCard

            view.inf_title.text = img.title
            view.inf_url.text = img.url
            view.inf_date.text = img.generateDateString()
            view.inf_tags.text = img.generateTagSting()
        }
        else
            Log.d("CUSTOM", "argument in info framgent is null" )

        return view
    }
}