package com.rm.translateit.ui.adapters

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.rm.translateit.R
import com.rm.translateit.api.models.translation.TranslationItem
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
        val (source, translation) = items[position]

        val translationText = fromHtml(multilineText(translation.translationItems))
        holder.translationTextView.text = translationText
        holder.sourceTextView.text = source.name
        holder.descriptionTextView.text = translation.details.description
        holder.urlButton.text = translation.details.url
        holder.urlButton.setOnClickListener {
            buttonCallback.invoke(translation.details.url)
        }

        holder.applyVisibilityIfNeeded()
    }

    private fun multilineText(translation: List<TranslationItem>): CharSequence {
        return translation
                .map { item -> decorator.toSingleLine(item) }
                .fold("") { first, second -> "$first\n$second" }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
        var translationTextView: TextView = itemView.findViewById(R.id.translationTextView)
        var descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        var urlButton: Button = itemView.findViewById(R.id.urlButton)

        fun applyVisibilityIfNeeded() {
            descriptionTextView.visibility = getVisibilityBasedOnText(descriptionTextView.text)
            urlButton.visibility = getVisibilityBasedOnText(urlButton.text)
        }

        private fun getVisibilityBasedOnText(text: CharSequence): Int {
            if (text.isNotEmpty()) return View.VISIBLE

            return View.GONE
        }
    }
}