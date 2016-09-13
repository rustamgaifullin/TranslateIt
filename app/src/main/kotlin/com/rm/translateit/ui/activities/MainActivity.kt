package com.rm.translateit.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.*
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.Spinner
import butterknife.bindView
import com.jakewharton.rxbinding.widget.RxTextView
import com.rm.translateit.R
import com.rm.translateit.api.Translater
import com.rm.translateit.api.TranslaterContext
import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.TranslationResult
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import com.rm.translateit.ui.adapters.ResultRecyclerViewAdapter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val fromSpinner: Spinner by bindView(R.id.from_spinner)
    val toSpinner: Spinner by bindView(R.id.to_spinner)
    val wordEditText: EditText by bindView(R.id.word_editText)
    val resultView: RecyclerView by bindView(R.id.result_recyclerView)

    var context: Translater = TranslaterContext.getContext()
    var items: MutableList<TranslationResult> = arrayListOf()
    lateinit var resultAdapter: ResultRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fromAdapter = LanguageSpinnerAdapter(this)
        fromSpinner.adapter = fromAdapter

        val toAdapter = LanguageSpinnerAdapter(this)
        toSpinner.adapter = toAdapter

        fromAdapter.updateLanguages(context.languages())
        toAdapter.updateLanguages(context.languages())

        resultAdapter = ResultRecyclerViewAdapter(items)
        resultView.adapter = resultAdapter
        resultView.layoutManager = LinearLayoutManager(this, VERTICAL, false)

        RxTextView.textChanges(wordEditText)
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .filter({ s -> s.length > 0 })
                .subscribe({ search() })
    }

    private fun search() {
        val word = wordEditText.text.toString()
        val from = (fromSpinner.selectedItem as Language).code
        val to = (toSpinner.selectedItem as Language).code
        context.translate(word, from, to).subscribe({ resultQuery ->
            items.clear()
            items.add(TranslationResult("wiki", resultQuery))
            resultAdapter.notifyDataSetChanged()
        })
    }
}
