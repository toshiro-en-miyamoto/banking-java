package banking.customer.v2;

import java.util.Optional;
import banking.branch.v2.Branch;

public interface Customer extends Comparable<Customer> {

   CustomerNumber getCustomerNumber();      // relevant to Customer.equals()
   Optional<Branch> getBranch();
   String getCustomerName();

}
