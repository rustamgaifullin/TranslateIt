package com.rm.translateit.api.languages

import com.rm.translateit.api.models.LanguageModel

interface Languages {
    fun all(): List<LanguageModel>
    fun updateOriginLastUsage(model: LanguageModel)
    fun updateDestinationLastUsage(model: LanguageModel)
    fun originLanguages(): List<LanguageModel>
    fun destinationLanguages(exceptOriginCode: String): List<LanguageModel>
}