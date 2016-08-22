package com.rm.translateit.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import com.rm.translateit.R
import com.rm.translateit.api.Translater
import com.rm.translateit.api.TranslaterContext
import com.rm.translateit.api.models.Language
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter

class MainActivity : AppCompatActivity() {

    var fromSpinner: Spinner? = null
    var toSpinner: Spinner? = null
    var translateButton: Button? = null
    var resultTextView: TextView? = null
    var wordEditText: EditText? = null

    var context: Translater = TranslaterContext.getContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fromSpinner = findViewById(R.id.from_spinner) as Spinner?
        toSpinner = findViewById(R.id.to_spinner) as Spinner?

        val fromAdapter = LanguageSpinnerAdapter(this)
        fromSpinner?.adapter = fromAdapter

        val toAdapter = LanguageSpinnerAdapter(this)
        toSpinner?.adapter = toAdapter

        fromAdapter.updateLanguages(context.languages())
        toAdapter.updateLanguages(context.languages())

        wordEditText = findViewById(R.id.word_editText) as EditText?
        resultTextView = findViewById(R.id.result_textView) as TextView?

        translateButton = findViewById(R.id.translate_button) as Button?
        translateButton?.setOnClickListener {
            val word = wordEditText?.text.toString()
            val from = (fromSpinner?.selectedItem as Language).code
            val to = (toSpinner?.selectedItem as Language).code
            resultTextView?.text = context.translate(word, from, to)
        }
    }
}
