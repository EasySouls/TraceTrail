package dev.easysouls.tracetrail.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PermissionResult
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.easysouls.tracetrail.domain.PermissionHandler
import javax.inject.Inject

class PermissionHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PermissionHandler {

    private val permissionLauncher = ActivityResultContracts.RequestPermission()

    override fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(permission: String) {
        val result = permissionLauncher.createIntent(context, permission)
        return context.startActivity(result)
    }
}