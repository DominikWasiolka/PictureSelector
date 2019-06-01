package com.example.pictureselector

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import androidx.recyclerview.widget.DividerItemDecoration
import java.time.LocalDate


class MainActivity : AppCompatActivity() {

    val MAX_NUMBER_OF_SIMILAR_IMAGES = 6
    private val ADD_NEW_URL_IMAGE = 255
    private var swipeDeleteBackground: ColorDrawable = ColorDrawable(Color.parseColor("red"))
    //private var swipeInfoBackground: ColorDrawable = ColorDrawable(Color.parseColor("blue"))
    private lateinit var deleteIcon: Drawable
    var imageCardList:LinkedList<ImageCard> = LinkedList <ImageCard>()
    var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Log.d("CUSTOM", "list Size "+imageCardList.size.toString())
        Log.d("CUSTOM", "created adapter")

        for (imageCard in imageCardList) {
            Log.d("CUSTOM", "Added element to imageCardList")
            adapter.add(ImageCardItem(imageCard))
        }

        recycleview_images.adapter = adapter
        recycleview_images.layoutManager = LinearLayoutManager(this)

        //horizontal line for items separating
        val dividerItemDecoration = DividerItemDecoration( // lines between items in recycleview
            recycleview_images.getContext(),
            DividerItemDecoration.VERTICAL
        )
        recycleview_images.addItemDecoration(dividerItemDecoration)

        // for swipe menu
        addSwipeMenu()

    }

    fun addSwipeMenu(){
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        val  itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("CUSTOM", "swipe direction " + direction.toString())

                if (direction==8) {
                    // delete item
                    Log.d("CUSTOM", "adapter position " + viewHolder.adapterPosition.toString())
                    imageCardList.removeAt(viewHolder.adapterPosition)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    adapter.removeGroup(imageCardList.size)//remove last free field
                }
                else {
                    val imageCard = imageCardList[viewHolder.adapterPosition]
                    openDetailsActivity(imageCard)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val iconPosition = (itemView.height - deleteIcon.intrinsicHeight) /2

                if (dX>0){
                    swipeDeleteBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconPosition, itemView.top + iconPosition,
                        itemView.left+iconPosition + deleteIcon.intrinsicWidth, itemView.bottom - iconPosition)

                    swipeDeleteBackground.draw(c)
                    c.save()

                    c.clipRect(swipeDeleteBackground.bounds)

                    deleteIcon.draw(c)
                    c.restore()

                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycleview_images)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.add_image -> {
                openAboutActivity()
                return super.onOptionsItemSelected(item)
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun openAboutActivity(){
        val intent = Intent(this,AddImage::class.java)
        startActivityForResult(intent, ADD_NEW_URL_IMAGE)
    }

    fun openDetailsActivity(imageCard:ImageCard){
        val intent = Intent(this,ImageDetails::class.java)
        intent.putExtra("imageCard", imageCard)

        val similarUrls = findSimilarImages(imageCard)
        intent.putExtra("similarUrls",similarUrls)

        Log.d("CUSTOM", "Extra similarUrls ok")
        startActivityForResult(intent, ADD_NEW_URL_IMAGE)
    }

    fun generateExampleImages(){
        val url1 = "https://www.w3schools.com/w3images/lights.jpg"
        val url2 = "https://www.w3schools.com/w3images/nature.jpg"
        val url3 = "https://www.w3schools.com/w3images/fjords.jpg"

        val result1 = ImageCard("title1",url1, listOf("tag1","tag2"), LocalDate.now())
        imageCardList.add(result1)

        val result2 = ImageCard("title2",url2,listOf("tag1","tag2"), LocalDate.now())
        imageCardList.add(result2)

        val result3 = ImageCard("title3",url3,listOf(),  LocalDate.now())
        imageCardList.add(result3)
    }

    fun findSimilarImages(currentImage: ImageCard):String {
        var iterator = 0
        var urls = ""
        var urlList = Array<String>(6){""}
        for (img in imageCardList){
            if(currentImage.generateTagSting() == img.generateTagSting()) {
                urls+=img.url+" "
                urlList[iterator]=img.url
                iterator+=1
                if (iterator == MAX_NUMBER_OF_SIMILAR_IMAGES)
                    break
            }
        }
        Log.d("CUSTOM", "Similar Urls were generated")
        return urls
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK)
            when (requestCode){
                ADD_NEW_URL_IMAGE -> {
                    val returnedImageCard :ImageCard = data?.getSerializableExtra(getString(R.string.extra_ret_img_card)) as ImageCard
                    imageCardList.add(returnedImageCard)
                    adapter.add(ImageCardItem(returnedImageCard))
                    adapter?.notifyDataSetChanged()

                    Log.d("CUSTOM", "list Size "+imageCardList.size.toString())
                }
            }
    }
}

