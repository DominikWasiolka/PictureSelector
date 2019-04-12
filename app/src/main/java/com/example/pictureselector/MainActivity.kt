package com.example.pictureselector

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import androidx.recyclerview.widget.DividerItemDecoration



class MainActivity : AppCompatActivity() {

    private val PICK_PHOTO_FOR_AVATAR = 256
    private val ADD_NEW_URL_IMAGE = 255
    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("red"))
    private lateinit var deleteIcon: Drawable
    //private var image : Bitmap
    var imageCardList:LinkedList<ImageCard> = LinkedList <ImageCard>()
    var adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //activateButton.setOnClickListener(){
        //pickImage()
        //to działa równolegle - problem, bo to poniżej wykona się szybciej
        //}
        generateExampleImages()



        Log.d("CUSTOM", "list Size "+imageCardList.size.toString())
        Log.d("CUSTOM", "created adapter")
        //tutaj dodać do adaptera
        for (imageCard in imageCardList) {
            Log.d("CUSTOM", "Added element to imageCardList")
            adapter.add(ImageCardItem(imageCard))
        }

        recycleview_images.adapter = adapter
        recycleview_images.layoutManager = LinearLayoutManager(this)
        deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)!!

        val dividerItemDecoration = DividerItemDecoration( // lines between items in recycleview
            recycleview_images.getContext(),
            DividerItemDecoration.VERTICAL
        )
        recycleview_images.addItemDecoration(dividerItemDecoration)

        // for swap menu
        val  itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // delete item
                Log.d("CUSTOM", "adapter position "+viewHolder.adapterPosition.toString())
                imageCardList.removeAt(viewHolder.adapterPosition)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                adapter.removeGroup(imageCardList.size)//remove last free field
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
                    swipeBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
                    deleteIcon.setBounds(itemView.left + iconPosition, itemView.top + iconPosition,
                        itemView.left+iconPosition + deleteIcon.intrinsicWidth, itemView.bottom - iconPosition)
                } else{
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconPosition - deleteIcon.intrinsicWidth, itemView.top + iconPosition,
                        itemView.right - iconPosition , itemView.bottom - iconPosition)
                }
                swipeBackground.draw(c)
                c.save()
                
                c.clipRect(swipeBackground.bounds)

                deleteIcon.draw(c)
                c.restore()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
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
                //Toast.makeText(this, "item 1 is Selcted", Toast.LENGTH_SHORT).show()
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

    fun generateExampleImages(){
        val url1 = "https://www.w3schools.com/w3images/lights.jpg"
        val url2 = "https://www.w3schools.com/w3images/nature.jpg"
        val url3 = "https://www.w3schools.com/w3images/fjords.jpg"

        val result1 = ImageCard("title1",url1, listOf("tag1","tag2"), "12-13-2011")
        imageCardList.add(result1)

        val result2 = ImageCard("title2",url2,listOf("tag1","tag2"), "12-13-2011")
        imageCardList.add(result2)

        val result3 = ImageCard("title3",url3,listOf("tag1","tag2"), "12-13-2011")
        imageCardList.add(result3)
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

                    val result = ImageCard("title1","url1",listOf("tag1","tag2"), "12-13-2011")//,image)
                    imageCardList.add(result)
                    Log.d("CUSTOM", "list Size "+imageCardList.size.toString())
                    //contentIV.setImageBitmap(image)

                    //val img = FirebaseVisionImage.fromBitmap(image) // obraz, który może być przetwarzany przez image

                    //val labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler()

                }
                ADD_NEW_URL_IMAGE -> {
                    val returnedImageCard :ImageCard = data?.getSerializableExtra(getString(R.string.extra_ret_img_card)) as ImageCard
                    imageCardList.add(returnedImageCard)
                    adapter.add(ImageCardItem(returnedImageCard))
                    adapter?.notifyDataSetChanged()

                    Log.d("CUSTOM", "list Size "+imageCardList.size.toString())
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

