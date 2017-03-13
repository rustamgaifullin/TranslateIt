package com.rm.translateit.ui.activities

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo.*
import android.widget.*
import butterknife.bindView
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxAdapterView
import com.jakewharton.rxbinding.widget.RxTextView
import com.rm.translateit.R
import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.models.translation.TranslationResult
import com.rm.translateit.api.translation.Sources
import com.rm.translateit.extension.hideKeyboard
import com.rm.translateit.extension.translationComponent
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import com.rm.translateit.ui.adapters.ResultRecyclerViewAdapter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

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

    @Inject
    lateinit var languages: Languages

    @Inject
    lateinit var allSources: Sources

    private var items: MutableList<TranslationResult> = arrayListOf()

    private var translatorSubscription: Subscription = Subscriptions.unsubscribed()
    private var originSpinnerSubscription: Subscription = Subscriptions.unsubscribed()

    lateinit var resultAdapter: ResultRecyclerViewAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun prepareDI() {
        translationComponent().inject(this)
    }

    override fun prepareUI() {
        originAdapter = LanguageSpinnerAdapter(this)
        originSpinner.adapter = originAdapter
        val originLanguages = languages.originLanguages()
        originAdapter.updateLanguages(originLanguages)

        val destinationLanguages = languages.destinationLanguages(originLanguages.first().code)
        destinationAdapter = LanguageSpinnerAdapter(this)
        destinationAdapter.updateLanguages(destinationLanguages)
        destinationSpinner.adapter = destinationAdapter

        resultAdapter = ResultRecyclerViewAdapter(items)
        resultView.adapter = resultAdapter
        resultView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        wordEditText.setImeActionLabel(getString(R.string.ime_action_translate), IME_ACTION_TRANSLATE)
    }

    override fun createBindings() {
        subscriptions.add(
                RxTextView.editorActions(wordEditText)
                        .filter { action ->
                            action == IME_ACTION_TRANSLATE ||
                                    action == IME_ACTION_DONE ||
                                    action == IME_ACTION_GO ||
                                    action == IME_ACTION_SEND ||
                                    action == IME_ACTION_SEARCH
                        }
                        .subscribe({
                            translate()
                        }))

        subscriptions.add(
                RxAdapterView.itemSelections(destinationSpinner)
                        .subscribe {
                            val language = destinationAdapter.getItem(destinationSpinner.selectedItemPosition)
                            languages.updateDestinationLastUsage(language)
                            translate()
                        })

        subscriptions.add(
                RxView.clicks(changeLanguageButton)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            swapLanguages()
                        })

        subscriptionForOriginSpinner()
    }

    private fun subscriptionForOriginSpinner() {
        originSpinnerSubscription = RxAdapterView.itemSelections(originSpinner)
                .subscribe {
                    currentOriginIndex ->
                    if (originSpinner.selectedItemPosition >= 0) {
                        val language = originAdapter.getItem(originSpinner.selectedItemPosition)
                        languages.updateOriginLastUsage(language)

                        destinationAdapter.updateLanguages(languages.destinationLanguages(language.code))

                        translate()
                    }
                }
        subscriptions.add(originSpinnerSubscription)
    }

    private fun swapLanguages() {
        originSpinnerSubscription.unsubscribe()
        subscriptions.remove(originSpinnerSubscription)

        val originLanguage = originAdapter.getItem(originSpinner.selectedItemPosition)
        val destinationLanguage = destinationAdapter.getItem(destinationSpinner.selectedItemPosition)

        val indexForOrigin = originAdapter.getItemIndex(destinationLanguage)
        originSpinner.setSelection(indexForOrigin)

        val destinationLanguages = languages.destinationLanguages(destinationLanguage.code)
        destinationAdapter.updateLanguages(destinationLanguages)
        val indexForDestionation = destinationAdapter.getItemIndex(originLanguage);
        destinationSpinner.setSelection(indexForDestionation)

        subscriptionForOriginSpinner()
    }

    private fun translate() {
        if (wordEditText.text.isNullOrEmpty()) return

        if (!translatorSubscription.isUnsubscribed) {
            translatorSubscription.unsubscribe()
        }

        val word = wordEditText.text.toString()
        val fromLanguage = originAdapter.getItem(originSpinner.selectedItemPosition)
        val toLanguage = destinationAdapter.getItem(destinationSpinner.selectedItemPosition)

        translatorSubscription = allSources.translate(word, fromLanguage, toLanguage)
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
}