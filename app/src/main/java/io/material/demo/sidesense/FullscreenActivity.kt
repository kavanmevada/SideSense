package io.material.demo.sidesense

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_fullscreen.*
import android.view.ViewGroup
import android.os.Build
import android.provider.Settings


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_fullscreen)

        val showDebugOverlay =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)
        if (showDebugOverlay) {
            //DebugOverlay.with(this@MainActivity).log("Message test " + MainActivity.i++)
            startAnimation()
        } else {
            requestOverlayPermission()
        }




    }


    fun startAnimation(){


        background.setOnTouchListener { v, event ->
            val pressure = event.pressure


            val param = background.layoutParams as ViewGroup.LayoutParams
            param.height = resources.displayMetrics.heightPixels
            param.width = resources.displayMetrics.widthPixels
            background.layoutParams = param

            val imgAnimation = imageView.drawable as android.graphics.drawable.AnimatedVectorDrawable

            if (imgAnimation.isRunning) {
                imgAnimation.stop()
            }

            imageView.visibility = View.INVISIBLE
            Handler().postDelayed({
                imageView.visibility = View.VISIBLE
                imgAnimation.start()
            }, 400)
            Handler().postDelayed({
                imageView.visibility = View.INVISIBLE
            }, 1400)




            true
        }


    }


    private fun requestOverlayPermission() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return
        }

        val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        myIntent.setData(Uri.parse("package:$packageName"))
        startActivityForResult(myIntent, APP_PERMISSIONS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == APP_PERMISSIONS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    //DebugOverlay.with(this@MainActivity).log("Message test " + i++)
                    startAnimation()
                }
            } else {
                //DebugOverlay.with(this@MainActivity).log("Message test " + i++)
                startAnimation()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {

        var i = 0
        private val APP_PERMISSIONS = 1234
    }
}


