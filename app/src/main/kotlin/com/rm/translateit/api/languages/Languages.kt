package com.rm.translateit.api.languages

import com.rm.translateit.api.translation.models.Language

interface Languages {
    fun languages(): List<Language>
}