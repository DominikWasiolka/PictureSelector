package com.example.pictureselector

import java.io.Serializable

class ImageCard(val title:String, val url:String, val tags:List<String>, val date: String) : Serializable {//, val bitmap:Bitmap){
    //companion object {
        fun generateTagSting() :String {
            var str = ""
            for (tag in this.tags)
                str += tag + " "
            return str
        }
    //}
}