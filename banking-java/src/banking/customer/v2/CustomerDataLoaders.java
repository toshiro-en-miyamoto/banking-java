package banking.customer.v2;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import banking.branch.v2.Branch;
import banking.branch.v2.Branches;

final class CustomerDataLoaders {
   private static CustomerDataLoader loader = null;

   static CustomerDataLoader getLoader() {
      Map<Integer, Branch> mapBranches = Branches.getMapBranchCodeToBranch();

      loader = new CustomerDataLoader() {
         @Override
         public Map<CustomerNumber, Customer> loadCustomers() {

            Map<CustomerNumber, Customer> map = new HashMap<>();

            CustomerNumber n;
            Optional<Branch> b;

            n = CustomerNumber.of(59082, 90388);
            b = Optional.of(mapBranches.get(301));
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(40478, 81098);
            b = Optional.of(mapBranches.get(704));
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(42219, 82253);
            b = Optional.of(mapBranches.get(704));
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(90944, 27526);
            b = Optional.of(mapBranches.get(701));
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(13700, 46678);
            b = Optional.of(mapBranches.get(701));
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(99155, 51171);
            b = Optional.of(mapBranches.get(701));
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(18827, 78489);
            b = Optional.empty();
            map.put(n, PrototypeCustomer.of(n, b));

            n = CustomerNumber.of(63855, 36217);
            b = Optional.empty();
            map.put(n, PrototypeCustomer.of(n, b));

            return map;
         }
      };
      return loader;
   }
}
