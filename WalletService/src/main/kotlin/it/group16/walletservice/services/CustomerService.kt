package it.group16.walletservice.services

import it.group16.walletservice.dto.CustomerDTO


interface CustomerService {
    /*
	 * Queries the database and returns the CustomerDto corresponding to the customerId passed as parameter.
	 * Throws an exception in case of invalid (negative) customerId
	 * Returns null if no customer is found with the given Id
	 */
    fun getCustomerById(customerId: Long): CustomerDTO?

    /*
     * Stores the customer passed as parameter in the application database
     */
    fun saveCustomer(customerDTO: CustomerDTO): CustomerDTO?

}