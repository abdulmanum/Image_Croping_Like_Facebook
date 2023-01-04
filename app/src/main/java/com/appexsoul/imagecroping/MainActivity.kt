package com.appexsoul.imagecroping

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.appexsoul.imagecroping.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.util.*

// Abdul Manum - Your Android Developer
// Copyright ©2023 All Right Reserved
class MainActivity : AppCompatActivity() {

    val context = this
    lateinit var binding: ActivityMainBinding

    var already = false
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelect.setOnClickListener {
            selectImage()
        }

        binding.btnZoom.setOnClickListener {

            binding.imageView2.visibility = View.VISIBLE
            val bitmap = getScreenshotByPosition(
                binding.view.x.toInt(),
                binding.view.y.toInt(),
                binding.view.width,
                binding.view.height
            )
            binding.imageView2.setImageBitmap(bitmap)

        }

    }


    fun getScreenshotByPosition(x: Int, y: Int, width: Int, height: Int): Bitmap {
        binding.imageView.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(binding.imageView.drawingCache, x, y, width, height)
        // Save the bitmap to a file
        val file = File(context.getExternalFilesDir(null), "${getDate()}")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.close()

        val handler = Handler()
        handler.postDelayed({
            binding.imageView.isDrawingCacheEnabled = false
            binding.imageView.invalidate()
        }, 3000)
        return bitmap
    }

    private fun zoomInOutImage(): Bitmap? {
        try {

            // get image
//            binding.imageView.isDrawingCacheEnabled = true
//            val bitmap = binding.imageView.drawingCache
//            // Save the bitmap to a file
//            val file = File(context.getExternalFilesDir(null), "${getDate()}")
//            val outputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//            outputStream.close()
//
//            val handler = Handler()
//            handler.postDelayed({
//                binding.imageView.isDrawingCacheEnabled = false
//                binding.imageView.invalidate()
//            }, 1000)


            // get image
            binding.view.isDrawingCacheEnabled = true
            val bitmap = binding.view.drawingCache
            // Save the bitmap to a file
            val file = File(context.getExternalFilesDir(null), "${getDate()}")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()

            val handler = Handler()
            handler.postDelayed({
                binding.view.isDrawingCacheEnabled = false
                binding.view.invalidate()
            }, 1000)


            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Catch: ${e.message}", Toast.LENGTH_SHORT).show()
            return null
        }

    }

    fun getDate(): Long {
        var cal = GregorianCalendar.getInstance()
        cal.time = Date()
        return cal.time.time
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK && data != null) {
            val imageUri = data.data
            binding.imageView.setImageURI(imageUri)
            binding.btnZoom.visibility = View.VISIBLE
        }
    }


}