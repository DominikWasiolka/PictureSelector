package com.example.pictureselector

import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ImageCard(val title:String, val url:String, var tags:List<String>, val date: LocalDate) : Serializable {//, val bitmap:Bitmap){
    //companion object {
        fun generateTagSting() :String {
            var str = ""
            for (tag in this.tags)
                str += tag + " "
            return str
        }

        fun generateDateString():String{
            var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return formatter.format(date)
        }
    //}
}