package com.example.checksheetproject.data.local

import android.content.Context
import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FileInspectionDraftLocalDataSource @Inject constructor(
    private val inspectionDraftDirectoryProvider: InspectionDraftDirectoryProvider,
    private val json: Json,
) : InspectionDraftLocalDataSource {
    override suspend fun getDraft(
        chargerId: String,
        inspectionMonth: String,
    ): InspectionSubmissionRequest? {
        return withContext(Dispatchers.IO) {
            val draftFile = draftFile(
                chargerId = chargerId,
                inspectionMonth = inspectionMonth,
            )
            if (!draftFile.exists()) {
                null
            } else {
                json.decodeFromString<InspectionSubmissionRequest>(draftFile.readText())
            }
        }
    }

    override suspend fun saveDraft(request: InspectionSubmissionRequest) {
        withContext(Dispatchers.IO) {
            val draftFile = draftFile(
                chargerId = request.chargerId,
                inspectionMonth = request.inspectionMonth,
            )
            draftFile.parentFile?.mkdirs()
            draftFile.writeText(json.encodeToString(request))
        }
    }

    override suspend fun deleteDraft(
        chargerId: String,
        inspectionMonth: String,
    ) {
        withContext(Dispatchers.IO) {
            val draftFile = draftFile(
                chargerId = chargerId,
                inspectionMonth = inspectionMonth,
            )
            if (draftFile.exists()) {
                draftFile.delete()
            }
        }
    }

    override suspend fun deleteExpiredDrafts(nowMillis: Long) {
        withContext(Dispatchers.IO) {
            val draftDirectory = draftDirectory()
            if (!draftDirectory.exists()) return@withContext

            draftDirectory.listFiles { file -> file.extension == "json" }
                ?.forEach { draftFile ->
                    runCatching {
                        val draft = json.decodeFromString<InspectionSubmissionRequest>(
                            draftFile.readText(),
                        )
                        if (nowMillis - draft.createdAtMillis >= DRAFT_EXPIRATION_MILLIS) {
                            draftFile.delete()
                        }
                    }.onFailure {
                        draftFile.delete()
                    }
                }
        }
    }

    private fun draftFile(
        chargerId: String,
        inspectionMonth: String,
    ): File {
        return File(
            draftDirectory(),
            "${draftKey(chargerId, inspectionMonth)}.json",
        )
    }

    private fun draftDirectory(): File {
        return inspectionDraftDirectoryProvider.draftDirectory()
    }

    private fun draftKey(
        chargerId: String,
        inspectionMonth: String,
    ): String {
        val rawKey = "$chargerId-$inspectionMonth"
        return URLEncoder.encode(
            rawKey,
            StandardCharsets.UTF_8.name(),
        )
    }

    private companion object {
        const val DRAFT_EXPIRATION_MILLIS = 24L * 60L * 60L * 1000L
    }
}
