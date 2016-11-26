package com.rm.translateit.api.languages

import com.rm.translateit.api.models.Language

interface Languages {
    fun languages(): List<Language>
    fun updateLastUsage(model: Language)
    fun originLanguages(): List<Language>
    fun destinationLanguages(exceptOrigin: String): List<Language>
}