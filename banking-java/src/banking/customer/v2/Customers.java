package banking.customer.v2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Customers {
   private static final Map<CustomerNumber, Customer> customers = new HashMap<>();

   public static Map<CustomerNumber, Customer> getMapCustomerNumberToCustomer() {
      return Collections.unmodifiableMap(customers);
   }

   public static Customer copy(Customer customer) {
      return PrototypeCustomer.of(customer);
   }

   public static void main(String[] args) {
      Map<CustomerNumber, Customer> map = getMapCustomerNumberToCustomer();
      System.out.printf("The number of customers: %d%n", map.size());
      map.values().stream().forEach(System.out::println);
   }

   static {
      customers.putAll(CustomerDataLoaders.getLoader().loadCustomers());
   }

}
