package com.folioreader.ui.fragment

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
    private var translatedWord: String = ""
    private var wordExists: Boolean = false

    private lateinit var addToWordsView: View
    private lateinit var wordTextView: TextView
    private lateinit var translatedWordTextView: TextView
    private lateinit var addWordButtonTextView: TextView

    companion object {
        const val EXTRA_TRANSLATE = "EXTRA_TRANSLATE"
        const val EXTRA_CHECK = "EXTRA_CHECK"
        const val ACTION_TRANSLATE_AND_CHECK = "ACTION_TRANSLATE_AND_CHECK"
    }



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

        wordTextView.text = word;
        translatedWordTextView.text = "Translated"

        val wmlp = myDialog!!.window!!.attributes;
        wmlp.gravity = Gravity.TOP or Gravity.LEFT


        val xPos = arguments!!.getInt("x_pos")
        val yPos = arguments!!.getInt("y_pos")
//        Log.d(TAG, "-> onCreateDialog -> $xPos")
//        Log.d(TAG, "-> onCreateDialog -> $yPos")

        Log.d(TAG, "-> registerReceiver")
        LocalBroadcastManager.getInstance(addToWordsView.context).registerReceiver(
            translateAndCheckReceiver, IntentFilter(AddToMyWordsFragment.ACTION_TRANSLATE_AND_CHECK)
        )

        translateAndCheckWord(word);
        onClickAddWord(word)

        wmlp.x = xPos;
        wmlp.y = yPos;
        return myDialog
    }

    private val translateAndCheckReceiver = object : BroadcastReceiver () {
        override fun onReceive(context: Context, intent: Intent) {
            val extras = intent.extras;
            Log.d(TAG, "-> translateAndCheckReceiver -> onReceive")

            if (extras != null) {
                translatedWord = extras.getString(EXTRA_TRANSLATE, "")
                wordExists = extras.getBoolean(EXTRA_CHECK, false)
                Log.d(TAG, "-> translateAndCheckReceiver -> translatedWord -> $translatedWord")
                Log.d(TAG, "-> translateAndCheckReceiver -> wordExists -> $wordExists")

                translatedWordTextView.text = translatedWord
                addWordButtonTextView.text =  if (wordExists) "Add word" else "Word added"
            }
        }
    }

    private fun translateAndCheckWord (word: String) {
        Log.d(TAG, "-> translateAndCheckWord")
        val localBroadcastManager = LocalBroadcastManager.getInstance(addToWordsView.context)
        val intent = Intent(FolioReader.ACTION_TRANSLATE_AND_CHECK)
        intent.putExtra(FolioReader.EXTRA_TRANSLATE_AND_CHECK, word)
        localBroadcastManager.sendBroadcast(intent)
    }

    private fun onClickAddWord (word: String) {
        addToWordsView.addWordButton.setOnClickListener {
            Log.d(TAG, "-> addWordButton ->")


            val localBroadcastManager = LocalBroadcastManager.getInstance(it.context);
            val intent = Intent(FolioReader.ACTION_ADD_WORD)
            intent.putExtra(FolioReader.EXTRA_ADD_WORD, word)
            localBroadcastManager.sendBroadcast(intent)
        }
    }

    private fun bindViews(view: View) {
        wordTextView = addToWordsView.selectedWord
        translatedWordTextView = addToWordsView.translatedWord
        addWordButtonTextView = addToWordsView.addWordButton
        Log.d(TAG, "-> bindViews -> ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "-> onDestroy")
        LocalBroadcastManager.getInstance(addToWordsView.context)
            .unregisterReceiver(translateAndCheckReceiver)
    }
}