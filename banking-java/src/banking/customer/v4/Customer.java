package banking.customer.v4;

import java.util.Optional;
import banking.branch.v4.Branch;

public interface Customer extends Comparable<Customer> {

   CustomerNumber getCustomerNumber();      // relevant to Customer.equals()
   Optional<Branch> getBranch();
   String getCustomerName();

}
