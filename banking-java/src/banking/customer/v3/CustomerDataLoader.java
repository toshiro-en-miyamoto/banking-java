package banking.customer.v3;

import java.util.Map;

interface CustomerDataLoader {

   Map<CustomerNumber, Customer> loadCustomers();

}
