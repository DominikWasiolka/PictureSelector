package com.example.pictureselector

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddImage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)

        val button = findViewById(R.id.save_button) as Button
        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val url = (findViewById(R.id.url) as EditText).getText().toString()

                if (url.isEmpty()){
                    whenUrlEmpty()
                    return
                }
                val title = (findViewById(R.id.title) as EditText).getText().toString()
                val tags = (findViewById(R.id.tags) as EditText).getText().toString()
                val date = (findViewById(R.id.date) as EditText).getText().toString()

                //if (date.isEmpty())
                    //Get current date
                val newImageCard = ImageCard(title,url,splitTags(tags),date)

                val returnIntent = Intent()
                returnIntent.putExtra(getString(R.string.extra_ret_img_card), newImageCard)

                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })
    }

    fun splitTags(inputString: String) :List<String>{

        val list_of_tags = inputString.split(", ")
        if (list_of_tags.size<4)
            return list_of_tags
        return list_of_tags.slice(IntRange(0,2))
    }

    fun whenUrlEmpty(){
        Toast.makeText(applicationContext, "Url cannot be empty", Toast.LENGTH_SHORT).show()
    }
}
