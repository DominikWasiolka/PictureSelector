package com.example.pictureselector

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.big_picture_fragment.view.*


class BigPictureFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("CUSTOM","load big picture layout")
        val view = inflater!!.inflate(R.layout.big_picture_fragment, container, false)
        if (arguments != null) {
            val url = arguments!!.getString("url")
            Log.d("CUSTOM", "load big picture layout; url: " + url)
            setUrlBitmap(url, view)
        }
        else
            Log.d("CUSTOM", "argument in load big picture layout is null" )

        return view
    }

    fun setUrlBitmap(url:String, view: View){
        Picasso.get()
            .load(url)
            .into(object : com.squareup.picasso.Target {

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {

                    view.bigImage.setImageBitmap(bitmap)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            })
    }
}