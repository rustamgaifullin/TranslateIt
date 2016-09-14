package com.rm.translateit.ui.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.rm.translateit.R
import com.rm.translateit.api.translation.models.TranslationResult

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

        holder?.sourceTextView?.text = source
        holder?.translationTextView?.text = translation
    }

    class ViewHolder: RecyclerView.ViewHolder {
        lateinit var sourceTextView: TextView
        lateinit var translationTextView: TextView

        constructor(itemView: View?): super(itemView) {
            sourceTextView = itemView?.findViewById(R.id.source_textView) as TextView
            translationTextView = itemView?.findViewById(R.id.translation_textView) as TextView
        }
    }
}
