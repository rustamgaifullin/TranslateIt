package com.rm.translateit.ui.activities

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxAdapterView
import com.jakewharton.rxbinding.widget.RxTextView
import com.rm.translateit.R
import com.rm.translateit.api.models.TranslationResult
import com.rm.translateit.api.translation.Services
import com.rm.translateit.extension.hideKeyboard
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import com.rm.translateit.ui.adapters.ResultRecyclerViewAdapter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions

class MainActivity : BaseActivity() {
    companion object {
        private val TAG = "MainActivity"
        private val IME_ACTION_TRANSLATE = 21
    }

    private val originSpinner: Spinner by bindView(R.id.origin_spinner)
    private val destinationSpinner: Spinner by bindView(R.id.destination_spinner)
    private val wordEditText: EditText by bindView(R.id.word_editText)
    private val resultView: RecyclerView by bindView(R.id.result_recyclerView)
    private val changeLanguageButton: Button by bindView(R.id.changeLanguage_button)
    private val progressBar: ProgressBar by bindView(R.id.progressBar)
    private lateinit var originAdapter: LanguageSpinnerAdapter
    private lateinit var destinationAdapter: LanguageSpinnerAdapter

    private val languages = Services.languages()
    private var items: MutableList<TranslationResult> = arrayListOf()

    private var translatorSubscription: Subscription = Subscriptions.unsubscribed()

    lateinit var resultAdapter: ResultRecyclerViewAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun prepareUI() {
        originAdapter = LanguageSpinnerAdapter(this)
        originSpinner.adapter = originAdapter
        val fromLanguages = languages.mapIndexed { index, language -> Pair(index, language) }
        originAdapter.updateLanguages(fromLanguages)

        destinationAdapter = LanguageSpinnerAdapter(this)
        destinationSpinner.adapter = destinationAdapter
        setToSpinnerSelection(1, 0)

        resultAdapter = ResultRecyclerViewAdapter(items)
        resultView.adapter = resultAdapter
        resultView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        wordEditText.setImeActionLabel(getString(R.string.ime_action_translate), IME_ACTION_TRANSLATE)
    }

    override fun createBindings() {
        RxTextView.editorActions(wordEditText)
                .filter { action -> action == IME_ACTION_TRANSLATE }
                .subscribe({
                    action ->
                    translate()
                })

        RxAdapterView.itemSelections(originSpinner)
                .subscribe {
                    currentIndex ->
                    if (destinationSpinner.selectedItemPosition >= 0) {
                        val toSpinnerIndex = getToLanguageIndex(currentIndex)
                        setToSpinnerSelection(toSpinnerIndex, currentIndex)

                        translate()
                    }
                }

        RxAdapterView.itemSelections(destinationSpinner)
                .subscribe {
                    onNext ->
                    translate()
                }

        RxView.clicks(changeLanguageButton)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    swapLanguages()
                }
    }

    private fun getToLanguageIndex(currentIndex: Int): Int {
        var toSpinnerIndex = destinationAdapter.getItem(destinationSpinner.selectedItemPosition).first

        if (toSpinnerIndex == currentIndex) {
            toSpinnerIndex = if (currentIndex == 0) 1 else 0
        }
        return toSpinnerIndex
    }

    private fun swapLanguages() {
        val newFromIndex = destinationAdapter.getItem(destinationSpinner.selectedItemPosition).first
        val newToIndex = originAdapter.getItem(originSpinner.selectedItemPosition).first

        originSpinner.setSelection(newFromIndex)
        setToSpinnerSelection(newToIndex, newFromIndex)
    }

    private fun translate() {
        if (wordEditText.text.isNullOrEmpty()) return

        if (!translatorSubscription.isUnsubscribed) {
            translatorSubscription.unsubscribe()
        }

        val word = wordEditText.text.toString()
        val from = originAdapter.getItem(originSpinner.selectedItemId).second
        val to = destinationAdapter.getItem(destinationSpinner.selectedItemId).second

        translatorSubscription = Services.translate(word, from, to)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    clearAndNotify()
                    progressBar.visibility = View.VISIBLE
                }
                .doOnError { progressBar.visibility = View.GONE }
                .doOnCompleted {
                    progressBar.visibility = View.GONE
                    hideKeyboard { items.size > 0 }
                }
                .subscribe(
                        {
                            result ->
                            items.add(result)
                            resultAdapter.notifyDataSetChanged()
                        },
                        {
                            error ->
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    private fun clearAndNotify() {
        items.clear()
        resultAdapter.notifyDataSetChanged()
    }

    private fun setToSpinnerSelection(toLanguageIndex: Int, currentLanguageIndex: Int) {
        val toLanguages = languages
                .mapIndexed { index, language -> Pair(index, language) }
                .filterIndexed { index, language -> index != currentLanguageIndex }
        destinationAdapter.updateLanguages(toLanguages)

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

        destinationSpinner.setSelection(languageIndex)
    }
}
