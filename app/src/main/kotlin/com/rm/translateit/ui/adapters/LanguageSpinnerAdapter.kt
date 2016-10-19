package com.rm.translateit.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rm.translateit.api.models.Language

class LanguageSpinnerAdapter(private val context: Context) : BaseAdapter() {

    private var languages = emptyList<Pair<Int, Language>>()

    fun updateLanguages(languages: List<Pair<Int, Language>>) {
        this.languages = languages
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return languages.count()
    }

    override fun getItem(position: Int): Pair<Int, Language> {
        return getItem(position.toLong())
    }

    fun getItem(position: Long): Pair<Int, Language> {
        return languages[position.toInt()]
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

        val language = languages[position].second
        textView = view as TextView?
        textView?.text = language.name

        return view
    }
}