package it.group16.walletservice.repositories

import it.group16.walletservice.domain.Wallet
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository : CrudRepository<Wallet, Long>