package it.group16.walletservice.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import it.group16.walletservice.dto.TransactionDTO
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
class Transaction (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq")
    @GenericGenerator(name = "seq", strategy="increment")
    var id: Long? = null,

    @field:Min(0, message = "Amount should always be positive")
    var amountTransferred: BigDecimal,

    var transactionTime: Date,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn
    var walletFrom: Wallet,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn
    var walletTo: Wallet
){
    constructor() : this(null,BigDecimal.ZERO,Date(), Wallet(),Wallet()) {

    }

    fun toDto(): TransactionDTO =
        TransactionDTO(this.amountTransferred, Date(), this.walletFrom.id!!, this.walletTo.id!!)
}