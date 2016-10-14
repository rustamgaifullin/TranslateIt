package com.rm.translateit.ui.activities

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxAdapterView
import com.jakewharton.rxbinding.widget.RxTextView
import com.rm.translateit.R
import com.rm.translateit.api.translation.Context
import com.rm.translateit.api.translation.models.TranslationResult
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import com.rm.translateit.ui.adapters.ResultRecyclerViewAdapter
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    companion object {
        private val TAG = "MainActivity"
    }

    private val fromSpinner: Spinner by bindView(R.id.from_spinner)
    private val toSpinner: Spinner by bindView(R.id.to_spinner)
    private val wordEditText: EditText by bindView(R.id.word_editText)
    private val resultView: RecyclerView by bindView(R.id.result_recyclerView)
    private val changeLanguageButton: Button by bindView(R.id.changeLanguage_button)
    private val progressBar: ProgressBar by bindView(R.id.progressBar)
    private lateinit var fromAdapter: LanguageSpinnerAdapter
    private lateinit var toAdapter: LanguageSpinnerAdapter

    private val languages = Context.languages()

    var items: MutableList<TranslationResult> = arrayListOf()
    lateinit var resultAdapter: ResultRecyclerViewAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun prepareUI() {
        fromAdapter = LanguageSpinnerAdapter(this)
        fromSpinner.adapter = fromAdapter
        val fromLanguages = languages.mapIndexed { index, language -> Pair(index, language) }
        fromAdapter.updateLanguages(fromLanguages)

        toAdapter = LanguageSpinnerAdapter(this)
        toSpinner.adapter = toAdapter
        setToSpinnerSelection(1, 0)

        resultAdapter = ResultRecyclerViewAdapter(items)
        resultView.adapter = resultAdapter
        resultView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun createBindings() {
        RxTextView.textChanges(wordEditText)
                .throttleWithTimeout(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .filter({ s -> s.length > 0 })
                .subscribe({
                    clearItemsAndSearch()
                })

        RxAdapterView.itemSelections(fromSpinner)
                .subscribe {
                    currentIndex ->
                    if (toSpinner.selectedItemPosition >= 0) {
                        var toSpinnerIndex = toAdapter.getItem(toSpinner.selectedItemPosition).first

                        if (toSpinnerIndex == currentIndex) {
                            toSpinnerIndex = if (currentIndex == 0) 1 else 0
                        }

                        setToSpinnerSelection(toSpinnerIndex, currentIndex)
                    }
        }

        RxView.clicks(changeLanguageButton).subscribe {
            swapLanguages()
            clearItemsAndSearch()
        }
    }

    private fun swapLanguages() {
        val newFromIndex = toAdapter.getItem(toSpinner.selectedItemPosition).first
        val newToIndex = fromAdapter.getItem(fromSpinner.selectedItemPosition).first

        fromSpinner.setSelection(newFromIndex)
        setToSpinnerSelection(newToIndex, newFromIndex)
    }

    private fun clearItemsAndSearch() {
        items.clear()
        search()
    }

    private fun search() {
        val word = wordEditText.text.toString()
        val from = fromAdapter.getItem(fromSpinner.selectedItemId).second.code
        val to = toAdapter.getItem(toSpinner.selectedItemId).second.code

        Context.translate(word, from, to)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBar.visibility = View.VISIBLE }
                .doOnError { progressBar.visibility = View.GONE }
                .doOnCompleted { progressBar.visibility = View.GONE }
                .subscribe(
                        {
                            result ->
                            items.add(result)
                            resultAdapter.notifyDataSetChanged()
                        },
                        {
                            error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        })

    }

    private fun setToSpinnerSelection(toLanguageIndex: Int, currentLanguageIndex: Int) {
        Log.d(TAG, "set indexes, from - $currentLanguageIndex to - $toLanguageIndex")

        val toLanguages = languages
                .mapIndexed { index, language -> Pair(index, language) }
                .filterIndexed { index, language -> index != currentLanguageIndex }
        toAdapter.updateLanguages(toLanguages)

        val languageIndex = toLanguages
                .mapIndexed { realIndex, languagePair ->
                    val mappedIndex = languagePair.first
                    Pair(mappedIndex, realIndex)
                }
                .filter { indexPair ->
                    val mappedIndex = indexPair.first
                    mappedIndex == toLanguageIndex
                }
                .map(Pair<Int, Int>::second)
                .last()

        toSpinner.setSelection(languageIndex)
    }
}
