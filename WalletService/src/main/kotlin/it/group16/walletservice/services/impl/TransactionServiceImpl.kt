package it.group16.walletservice.services.impl

import it.group16.walletservice.domain.Transaction
import it.group16.walletservice.dto.TransactionDTO
import it.group16.walletservice.repositories.TransactionRepository
import it.group16.walletservice.repositories.WalletRepository
import it.group16.walletservice.services.TransactionsService
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class TransactionServiceImpl(var transactionRepo: TransactionRepository, var walletRepo: WalletRepository): TransactionsService{

	override fun getTransactionById(transactionId: Long): TransactionDTO? {
		val transaction = transactionRepo.findById(transactionId)
		return if (transaction.isPresent) transaction.get().toDto()
		else null
	}

	override fun saveTransactions(transactionDTOInput: TransactionDTO): TransactionDTO? {

		val walletFrom=walletRepo.findById(transactionDTOInput.walletFromId)
		val walletTo=walletRepo.findById(transactionDTOInput.walletToId)
        if (walletFrom.isPresent && walletTo.isPresent) {
			return if (walletFrom.get().amount >= transactionDTOInput.amountTransferred) {
				val transaction = Transaction(null, transactionDTOInput.amountTransferred, Date(), walletFrom.get(),walletTo.get())
				transactionRepo.save(transaction)
				walletFrom.get().amount = walletFrom.get().amount - transactionDTOInput.amountTransferred
				walletTo.get().amount = walletTo.get().amount + transactionDTOInput.amountTransferred
				walletRepo.save(walletFrom.get())
				walletRepo.save(walletTo.get())
				transaction.toDto()
			} else {
				System.err.println("Payer does not have enough money")
				null
			}
        }else{
			System.err.println("One of referenced wallets does not exist")
			return null
		}
	}

}