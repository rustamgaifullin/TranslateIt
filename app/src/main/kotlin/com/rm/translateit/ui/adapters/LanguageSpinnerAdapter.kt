package com.rm.translateit.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rm.translateit.api.models.Language
import com.rm.translateit.ui.extension.currentLocale

class LanguageSpinnerAdapter(private val context: Context) : BaseAdapter() {

  private var languages = emptyList<Language>()

  fun updateLanguages(languages: List<Language>) {
    this.languages = languages
    notifyDataSetChanged()
  }

  fun getItemIndex(languageToSearch: Language): Int {
    return languages.indexOfFirst { it.code == languageToSearch.code }
  }

  override fun getCount(): Int {
    return languages.count()
  }

  override fun getItem(position: Int): Language {
    return languages[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getView(
    position: Int,
    convertView: View?,
    parent: ViewGroup?
  ): View? {
    val view: View?
    val textView: TextView?

    if (convertView == null) {
      view = LayoutInflater.from(context)
          .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

    } else {
      view = convertView
    }

    val language = languages[position]
    textView = view as TextView?
    textView?.text = language.findName(context.currentLocale().language)
        .capitalize()

    return view
  }
}