package com.example.pictureselector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import java.util.*

class ImageDetails : AppCompatActivity() {

    var isBigPictureFragmentLoaded = true
    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_details)

        val Change = findViewById<Button>(R.id.btn_changeView)

        val imageCard = intent.extras.getSerializable("imageCard") as ImageCard
        val similarUrls = intent.extras.getString("similarUrls")
        Log.d("CUSTOM", "Image details loaded" )
        Log.d("CUSTOM", similarUrls )

        showBigPictureFragment(imageCard.url)
        Change.setOnClickListener {
            if(isBigPictureFragmentLoaded){
                showInfoPictureFragment(imageCard)
                showSimilarPictureFragment(similarUrls)
            }
            else
                showBigPictureFragment(imageCard.url)
        }
    }

    fun showBigPictureFragment(url: String){
        val bundle = Bundle()
        bundle.putString("url", url)

        val transaction = manager.beginTransaction()
        val fragment = BigPictureFragment()
        fragment.arguments = bundle

        findViewById<FrameLayout>(R.id.bottom).removeAllViewsInLayout()
        transaction.replace(R.id.top,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isBigPictureFragmentLoaded = true
    }

    fun showInfoPictureFragment(imageCard: ImageCard){
        val bundle = Bundle()
        bundle.putSerializable("imageCard", imageCard)

        val transaction = manager.beginTransaction()
        val fragment = InfoFragment()
        fragment.arguments = bundle

        transaction.replace(R.id.top,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isBigPictureFragmentLoaded = false
    }

    fun showSimilarPictureFragment(similarUrls: String){
        val bundle = Bundle()
        bundle.putSerializable("similarUrls", similarUrls)
        Log.d("CUSTOM", similarUrls )

        val transaction = manager.beginTransaction()
        val fragment = SimilarPicturesFragment()
        fragment.arguments = bundle
        transaction.replace(R.id.bottom,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
