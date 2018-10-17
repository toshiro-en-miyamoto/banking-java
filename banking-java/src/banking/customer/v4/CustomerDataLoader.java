package banking.customer.v4;

import java.util.Map;

interface CustomerDataLoader {

   Map<CustomerNumber, Customer> loadCustomers();

}
