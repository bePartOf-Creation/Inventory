package com.mq.kafkaproducer.config;

import com.mq.kafkaproducer.models.Address;
import com.mq.kafkaproducer.models.Customer;
import com.mq.kafkaproducer.repository.AddressRepository;
import com.mq.kafkaproducer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DataBaseSeeder implements CommandLineRunner {

   @Autowired
   private CustomerRepository customerRepo;
   @Autowired
   private AddressRepository addressRepo;
    @Override
    public void run(String... args) {
        Address address1 = Address.builder().addressNumber(2).nameOfStreet("Tapa Street, Orile- Iganmu").stateOfCity("Lagos").build();
        Address address2 = Address.builder().addressNumber(22).nameOfStreet("Tapa Street, Orile- Iganmu").stateOfCity("Lagos").build();
        Address address3 = Address.builder().addressNumber(12).nameOfStreet("Tapa Street, Orile- Iganmu").stateOfCity("Lagos").build();
        
        Address addressOne = this.addressRepo.save(address1);
        Address addressTwo = this.addressRepo.save(address2);
        Address addressThree = this.addressRepo.save(address3);
        
        
        Customer customer1 = Customer.builder().firstName("Olufunlola").lastName("Olasunkanmi").phoneNumber("08085690901").emailAddress("test@gamil.com").customerAddress(addressOne).build();
        Customer customer2 = Customer.builder().firstName("Ola").lastName("Olasunkanmi").phoneNumber("08085690901").emailAddress("ola@gamil.com").customerAddress(addressTwo).build();
        Customer customer3 = Customer.builder().firstName("Lola").lastName("Olasunkanmi").phoneNumber("08085690901").emailAddress("lola@gamil.com").customerAddress(addressThree).build();
        
        this.customerRepo.save(customer1);
        this.customerRepo.save(customer2);
        this.customerRepo.save(customer3);
    }   
}
