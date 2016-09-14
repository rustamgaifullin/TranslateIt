package com.rm.translateit.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.Spinner
import butterknife.bindView
import com.jakewharton.rxbinding.widget.RxTextView
import com.rm.translateit.R
import com.rm.translateit.api.translation.Context
import com.rm.translateit.api.translation.models.Language
import com.rm.translateit.api.translation.models.TranslationResult
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import com.rm.translateit.ui.adapters.ResultRecyclerViewAdapter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val fromSpinner: Spinner by bindView(R.id.from_spinner)
    val toSpinner: Spinner by bindView(R.id.to_spinner)
    val wordEditText: EditText by bindView(R.id.word_editText)
    val resultView: RecyclerView by bindView(R.id.result_recyclerView)

    var items: MutableList<TranslationResult> = arrayListOf()
    lateinit var resultAdapter: ResultRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fromAdapter = LanguageSpinnerAdapter(this)
        fromSpinner.adapter = fromAdapter

        val toAdapter = LanguageSpinnerAdapter(this)
        toSpinner.adapter = toAdapter

        fromAdapter.updateLanguages(Context.languages())
        toAdapter.updateLanguages(Context.languages())

        resultAdapter = ResultRecyclerViewAdapter(items)
        resultView.adapter = resultAdapter
        resultView.layoutManager = LinearLayoutManager(this, VERTICAL, false)

        RxTextView.textChanges(wordEditText)
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .filter({ s -> s.length > 0 })
                .subscribe({
                    items.clear()
                    search()
                })
    }

    private fun search() {
        val word = wordEditText.text.toString()
        val from = (fromSpinner.selectedItem as Language).code
        val to = (toSpinner.selectedItem as Language).code

        Context.translate(word, from, to).subscribe { result ->
            items.add(result)
            resultAdapter.notifyDataSetChanged()
        }
    }
}
