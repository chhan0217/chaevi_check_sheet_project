package com.example.checksheetproject

import android.app.Application
import com.example.checksheetproject.domain.usecase.DeleteExpiredInspectionDraftsUseCase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class CheckSheetApplication : Application() {
    @Inject
    lateinit var deleteExpiredInspectionDraftsUseCase: DeleteExpiredInspectionDraftsUseCase

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            runCatching {
                deleteExpiredInspectionDraftsUseCase()
            }
        }
    }
}
