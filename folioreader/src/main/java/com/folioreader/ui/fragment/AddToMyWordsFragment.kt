package com.folioreader.ui.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.folioreader.Constants
import com.folioreader.FolioReader
import com.folioreader.R
import kotlinx.android.synthetic.main.layout_add_to_my_words.view.*


class AddToMyWordsFragment : DialogFragment() {
    private val TAG = "AddToMyWordsFragment"

    private lateinit var myDialog: Dialog

    private var word: String = ""

    private  lateinit var addToWordsView: View
    private lateinit var textViewWord: TextView
    private lateinit var textViewTranslate: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "-> onCreate -> ")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d(TAG, "-> onCreateDialog -> ")
        myDialog = Dialog(context!!)
        addToWordsView = View.inflate(context, R.layout.layout_add_to_my_words, null)
        myDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        bindViews(addToWordsView)
        word = arguments!!.getString(Constants.SELECTED_WORD, "")

        myDialog.setContentView(addToWordsView)

        textViewWord.text = word;
        textViewTranslate.text = "Translated"

        val wmlp = myDialog!!.window!!.attributes;
        wmlp.gravity = Gravity.TOP or Gravity.LEFT


        val xPos = arguments!!.getInt("x_pos")
        val yPos = arguments!!.getInt("y_pos")
//        Log.d(TAG, "-> onCreateDialog -> $xPos")
//        Log.d(TAG, "-> onCreateDialog -> $yPos")

        onClickAddWord()

        wmlp.x = xPos;
        wmlp.y = yPos;
        return myDialog
    }

    private fun onClickAddWord () {
        addToWordsView.addWordButton.setOnClickListener {
            Log.d(TAG, "-> addWordButton ->")


            val localBroadcastManager = LocalBroadcastManager.getInstance(it.context);
            val intent = Intent(FolioReader.ACTION_ADD_WORD)
            localBroadcastManager.sendBroadcast(intent)
        }


    }

    private fun bindViews(view: View) {
        textViewWord = addToWordsView.selectedWord
        textViewTranslate = addToWordsView.translatedWord
        Log.d(TAG, "-> bindViews -> ")

    }
}