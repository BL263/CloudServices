package it.group16.walletservice.dto

import javax.validation.constraints.Min

data class TransactionForm(@Min(0) var amount: Long, var walletToId: Long )