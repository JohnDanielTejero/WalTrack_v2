package com.jdt.waltrackv2.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import com.jdt.waltrackv2.R
import com.jdt.waltrackv2.databinding.ActivityMainBinding
import com.jdt.waltrackv2.databinding.MenuDialogBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var buttonTest: Button
    private lateinit var menuDialog : MenuDialogBinding
    private lateinit var dialog: Dialog

    private var dialogLaunched : Boolean = false
    private var x1 = 0f
    private var x2 = 0f
    private val MIN_DISTANCE = 150
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        menuDialog = MenuDialogBinding.inflate(layoutInflater)
        buttonTest = binding.tite

        binding.root.setOnTouchListener { _, event ->
            handleSwipeEvent(event)
            true
        }
        buttonTest.setOnClickListener {
            dialogLaunched = true
            launchDialog()
        }

        menuDialog.root.setOnTouchListener { _, event ->
            handleSwipeEvent(event)
            true
        }

    }
    private fun handleSwipeEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> x1 = event.x
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val deltaX = x2 - x1
                if (deltaX > MIN_DISTANCE && !dialogLaunched) {
                    launchDialog()
                    dialogLaunched = true
                } else if (-deltaX > MIN_DISTANCE && dialogLaunched) {
                    dialogLaunched = false
                    dialog.dismiss()
                }
            }
        }
    }
    fun launchDialog(){
        (menuDialog.root.parent).let {
            (it as? ViewGroup)?.removeView(menuDialog.root)
        }

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(menuDialog.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)
    }
}