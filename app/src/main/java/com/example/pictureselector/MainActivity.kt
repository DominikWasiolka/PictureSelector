package com.example.pictureselector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var PICK_PHOTO_FOR_AVATAR = 256
    //private var image : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activateButton.setOnClickListener(){
            pickImage()
        }

    }

    private fun pickImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type ="image/*"
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR)
    }// podpiąć to pod jakiś guzik;

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
            when (requestCode){
                PICK_PHOTO_FOR_AVATAR -> {
                    val selectedImage: Uri? = data!!.data
                    var image : Bitmap
                    image = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    contentIV.setImageBitmap(image)

                    val img = FirebaseVisionImage.fromBitmap(image) // obraz, który może być przetwarzany przez image

                    //val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()

                }
            }
    }
/*
    private fun processImageTagging(){
        image?.apply(this
        val tagsDetector.detect(toFirebaseVisonImage())
            .addOnSuccessLIstener(it
            processTags(it)
            ).addONFailureListener( iterator(Log.e(tag)))
        )
    }

    private fun processTags(tags: List<FirebaseVisionImageLabel>){
        val res:List<String!> = tags.map (it.map)
        Log.d("TAG", res.joinToString(" "))
    }*/
}
