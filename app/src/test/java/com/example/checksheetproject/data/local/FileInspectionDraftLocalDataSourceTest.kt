package com.example.checksheetproject.data.local

import com.example.checksheetproject.data.remote.dto.InspectionSubmissionRequest
import java.io.File
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FileInspectionDraftLocalDataSourceTest {
    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `하루가 지난 임시저장 파일은 삭제하고 최신 파일은 유지한다`() = runTest {
        val draftDirectory = temporaryFolder.newFolder("inspection_drafts")
        val dataSource = createDataSource(draftDirectory)
        val nowMillis = 2L * DAY_MILLIS
        val expiredDraft = File(draftDirectory, "expired.json")
        val activeDraft = File(draftDirectory, "active.json")

        expiredDraft.writeText(
            json.encodeToString(
                draftRequest(
                    chargerId = "CHB-001",
                    createdAtMillis = nowMillis - DAY_MILLIS,
                ),
            ),
        )
        activeDraft.writeText(
            json.encodeToString(
                draftRequest(
                    chargerId = "CHB-002",
                    createdAtMillis = nowMillis - DAY_MILLIS + 1L,
                ),
            ),
        )

        dataSource.deleteExpiredDrafts(nowMillis = nowMillis)

        assertFalse(expiredDraft.exists())
        assertTrue(activeDraft.exists())
    }

    @Test
    fun `읽을 수 없는 임시저장 파일은 삭제한다`() = runTest {
        val draftDirectory = temporaryFolder.newFolder("inspection_drafts")
        val dataSource = createDataSource(draftDirectory)
        val brokenDraft = File(draftDirectory, "broken.json")
        brokenDraft.writeText("{ broken")

        dataSource.deleteExpiredDrafts(nowMillis = DAY_MILLIS)

        assertFalse(brokenDraft.exists())
    }

    private fun createDataSource(draftDirectory: File): FileInspectionDraftLocalDataSource {
        return FileInspectionDraftLocalDataSource(
            inspectionDraftDirectoryProvider = object : InspectionDraftDirectoryProvider {
                override fun draftDirectory(): File = draftDirectory
            },
            json = json,
        )
    }

    private fun draftRequest(
        chargerId: String,
        createdAtMillis: Long,
    ): InspectionSubmissionRequest {
        return InspectionSubmissionRequest(
            chargerId = chargerId,
            inspectionMonth = "2026-06",
            inspectorId = "",
            createdAtMillis = createdAtMillis,
            createdAtDateTime = "2026-06-01 10:00:00",
            groups = emptyList(),
        )
    }

    private companion object {
        const val DAY_MILLIS = 24L * 60L * 60L * 1000L
    }
}
