package it.group16.walletservice.services.impl

import it.group16.walletservice.domain.Customer
import it.group16.walletservice.repositories.CustomerRepository
import it.group16.walletservice.services.CustomerService
import it.group16.walletservice.dto.CustomerDTO
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class CustomerServiceImpl(var repository: CustomerRepository): CustomerService {
    override fun getCustomerById(customerId: Long): CustomerDTO? {
        val customer = repository.findById(customerId)
        return if (customer.isPresent) customer.get().toDto()
        else null
    }

    override fun saveCustomer(customerDTO: CustomerDTO): CustomerDTO? {
        val customer = Customer(name = customerDTO.name, surname = customerDTO.surname, deliveryAddress = customerDTO.deliveryAddress, email = customerDTO.email)
        return repository.save(customer).toDto()
    }

}


