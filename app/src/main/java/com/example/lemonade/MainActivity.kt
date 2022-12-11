package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"
    // SELECT represents the "pick lemon" state
    private val SELECT = "select"
    // SQUEEZE represents the "squeeze lemon" state
    private val SQUEEZE = "squeeze"
    // DRINK represents the "drink lemonade" state
    private val DRINK = "drink"
    // RESTART represents the state where the lemonade has been drunk and the glass is empty
    private val RESTART = "restart"
    // Default the state to select
    private var lemonadeState = "select"
    // Default lemonSize to -1
    private var lemonSize = -1
    // Default the squeezeCount to -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }
        lemonImage!!.setOnLongClickListener {
            showSnackbar()
        }
    }

    /**
     * === DO NOT ALTER THIS METHOD ===
     *
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        if(lemonadeState==SELECT){
            lemonadeState=SQUEEZE
            val tree:LemonTree=lemonTree
            lemonSize=tree.pick()
            squeezeCount=0
        }
        else if (lemonadeState==SQUEEZE){
            squeezeCount+=1
            lemonSize-=1
            if (lemonSize==0){
                lemonadeState=DRINK
            }
        }
        else if(lemonadeState==DRINK){
            lemonadeState=RESTART
            lemonSize-=1

        }
        else{
            lemonadeState=SELECT
        }
        setViewElements()
    }
    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        if(lemonadeState==SELECT){
            textAction.text=resources.getString(R.string.lemon_select)
            lemonImage?.setImageResource(R.drawable.lemon_tree)
        }
        else if (lemonadeState==SQUEEZE){
            textAction.text=resources.getString(R.string.lemon_squeeze)+" "+lemonSize
            lemonImage?.setImageResource(R.drawable.lemon_squeeze)
        }
        else if(lemonadeState==DRINK){
            textAction.text=resources.getString(R.string.lemon_drink)
            lemonImage?.setImageResource(R.drawable.lemon_drink)
        }
        else{
            textAction.text=resources.getString(R.string.lemon_empty_glass)
            lemonImage?.setImageResource(R.drawable.lemon_restart)
        }
    }
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}
class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
