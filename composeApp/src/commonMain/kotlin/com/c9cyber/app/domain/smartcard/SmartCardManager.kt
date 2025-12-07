package com.c9cyber.app.domain.smartcard

import com.c9cyber.app.utils.AppletAID
import com.c9cyber.app.utils.AppletCLA
import com.c9cyber.app.utils.INS
import com.c9cyber.app.utils.buildSelectApdu
import com.c9cyber.app.utils.getStatusWord

sealed class PinVerifyResult {
    data object Success : PinVerifyResult()
    data object CardLocked : PinVerifyResult()
    data class WrongPin(val remainingTries: Int) : PinVerifyResult()
    data class Error(val message: String) : PinVerifyResult()
}

sealed class UnblockResult {
    data object Success : UnblockResult()
    data class Error(val message: String) : UnblockResult()
}

class SmartCardManager(
    val transport: SmartCardTransport,
    val monitor: SmartCardMonitor
) {
    val presenceState = monitor.presenceState

    fun trySelectApplet(): Boolean {
        return try {
            val readers = transport.listReaders()
            if (readers.isEmpty()) return false

            val reader = readers[0]

            if (!transport.connect(reader)) return false

            val selectApdu = buildSelectApdu(AppletAID)
            val response = transport.transmit(selectApdu)

            val sw = getStatusWord(response)

            if (sw == 0x9000) {
                true
            } else {
                transport.disconnect()
                false
            }

        } catch (e: Exception) {
            transport.disconnect()
            false
        }
    }

    fun verifyPin(pin: String): PinVerifyResult {
        return try {
            val pinBytes = pin.toByteArray()
            val apdu = byteArrayOf(AppletCLA, INS.VerifyPin, 0x00, 0x00, pinBytes.size.toByte()) + pinBytes

            val response = transport.transmit(apdu)
            val sw = getStatusWord(response)

            when {
                sw == 0x9000 -> PinVerifyResult.Success
                sw == 0x6982 -> PinVerifyResult.CardLocked
                (sw ushr 8) == 0x63 -> PinVerifyResult.WrongPin(sw and 0x0F)
                else -> PinVerifyResult.Error("SW=${Integer.toHexString(sw)}")
            }
        } catch (e: Exception) {
            PinVerifyResult.Error("Lỗi kết nối")
        }
    }

    fun unblockPin(): UnblockResult {
        return try {
            val readers = transport.listReaders()

            if (!transport.connect(readers[0])) {
                return UnblockResult.Error("Không kết nối được tới thẻ")
            }

            val selectApdu = buildSelectApdu(AppletAID)
            val selectRes = transport.transmit(selectApdu)

            if (getStatusWord(selectRes) != 0x9000) {
                transport.disconnect()
                return UnblockResult.Error("Lỗi thẻ")
            }

            val unblockApdu = byteArrayOf(AppletCLA, INS.UnblockPin, 0x00, 0x00, 0x00)
            val response = transport.transmit(unblockApdu)
            val sw = getStatusWord(response)

            if (sw == 0x9000) {
                UnblockResult.Success
            } else {
                transport.disconnect()
                UnblockResult.Error("Mở khóa thất bại: ${Integer.toHexString(sw)}")
            }

        } catch (e: Exception) {
            transport.disconnect()
            UnblockResult.Error("Lỗi: ${e.message}")
        }
    }

}