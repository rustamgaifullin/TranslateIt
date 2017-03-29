package com.rm.translateit.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rm.translateit.api.models.LanguageModel

class LanguageSpinnerAdapter(private val context: Context) : BaseAdapter() {

    private var languages = emptyList<LanguageModel>()

    fun updateLanguages(languages: List<LanguageModel>) {
        this.languages = languages
        notifyDataSetChanged()
    }

    fun getItemIndex(languageToSearch: LanguageModel): Int {
        return languages.indexOfFirst { it.code == languageToSearch.code}
    }

    override fun getCount(): Int {
        return languages.count()
    }

    override fun getItem(position: Int): LanguageModel {
        return languages[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val textView: TextView?

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)

        } else {
            view = convertView
        }

        val language = languages[position]
        textView = view as TextView?
        textView?.text = language.name

        return view
    }
}