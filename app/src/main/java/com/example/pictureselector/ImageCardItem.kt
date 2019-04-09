package com.example.pictureselector

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.list_item.view.*

class ImageCardItem(val img: ImageCard): Item<ViewHolder>(){


    override fun bind(viewHolder: ViewHolder, position: Int) {
        //var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        viewHolder.itemView.title.text = img.title
        viewHolder.itemView.tags.text = img.generateTagSting()
        viewHolder.itemView.date.text = img.date//formatter.format(img.date)
        //viewHolder.itemView.image.setImageBitmap(img.bitmap) //tutaj wywołanie obrazu
        val thubnailImageView = viewHolder.itemView.image
        Picasso.get().load(img.url).into(thubnailImageView)
    }

    override fun getLayout(): Int {
        return R.layout.list_item
    }
}