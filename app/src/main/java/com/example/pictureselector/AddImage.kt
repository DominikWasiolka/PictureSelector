package com.example.pictureselector

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.time.LocalDate
import java.time.Month
import java.util.*

class AddImage : AppCompatActivity() {

    var date: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_image)

        val button = findViewById<Button>(R.id.save_button)
        button.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val url = (findViewById<EditText>(R.id.url)).text.toString()

                if (url.isEmpty()){
                    whenUrlEmpty()
                    return
                }
                val title = (findViewById<EditText>(R.id.title)).text.toString()
                val tags = (findViewById<EditText>(R.id.tags)).text.toString()

                val newImageCard = ImageCard(title,url,splitTags(tags),date)

                val returnIntent = Intent()
                returnIntent.putExtra(getString(R.string.extra_ret_img_card), newImageCard)

                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        })

        val dateButton = findViewById<Button>(R.id.dateButton)
        dateButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                val cal = Calendar.getInstance()

                val dateSetListener = DatePickerDialog.OnDateSetListener{ datePicker, year, month, day ->

                    date = LocalDate.of(year, Month.of(month+1),day) // +1 because for DatePicker January = 0
                    findViewById<Button>(R.id.dateButton).text = date.toString()

                    Log.d("CUSTOM", "date: "+year.toString()+"-"+month.toString()+"-"+day.toString())
                }
                DatePickerDialog(this@AddImage, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        })
    }

    fun splitTags(inputString: String) :List<String>{
        if(inputString.isEmpty())
            return listOf()

        val list_of_tags = inputString.split(", ")
        if (list_of_tags.size<4)
            return list_of_tags
        return list_of_tags.slice(IntRange(0,2))
    }

    fun whenUrlEmpty(){
        Toast.makeText(applicationContext, "Url cannot be empty", Toast.LENGTH_SHORT).show()
    }
}
