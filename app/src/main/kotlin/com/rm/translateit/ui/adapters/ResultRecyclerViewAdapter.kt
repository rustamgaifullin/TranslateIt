package com.rm.translateit.ui.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rm.translateit.R
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.TranslationResult

class ResultRecyclerViewAdapter(val items: MutableList<TranslationResult>) : Adapter<ResultRecyclerViewAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResultRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.result_view, parent, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val (source, translation) = items[position]

        holder?.sourceTextView?.text = source.name
        holder?.translationTextView?.text = multilineText(translation)
    }

    private fun multilineText(translation: List<TranslationItem>) = translation
            .map(TranslationItem::word)
            .reduce { first, second -> "$first\n$second" }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var sourceTextView: TextView = itemView?.findViewById(R.id.source_textView) as TextView
        var translationTextView: TextView = itemView?.findViewById(R.id.translation_textView) as TextView
    }
}