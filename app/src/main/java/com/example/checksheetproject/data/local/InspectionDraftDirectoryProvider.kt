package com.example.checksheetproject.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

interface InspectionDraftDirectoryProvider {
    fun draftDirectory(): File
}

class AppInspectionDraftDirectoryProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : InspectionDraftDirectoryProvider {
    override fun draftDirectory(): File {
        return File(context.filesDir, DRAFT_DIRECTORY_NAME)
    }

    private companion object {
        const val DRAFT_DIRECTORY_NAME = "inspection_drafts"
    }
}
