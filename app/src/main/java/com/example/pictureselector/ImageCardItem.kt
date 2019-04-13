package com.example.pictureselector

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.list_item.view.*
import android.graphics.drawable.Drawable


class ImageCardItem(val img: ImageCard): Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        //var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        viewHolder.itemView.title.text = img.title
        //viewHolder.itemView.tags.text = img.generateTagSting()
        viewHolder.itemView.date.text = img.date//formatter.format(img.date)
        //viewHolder.itemView.image.setImageBitmap(img.bitmap) //tutaj wywołanie obrazu

        if (img.generateTagSting() == "") {
            Log.d("CUSTOM", "prepare to set url")
            setUrlBitmap(img.url,viewHolder)
        }
        else{
            val thubnailImageView = viewHolder.itemView.image
            Picasso.get().load(img.url).into(thubnailImageView)
            viewHolder.itemView.tags.text = img.generateTagSting()
        }
    }

    override fun getLayout(): Int {
        return R.layout.list_item
    }

    fun setUrlBitmap(url:String, viewHolder: ViewHolder){
        Picasso.get()
            .load(url)
            .into(object : com.squareup.picasso.Target {

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {

                    val str:String = processImageTagging(bitmap, viewHolder)
                    //img.tags = splitTags(str)
                    //viewHolder.itemView.tags.text = str
                    viewHolder.itemView.image.setImageBitmap(bitmap)
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            })
        //return pictureBitmap

    }

    fun splitTags(inputString: String) :List<String>{

        val list_of_tags = inputString.split(", ")
        if (list_of_tags.size<4)
            return list_of_tags
        return list_of_tags.slice(IntRange(0,2))
    }

    private fun processImageTagging(bitmap: Bitmap, viewHolder: ViewHolder) : String {
        val visionImg: FirebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap)
        val labeler: FirebaseVisionImageLabeler = FirebaseVision.getInstance().onDeviceImageLabeler
        var outputStirng =""
        labeler.processImage(visionImg)
            .addOnSuccessListener { tags->
                val str = tags.joinToString ( ", " ){it.text}
                img.tags = splitTags(str)
                viewHolder.itemView.tags.text = img.generateTagSting()
                Log.d("CUSTOM", "tags: " + str)
            }
            .addOnFailureListener{ex ->
                Log.d("CUSTOM", "error tagging: " + ex.toString())
            }
        return outputStirng
    }
}