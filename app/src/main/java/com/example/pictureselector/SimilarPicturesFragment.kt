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
import kotlinx.android.synthetic.main.similar_pictures_fragment.view.*


class SimilarPicturesFragment : Fragment(){
    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("CUSTOM","load similar pictures layout")
        var i=1
        val view = inflater!!.inflate(R.layout.similar_pictures_fragment, container, false)
        if (arguments != null) {
            val urls = arguments!!.getString("similarUrls").split(" ")
            Log.d("CUSTOM", "URLs size " + urls.size.toString())
            urls.dropLast(3)
            Log.d("CUSTOM", "URLs size " + urls.size.toString())
            for (url in urls){
                Log.d("CUSTOM-similarUrls",url)
                if(url!="")
                    setUrlBitmap(url,view,i)
                i++
            }
        }
        return view
    }

    fun setUrlBitmap(url:String, view: View, id:Int){
        Picasso.get()
            .load(url)
            .into(object : com.squareup.picasso.Target {

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                    when(id){
                        1->view.image_1_1.setImageBitmap(bitmap)
                        2->view.image_1_2.setImageBitmap(bitmap)
                        3->view.image_1_3.setImageBitmap(bitmap)
                        4->view.image_2_1.setImageBitmap(bitmap)
                        5->view.image_2_2.setImageBitmap(bitmap)
                        6->view.image_2_3.setImageBitmap(bitmap)
                    }

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            })

    }
}