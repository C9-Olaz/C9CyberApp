package com.c9cyber.app.domain.smartcard

class SmartCardManager(
    val transport: SmartCardTransport,
    val monitor: SmartCardMonitor
) {
    val presenceState = monitor.presenceState

}