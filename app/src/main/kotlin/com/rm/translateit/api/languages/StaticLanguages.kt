package com.rm.translateit.api.languages

import com.rm.translateit.api.models.Language

class StaticLanguages: Languages {
    override fun languages(): List<Language> {
        return listOf(
                Language("EN", "English"),
                Language("PL", "Polish"),
                Language("RU", "Russian")
        )
    }

    override fun originLanguages(): List<Language> {
        return languages()
    }

    override fun destinationLanguages(exceptOrigin: String): List<Language> {
        return languages().filter { language -> language.code != exceptOrigin.toUpperCase() }
    }

    override fun updateLastUsage(model: Language) {

    }
}