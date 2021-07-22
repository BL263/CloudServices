package it.group16.walletservice.domain

import com.fasterxml.jackson.annotation.JsonManagedReference
import it.group16.walletservice.dto.WalletDTO
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.Min

@Entity
class Wallet (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq")
    @GenericGenerator(name = "seq", strategy="increment")
    var id: Long? = null,

    @field:Min(0, message = "Amount should always be positive")
    var amount: BigDecimal =  BigDecimal.ZERO,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    var customer: Customer,

    @OneToMany(mappedBy = "walletFrom", fetch = FetchType.LAZY, targetEntity = Transaction::class)
    @JsonManagedReference
    var payees: MutableSet<Transaction> = mutableSetOf() ,

    @OneToMany(mappedBy = "walletTo", fetch = FetchType.LAZY, targetEntity =  Transaction::class)
    @JsonManagedReference
    var payers: MutableSet<Transaction> = mutableSetOf() ,
)
{
    constructor() : this(null, BigDecimal.ZERO,Customer(), mutableSetOf(), mutableSetOf()) {

    }

    fun toDto(): WalletDTO = WalletDTO(this.id!!, this.amount, this.customer.id!!)
}