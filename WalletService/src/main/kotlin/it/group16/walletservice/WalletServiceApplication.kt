package it.group16.walletservice

import it.group16.walletservice.domain.Customer
import it.group16.walletservice.domain.Wallet
import it.group16.walletservice.dto.TransactionDTO
import it.group16.walletservice.repositories.CustomerRepository
import it.group16.walletservice.repositories.TransactionRepository

import it.group16.walletservice.repositories.WalletRepository
import it.group16.walletservice.services.impl.CustomerServiceImpl
import it.group16.walletservice.services.impl.TransactionServiceImpl
import it.group16.walletservice.services.impl.WalletServiceImpl
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.EnableScheduling
import java.math.BigDecimal
import java.util.*




@EnableEurekaClient
@EnableScheduling
@Configuration
@SpringBootApplication
class WalletServiceApplication  {



    @Bean
    fun test(
        customerRepo: CustomerRepository,
        walletRepo: WalletRepository,
        transactionRepo: TransactionRepository): CommandLineRunner {
        return CommandLineRunner {
            val customerService = CustomerServiceImpl(customerRepo)
            val walletService = WalletServiceImpl(walletRepo, customerRepo, transactionRepo)
            val transactionService = TransactionServiceImpl(transactionRepo, walletRepo)


            val c1 = Customer(name = "mirco", surname = "vigna", email = "mirco@gmail.com", deliveryAddress = "roveda 29")
            val c2 = Customer(name = "andrea", surname = "votte", email = "andrea@gmail.com", deliveryAddress = "roveda 29")
            val c3 = Customer(name = "martina", surname = "manci", email = "martina@gmail.com", deliveryAddress = "roveda 29")
            val c4 = Customer(name = "irene", surname = "malde", email = "irene@gmail.com", deliveryAddress = "roveda 29")

            customerRepo.save(c1)
            customerRepo.save(c2)
            customerRepo.save(c3)
            customerRepo.save(c4)

            println(walletService.createWallet(1))
            println(walletService.createWallet(2))
            println(walletService.createWallet(3))
            println(walletService.createWallet(4))
            val w1 = Wallet(amount = BigDecimal(100), customer = c3)
            walletRepo.save(w1)

            transactionService.saveTransactions(TransactionDTO(BigDecimal(55), Date(), 5, 2))
            transactionService.saveTransactions(TransactionDTO(BigDecimal(15), Date(), 5, 1))
            transactionService.saveTransactions(TransactionDTO(BigDecimal(5), Date(), 2, 4))
            transactionService.saveTransactions(TransactionDTO(BigDecimal(10), Date(), 1, 5))
            transactionService.saveTransactions(TransactionDTO(BigDecimal(3), Date(), 5, 3))
            println(walletService.getWalletTransactions(1))


            /*val customer = Customer(null, "mirco", "vigna", "torino", "admin@gmail.com")
            val user = User(null, "mitchell", BCryptPasswordEncoder().encode("12345"), "admin@gmail.com", true, "ADMIN CUSTOMER", customer)
            customerRepo.save(customer)
            userRepo.save(user)
            val w2 = Wallet(amount = BigDecimal(100), customer = customer)
            walletRepo.save(w2)*/

        }
    }
}


fun main(args: Array<String>) {
    runApplication<WalletServiceApplication>(*args)
}





