package banking.customer.v2;

import java.util.Map;

interface CustomerDataLoader {

   Map<CustomerNumber, Customer> loadCustomers();

}
