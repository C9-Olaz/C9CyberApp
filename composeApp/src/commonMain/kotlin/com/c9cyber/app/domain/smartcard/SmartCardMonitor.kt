package com.c9cyber.app.domain.smartcard

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CardPresenceStatus {
    Present,
    Absent
}

class SmartCardMonitor(
    private val smartCardTransport: SmartCardTransport
) {
    private val _presenceState = MutableStateFlow(CardPresenceStatus.Absent)
    val presenceState = _presenceState.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var job: Job? = null


    fun startMonitoring(
        onCardRemoved: () -> Unit,
        onCardInserted: () -> Unit
    ) {
        job?.cancel()

        job = scope.launch {
            while (isActive) {
                cardPresenceMonitoring(
                    onCardRemoved = onCardRemoved,
                    onCardInserted = onCardInserted
                )

                delay(1000)
            }
        }
    }

    private suspend fun cardPresenceMonitoring(
        onCardRemoved: () -> Unit = {},
        onCardInserted: () -> Unit = {}
    ) {
        try {
            val readers = smartCardTransport.listReaders()

            if (readers.isEmpty() || !smartCardTransport.isCardPresent(readers[0])) {

                smartCardTransport.disconnect()

                if (_presenceState.value == CardPresenceStatus.Present) {
                    withContext(Dispatchers.Main) { onCardRemoved() }
                }

                _presenceState.value = CardPresenceStatus.Absent


                return
            } else {
                if (_presenceState.value == CardPresenceStatus.Absent) {
                    withContext(Dispatchers.Main) { onCardInserted() }
                }
                _presenceState.value = CardPresenceStatus.Present
            }
        } catch (_: Exception) {
            withContext(Dispatchers.Main) { onCardRemoved() }
            return
        }
    }
}