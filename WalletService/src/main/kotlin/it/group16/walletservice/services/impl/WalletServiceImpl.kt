package it.group16.walletservice.services.impl

import it.group16.walletservice.domain.Customer
import it.group16.walletservice.domain.Wallet
import it.group16.walletservice.dto.TransactionDTO
import it.group16.walletservice.dto.WalletDTO
import it.group16.walletservice.repositories.CustomerRepository
import it.group16.walletservice.repositories.TransactionRepository
import it.group16.walletservice.repositories.WalletRepository
import it.group16.walletservice.services.WalletService
import org.springframework.expression.spel.SpelEvaluationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Service
@Transactional
class WalletServiceImpl(
    var walletRepository: WalletRepository,
    var customerRepository: CustomerRepository,
    var transactionRepository: TransactionRepository): WalletService {


   // @PostAuthorize("returnObject.customerId==authentication.principal.customerId or authentication.principal.roles.contains('ADMIN')")
    override fun getWalletById(walletId: Long): WalletDTO? {
        return try{
            if (walletRepository.existsById(walletId)) {
                walletRepository.findById(walletId).get().toDto()
            } else {
                System.err.println("Wallet does not exist")
                null
            }
        } catch(e: SpelEvaluationException){
            null
        }

    }

    override fun createWallet(customerId: Long): WalletDTO? {
        return if (customerRepository.existsById(customerId)) {
            val owner: Customer = customerRepository.findById(customerId).get()
            val wallet = walletRepository.save(Wallet(customer = owner))
            wallet.toDto()
        } else {
            System.err.println("Customer does not exist")
            null
        }
    }

    override fun saveWallet(wallet: Wallet): Boolean {
        walletRepository.save(wallet)
        return true
    }

    override fun deleteWallet(walletId: Long) {
        val walletFound = walletRepository.findById(walletId)
        if (walletFound.isPresent) {
            walletRepository.delete(walletFound.get())
        }

    }

    override fun getWalletTransactions(walletId: Long): List<TransactionDTO>? {
        val wallet = walletRepository.findById(walletId)
        if (wallet.isPresent) {
            val walletList = transactionRepository.findByWalletFromOrWalletTo(wallet.get(), wallet.get())
            if (!walletList.isNullOrEmpty()) {
                return walletList.map { it.toDto() }
            }
        }
        return null
    }

    override fun getWalletTransaction(walletId: Long, transactionId: Long): TransactionDTO? {
        if (walletRepository.existsById(walletId)) {
            if (transactionRepository.existsById(transactionId)) {
                val transactionDTO = transactionRepository.findById(transactionId).get().toDto()
                if (transactionDTO.walletFromId == walletId || transactionDTO.walletToId == walletId) {
                    return transactionDTO
                } else System.err.println("Transaction and wallet are not related")
            } else System.err.println("Transaction does not exist")
        } else System.err.println("Wallet does not exist")
        return null
    }

    override fun transactionsByDate(walletId: Long, startDate: Long, endDate: Long): List<TransactionDTO>? {
        return if (walletRepository.existsById(walletId)) {
            val transactions = transactionRepository.findAllByOwnerRangeDate(
                walletId, Date(startDate), Date(endDate)
            )
            transactions.map { t -> t.toDto() }
        } else {
            System.err.println("Wallet does not exist")
            null
        }
    }
}









