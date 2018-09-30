package banking.v2;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

import banking.branch.v2.Branch;
import banking.branch.v2.Branches;
import banking.customer.v2.Customer;
import banking.customer.v2.CustomerNumber;
import banking.customer.v2.Customers;

public final class BankStream2 {

   private static void testBranch() {

      Map<Integer, Branch> branches = Branches.getMapBranchCodeToBranch();

      assert 3
      == branches.size() : "the number of branches";

   }

   private static void testCustomer() {

      Map<CustomerNumber, Customer> customers = Customers.getMapCustomerNumberToCustomer();

      assert 8
      == customers.size() : "the number of customers";

   }

   private static void introCollect() {

      Map<CustomerNumber, Customer> customers = Customers.getMapCustomerNumberToCustomer();

      Set<Branch> branches1
      = customers.values()
            .stream()
            .map(Customer::getBranch)
            .filter(Optional<Branch>::isPresent)
            .map(Optional::get)
            .collect(
                  () -> new HashSet<>(),              // supplier
                  (set, branch) -> set.add(branch),   // accumulator
                  (set, other) -> set.addAll(other)   // combiner
            );

      assert 3
      == branches1.size() : "the number of branches #1";

      Set<Branch> branches2
      = customers.values()
            .stream()
            .map(Customer::getBranch)
            .filter(Optional<Branch>::isPresent)
            .map(Optional::get)
            .collect(
                  HashSet::new,        // supplier
                  HashSet::add,        // accumulator
                  HashSet::addAll      // combiner
            );

      assert 3
      == branches2.size() : "the number of branches #2";

      assert
      branches1.equals(branches2) : "equality of branches #1 to #2";

      Set<Branch> branches3
      = customers.values()
            .stream()
            .map(Customer::getBranch)
            .filter(Optional<Branch>::isPresent)
            .map(Optional::get)
            .collect(Collectors.toSet());

      assert 3
      == branches3.size() : "the number of branches #3";

      assert
      branches1.equals(branches3) : "equality of branches #1 to #3";

      Set<Branch> branches4
      = customers.values()
            .stream()
            .map(Customer::getBranch)
            .filter(Optional<Branch>::isPresent)
            .map(Optional::get)
            .collect(toSet());

      assert 3
      == branches4.size() : "the number of branches #4";

      Map<Optional<Branch>, Set<Customer>> mapBranchToCustomer
      = customers.values()
            .stream()
            .collect(groupingBy(
                  Customer::getBranch,
                  mapping(Function.identity(), toSet())
            ));

      mapBranchToCustomer
            .forEach((branch, set) -> {
               System.out.println(branch.isPresent() ? branch.get() : "(***)");
               set.forEach(customer -> System.out.printf("   %s%n", customer));
            });

   }

   public static void main(String[] args) {
      testBranch();
      testCustomer();
      introCollect();
   }

}
