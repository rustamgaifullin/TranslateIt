package com.rm.translateit.ui.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.rm.translateit.R
import com.rm.translateit.api.models.translation.TranslationResult
import com.rm.translateit.ui.decarators.SimpleTranslationResultDecorator
import com.rm.translateit.ui.decarators.TranslationResultDecorator
import com.rm.translateit.ui.util.fromHtml

class ResultRecyclerViewAdapter(
        private val items: MutableList<TranslationResult>,
        private val buttonCallback: (String) -> Unit) : Adapter<ResultRecyclerViewAdapter.ViewHolder>() {
    private val decorator: TranslationResultDecorator = SimpleTranslationResultDecorator()

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultRecyclerViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.result_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, translation) = items[position]

        val translationText = fromHtml(decorator.toSingleLine(translation.words))
        holder.translationTextView.text = translationText
        holder.descriptionTextView.text = translation.details.description
        holder.urlButton.setOnClickListener {
            buttonCallback.invoke(translation.details.url)
        }

        holder.descriptionTextView.visibility = getVisibilityBasedOnText(translation.details.description)
        holder.urlButton.visibility = getVisibilityBasedOnText(translation.details.url)
    }

    private fun getVisibilityBasedOnText(text: CharSequence): Int {
        if (text.isNotEmpty()) return View.VISIBLE

        return View.GONE
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var translationTextView: TextView = itemView.findViewById(R.id.translationTextView)
        var descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

        var urlButton: Button = itemView.findViewById(R.id.urlButton)
    }
}