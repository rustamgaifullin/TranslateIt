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
import kotterknife

class MainActivity : AppCompatActivity() {

    val fromSpinner: Spinner by bindView(R.id.from_spinner)
    var toSpinner: Spinner by bindView(R.id.to_spinner)
    var translateButton: Button by bindView(R.id.translate_button)
    var resultTextView: TextView by bindView(R.id.result_textView)
    var wordEditText: EditText by bindView(R.id.word_editText)

    var context: Translater = TranslaterContext.getContext()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fromAdapter = LanguageSpinnerAdapter(this)
        fromSpinner.adapter = fromAdapter

        val toAdapter = LanguageSpinnerAdapter(this)
        toSpinner.adapter = toAdapter

        fromAdapter.updateLanguages(context.languages())
        toAdapter.updateLanguages(context.languages())

        translateButton.setOnClickListener {
            val word = wordEditText.text.toString()
            val from = (fromSpinner.selectedItem as Language).code
            val to = (toSpinner.selectedItem as Language).code
            resultTextView.text = context.translate(word, from, to)
        }
    }
}
