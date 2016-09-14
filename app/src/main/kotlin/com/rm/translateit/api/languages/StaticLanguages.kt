package com.rm.translateit.api.languages

import com.rm.translateit.api.translation.models.Language

class StaticLanguages: Languages {
    override fun languages(): List<Language> {
        return listOf(
                Language("EN", "English"),
                Language("PL", "Polish"),
                Language("RU", "Russian")
        )
    }
}