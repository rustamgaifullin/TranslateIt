package com.rm.translateit.api.languages

import com.rm.translateit.api.models.LanguageModel

class StaticLanguages: Languages {
    override fun all(): List<LanguageModel> {
        return listOf(
                LanguageModel("EN", "English"),
                LanguageModel("PL", "Polish"),
                LanguageModel("RU", "Russian")
        )
    }

    override fun originLanguages(): List<LanguageModel> {
        return all()
    }

    override fun destinationLanguages(exceptOriginCode: String): List<LanguageModel> {
        return all().filter { language -> language.code != exceptOriginCode.toUpperCase() }
    }

    override fun updateOriginLastUsage(model: LanguageModel) {

    }

    override fun updateDestinationLastUsage(model: LanguageModel) {

    }
}