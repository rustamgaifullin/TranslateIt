package com.rm.translateit.ui.activities

import android.net.Uri
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo.*
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Toast
import com.rm.translateit.R
import com.rm.translateit.api.languages.Languages
import com.rm.translateit.api.models.translation.TranslationResult
import com.rm.translateit.api.toLanguage
import com.rm.translateit.api.translation.Sources
import com.rm.translateit.ui.adapters.LanguageSpinnerAdapter
import com.rm.translateit.ui.adapters.ResultRecyclerViewAdapter
import com.rm.translateit.ui.extension.hideKeyboard
import com.rm.translateit.ui.extension.translationComponent
import kotlinx.android.synthetic.main.activity_main.*
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat


class MainActivity : BaseActivity() {
    companion object {
        private val TAG = "MainActivity"
        private val IME_ACTION_TRANSLATE = 21
    }

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
        origin_spinner.adapter = originAdapter
        val originLanguages = languages.originLanguages()
        originAdapter.updateLanguages(originLanguages)

        val destinationLanguages = languages.destinationLanguages(originLanguages.first().code)
        destinationAdapter = LanguageSpinnerAdapter(this)
        destinationAdapter.updateLanguages(destinationLanguages)
        destination_spinner.adapter = destinationAdapter

        resultAdapter = ResultRecyclerViewAdapter(items) {
            val customTabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setToolbarColor(ContextCompat.getColor(MainActivity@this, R.color.primary))
                    .build()
            customTabsIntent.launchUrl(MainActivity@this, Uri.parse(it))
        }

        resultRecyclerView.adapter = resultAdapter
        resultRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        word_editText.setImeActionLabel(getString(R.string.ime_action_translate), IME_ACTION_TRANSLATE)
    }

    override fun createBindings() {
        word_editText.setOnEditorActionListener { _, action, _ ->
            if (action == IME_ACTION_TRANSLATE ||
                    action == IME_ACTION_DONE ||
                    action == IME_ACTION_GO ||
                    action == IME_ACTION_SEND ||
                    action == IME_ACTION_SEARCH) {
                translate()

                return@setOnEditorActionListener true
            }

            return@setOnEditorActionListener false
        }

        destination_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val language = destinationAdapter.getItem(destination_spinner.selectedItemPosition)
                languages.updateDestinationLastUsage(language)
                translate()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }

        }

        changeLanguage_button.setOnClickListener {
            swapLanguages()
        }

        subscriptionForOriginSpinner()
    }

    private fun subscriptionForOriginSpinner() {
        origin_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                val language = originAdapter.getItem(origin_spinner.selectedItemPosition)
                languages.updateOriginLastUsage(language)

                destinationAdapter.updateLanguages(languages.destinationLanguages(language.code))

                translate()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {

            }
        }
    }

    private fun swapLanguages() {
        originSpinnerSubscription.unsubscribe()
        subscriptions.remove(originSpinnerSubscription)

        val originLanguage = originAdapter.getItem(origin_spinner.selectedItemPosition)
        val destinationLanguage = destinationAdapter.getItem(destination_spinner.selectedItemPosition)

        val indexForOrigin = originAdapter.getItemIndex(destinationLanguage)
        origin_spinner.setSelection(indexForOrigin)

        val destinationLanguages = languages.destinationLanguages(destinationLanguage.code)
        destinationAdapter.updateLanguages(destinationLanguages)
        val indexForDestination = destinationAdapter.getItemIndex(originLanguage)
        destination_spinner.setSelection(indexForDestination)

        subscriptionForOriginSpinner()
    }

    private fun translate() {
        if (word_editText.text.isNullOrEmpty()) return

        if (!translatorSubscription.isUnsubscribed) {
            translatorSubscription.unsubscribe()
        }

        val word = word_editText.text.toString()
        val fromLanguage = originAdapter.getItem(origin_spinner.selectedItemPosition).toLanguage()
        val toLanguage = destinationAdapter.getItem(destination_spinner.selectedItemPosition).toLanguage()

        translatorSubscription = allSources.translate(word, fromLanguage, toLanguage)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    clearAndNotify()
                    progressBar.visibility = View.VISIBLE
                    showNotFoundView(false)
                }
                .doOnError {
                    progressBar.visibility = View.GONE
                    showNotFoundView(items.size == 0)
                }
                .doOnCompleted {
                    progressBar.visibility = View.GONE
                    hideKeyboard { items.size > 0 }
                    showNotFoundView(items.size == 0)
                }
                .subscribe(
                        { result ->
                            items.add(result)
                            resultAdapter.notifyDataSetChanged()
                        },
                        { error ->
                            Log.e(TAG, error.message, error)
                            Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                        }
                )
    }

    private fun showNotFoundView(showView: Boolean) {
        if (showView) {
            notFoundView.visibility = View.VISIBLE
            resultRecyclerView.visibility = View.GONE
        } else {
            notFoundView.visibility = View.GONE
            resultRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun clearAndNotify() {
        items.clear()
        resultAdapter.notifyDataSetChanged()
    }
}