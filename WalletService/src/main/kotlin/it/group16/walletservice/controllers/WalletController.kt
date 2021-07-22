package it.group16.walletservice.controllers

import it.group16.walletservice.dto.TransactionDTO
import it.group16.walletservice.dto.TransactionForm
import it.group16.walletservice.services.CustomerService
import it.group16.walletservice.services.TransactionsService
import it.group16.walletservice.services.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.expression.spel.SpelEvaluationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*


@RestController
@RequestMapping("/wallet")
class WalletController {

    @Autowired
    lateinit var walletService: WalletService

    @Autowired
    lateinit var transactionService: TransactionsService

    @Autowired
    lateinit var customerService: CustomerService


    @PostMapping(path = ["/"])
    @ResponseStatus(HttpStatus.CREATED)
  //  @PreAuthorize("authentication.principal.customerId==#customerId or authentication.principal.roles.contains('ADMIN')")
    fun addWallet(@RequestParam customerId: Long): ResponseEntity<Any> {
        return try{
            val wallet = walletService.createWallet(customerId)
            if(wallet != null){
                ResponseEntity.ok(wallet)
            } else ResponseEntity.badRequest().body("Bad Request Message")
        } catch (e: SpelEvaluationException){
            ResponseEntity.badRequest().body("Bad Request Message")
        }


    }

    @GetMapping("/{walletId}")
    @ResponseStatus(HttpStatus.OK)
    fun getWallet(@PathVariable walletId: Long): ResponseEntity<Any> {
        val wallet = walletService.getWalletById(walletId)
        return if(wallet!=null){
            ResponseEntity.ok(wallet)
        } else ResponseEntity.badRequest().body("Bad Request Message")
    }

    @PostMapping("/{walletId}/transaction")
    @ResponseStatus(HttpStatus.CREATED)
  //  @PreAuthorize("authentication.principal.roles.contains('CUSTOMER')")
    fun createTransactionByWalletId(@PathVariable walletId: Long,@RequestBody transactionForm: TransactionForm): ResponseEntity<Any> {
        if (walletService.getWalletById(walletId)!==null){
            val transactionDTO = transactionService.saveTransactions(TransactionDTO(BigDecimal(transactionForm.amount),Date(),walletId ,transactionForm.walletToId))
            if (transactionDTO!=null){
                return ResponseEntity.ok(transactionDTO)
            }
            }
            return ResponseEntity.badRequest().body("Bad Request Message")
    }

    @GetMapping("/{walletId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    fun transactionByWalletIdAndDate(
        @PathVariable walletId: Long,
        @RequestParam(
            name = "from",
            required = false,
            defaultValue = "1617975717827"
        ) dateFrom: Long,
        @RequestParam(
            name = "to",
            required = false,
            defaultValue = "1617975717827"
        ) dateTo: Long
    ): ResponseEntity<Any> {
        if (walletService.getWalletById(walletId)!==null){
            val list = walletService.transactionsByDate(walletId, dateFrom, dateTo)
            return if (list!=null){
                ResponseEntity.ok(list)
            } else ResponseEntity.badRequest().body("Bad Request Message")
        } else return ResponseEntity.badRequest().body("Bad Request Message")

    }

    @GetMapping("/{walletId}/transactions/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    fun transactionByTransactionID(@PathVariable walletId: Long, @PathVariable transactionId: Long): ResponseEntity<Any> {
        if (walletService.getWalletById(walletId)!==null){
            val transaction = walletService.getWalletTransaction(walletId, transactionId)
            return if (transaction!=null){
                ResponseEntity.ok(transaction)
            } else ResponseEntity.badRequest().body("Bad Request Message")
        }else return ResponseEntity.badRequest().body("Bad Request Message")
    }
}