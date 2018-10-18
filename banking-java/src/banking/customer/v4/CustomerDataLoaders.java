package banking.customer.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import banking.branch.v4.Branch;
import banking.branch.v4.Branches;

final class CustomerDataLoaders {
   private static CustomerDataLoader loader = null;

   static CustomerDataLoader getLoader() {
      Map<Integer, Branch> mapBranches = Branches.getMapBranchCodeToBranch();

      loader = new CustomerDataLoader() {
         @Override
         public Map<CustomerNumber, Customer> loadCustomers() {
            Map<CustomerNumber, Customer> map = new HashMap<>();

            try ( 
               Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
            ){
               Statement stmt = conn.createStatement();
               ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
               while(rs.next()) {
                  int high = rs.getInt(1);
                  int low  = rs.getInt(2);
                  CustomerNumber n = CustomerNumber.of(high, low);

                  Optional<Branch> b = Optional.ofNullable(rs.getString(3))
                     .flatMap(strBcode -> {
                        try {
                           int intBcode = Integer.parseInt(strBcode);
                           return Optional.of(mapBranches.get(intBcode));
                        } catch(NumberFormatException e) {
                           return Optional.empty();
                        }
                     });

                  map.put(n, PrototypeCustomer.of(n, b));
               }
            } catch (SQLException e) {
               System.err.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
            }

            return map;
         }
      };
      
      return loader;
   }

}
