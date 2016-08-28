package com.rm.translateit.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import com.rm.translateit.R
import com.rm.translateit.api.Translater
import com.rm.translateit.api.TranslaterContext
import com.rm.translateit.api.models.Language
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val fromSpinner: Spinner by bindView(R.id.from_spinner)
    val toSpinner: Spinner by bindView(R.id.to_spinner)
    val translateButton: Button by bindView(R.id.translate_button)
    val resultTextView: TextView by bindView(R.id.result_textView)
    val wordEditText: EditText by bindView(R.id.word_editText)

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

        RxView.clicks(translateButton)
                .subscribe( { search() } )

        RxTextView.textChanges(wordEditText)
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .subscribe({ search() })
    }

    private fun search() {
        val word = wordEditText.text.toString()
        val from = (fromSpinner.selectedItem as Language).code
        val to = (toSpinner.selectedItem as Language).code
        context.translate(word, from, to).subscribe({
            resultQuery ->
            resultTextView.text = resultQuery
        })
    }
}
