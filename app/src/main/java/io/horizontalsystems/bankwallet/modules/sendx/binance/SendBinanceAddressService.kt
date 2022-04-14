package io.horizontalsystems.bankwallet.modules.sendx.binance

import io.horizontalsystems.bankwallet.R
import io.horizontalsystems.bankwallet.core.ISendBinanceAdapter
import io.horizontalsystems.bankwallet.core.adapters.zcash.ZcashAdapter
import io.horizontalsystems.bankwallet.core.providers.Translator
import io.horizontalsystems.bankwallet.entities.Address
import io.horizontalsystems.bitcoincore.exceptions.AddressFormatException
import io.horizontalsystems.hodler.HodlerPlugin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SendBinanceAddressService(private val adapter: ISendBinanceAdapter) {

    private var address: Address? = null
    private var validAddress: Address? = null
    private var addressError: Throwable? = null

    private val _stateFlow = MutableStateFlow(
        State(
            validAddress = validAddress,
            addressError = addressError,
            canBeSend = validAddress != null
        )
    )
    val stateFlow = _stateFlow.asStateFlow()

    fun start() {
        validateAddress()
        refreshValidAddress()

        emitState()
    }

    fun setAddress(address: Address?) {
        this.address = address

        validateAddress()
        refreshValidAddress()

        emitState()
    }

    private fun refreshValidAddress() {
        validAddress = if (addressError == null) address else null
    }

    private fun validateAddress() {
        addressError = null
        val address = this.address ?: return

        try {
            adapter.validate(address.hex)
        } catch (e: Exception) {
            addressError = getError(e)
        }
    }

    private fun getError(error: Throwable): Throwable {
        val message = when (error) {
            is HodlerPlugin.UnsupportedAddressType -> Translator.getString(R.string.Send_Error_UnsupportedAddress)
            is AddressFormatException -> Translator.getString(R.string.SwapSettings_Error_InvalidAddress)
            is ZcashAdapter.ZcashError.TransparentAddressNotAllowed -> Translator.getString(R.string.Send_Error_TransparentAddress)
            is ZcashAdapter.ZcashError.SendToSelfNotAllowed -> Translator.getString(R.string.Send_Error_SendToSelf)
            else -> error.message ?: error.javaClass.simpleName
        }

        return Throwable(message)
    }

    private fun emitState() {
        _stateFlow.update {
            State(
                validAddress = validAddress,
                addressError = addressError,
                canBeSend = validAddress != null
            )
        }
    }

    data class State(
        val validAddress: Address?,
        val addressError: Throwable?,
        val canBeSend: Boolean
    )
}
