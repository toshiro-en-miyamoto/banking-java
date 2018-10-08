package banking.customer.v3;

import java.util.Optional;
import banking.branch.v3.Branch;

public interface Customer extends Comparable<Customer> {

   CustomerNumber getCustomerNumber();      // relevant to Customer.equals()
   Optional<Branch> getBranch();
   String getCustomerName();

}
