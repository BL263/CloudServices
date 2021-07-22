package it.group16.walletservice.dto

import java.math.BigDecimal

data class WalletDTO(
    var id: Long,
    var amount: BigDecimal,
    var customerId: Long
)
