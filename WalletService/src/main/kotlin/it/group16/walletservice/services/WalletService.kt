package it.group16.walletservice.services

import it.group16.walletservice.domain.Wallet
import it.group16.walletservice.dto.TransactionDTO
import it.group16.walletservice.dto.WalletDTO
import org.springframework.stereotype.Service

@Service
interface WalletService {
    /*
    * Queries the database and returns the WalletDTO corresponding to the walletId passed as parameter.
    * Throws an exception in case of invalid (negative) walletId
    * Returns null if no wallet is found with the given Id
    */

    fun getWalletById(walletId: Long): WalletDTO?

    /*
     * Stores the wallet passed as parameter in the application database
     */
    fun saveWallet(wallet: Wallet): Boolean

    /*
     * Stores the wallet passed as parameter in the application database
     */
    fun createWallet(customerId: Long): WalletDTO?

    /*
     * Deletes the wallet with the given Id from the database.
     * Throws an exception in case of invalid (negative) walletId
   	 * Returns true if the wallet is deleted, false otherwise
     */
    fun deleteWallet(walletId: Long)

    /* get transactions into and from a wallet */
    fun getWalletTransactions(walletId: Long): List<TransactionDTO>?
    fun getWalletTransaction(walletId: Long, transactionId: Long): TransactionDTO?
    fun transactionsByDate(walletId: Long, startDate: Long, endDate: Long): List<TransactionDTO>?
}