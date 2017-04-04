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
import com.rm.translateit.ui.decarators.SimpleTranslationResultDecorator
import com.rm.translateit.ui.decarators.TranslationResultDecorator
import com.rm.translateit.ui.fromHtml

class ResultRecyclerViewAdapter(val items: MutableList<TranslationResult>) : Adapter<ResultRecyclerViewAdapter.ViewHolder>() {

    private val decorator: TranslationResultDecorator = SimpleTranslationResultDecorator()

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResultRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.result_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val (source, translation) = items[position]

        val translationText = fromHtml(multilineText(translation))
        holder?.translationTextView?.text = translationText
        holder?.sourceTextView?.text = source.name
    }

    private fun multilineText(translation: List<TranslationItem>):CharSequence = translation
            .map { item -> decorator.toSingleLine(item) }
            .reduce { first, second -> "$first\n$second" }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var sourceTextView: TextView = itemView?.findViewById(R.id.source_textView) as TextView
        var translationTextView: TextView = itemView?.findViewById(R.id.translation_textView) as TextView
    }
}