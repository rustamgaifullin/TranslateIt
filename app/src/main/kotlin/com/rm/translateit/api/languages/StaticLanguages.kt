package com.rm.translateit.api.languages

import com.rm.translateit.api.models.Language

class StaticLanguages: Languages {
    override fun all(): List<Language> {
        return listOf(
                Language("EN", "English"),
                Language("PL", "Polish"),
                Language("RU", "Russian")
        )
    }

    override fun originLanguages(): List<Language> {
        return all()
    }

    override fun destinationLanguages(exceptOriginCode: String): List<Language> {
        return all().filter { language -> language.code != exceptOriginCode.toUpperCase() }
    }

    override fun updateOriginLastUsage(model: Language) {

    }

    override fun updateDestinationLastUsage(model: Language) {

    }
}