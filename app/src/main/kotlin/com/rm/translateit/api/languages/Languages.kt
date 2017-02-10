package com.rm.translateit.api.languages

import com.rm.translateit.api.models.Language

interface Languages {
    fun languages(): List<Language>
    fun updateOriginLastUsage(model: Language)
    fun updateDestinationLastUsage(model: Language)
    fun originLanguages(): List<Language>
    fun destinationLanguages(exceptOriginCode: String): List<Language>
}