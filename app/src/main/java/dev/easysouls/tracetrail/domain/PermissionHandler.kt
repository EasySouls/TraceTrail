package dev.easysouls.tracetrail.domain

interface PermissionHandler {
    fun hasPermission(permission: String): Boolean
    fun requestPermission(permission: String)
}