package banking.customer.v4;

import java.util.Optional;

import banking.branch.v4.Branch;
import banking.branch.v4.Branches;

public final class PrototypeCustomer implements Customer {
   private final CustomerNumber customerNumber;
   private final Optional<Branch> branch;
   private final String customerName;

   @Override public CustomerNumber getCustomerNumber() { return customerNumber; }
   @Override public String getCustomerName() { return customerName; }
   @Override public Optional<Branch> getBranch() { return branch; }

   private PrototypeCustomer(CustomerNumber customerNumber, Optional<Branch> branch, String customerName) {
      this.customerNumber = CustomerNumber.of(customerNumber);
      this.branch = branch.map(Branches::copy);
      this.customerName = new String(customerName);
   }

   static Customer of(CustomerNumber customerNumber, Optional<Branch> branch, String customerName) {
      return new PrototypeCustomer(customerNumber, branch, customerName);
   }

   static Customer of(CustomerNumber customerNumber, Optional<Branch> branch) {
      return new PrototypeCustomer(customerNumber, branch, String.valueOf(customerNumber));
   }

   static Customer of(Customer customer) {
      return of(
            ((PrototypeCustomer) customer).customerNumber,
            ((PrototypeCustomer) customer).branch,
            ((PrototypeCustomer) customer).customerName
      );
   }

   @Override public String toString() {
      return branch.isPresent()
            ? String.format("(%s) %s", Branches.toBranchCode(branch.get()), customerNumber)
            : String.format("(***) %s", customerNumber);
   }

   @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;
      return customerNumber == ((PrototypeCustomer) o).customerNumber;
   }

   @Override public int hashCode() {
      return customerNumber.hashCode();
   }

   @Override public int compareTo(Customer c) {
      return customerNumber.compareTo(((PrototypeCustomer) c).customerNumber);
   }

}
