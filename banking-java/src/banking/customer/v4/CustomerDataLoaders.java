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
                  Statement stmt = conn.createStatement();
                  )
            {
               // "CREATE TABLE customer (cnumhigh INTEGER, cnumlow INTEGER, bcode INTEGER)"
               ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
               while(rs.next()) {
                  int high = rs.getInt(1);
                  int low  = rs.getInt(2);
                  CustomerNumber n = CustomerNumber.of(high, low);

                  Optional<String> sBcode = Optional.ofNullable(rs.getString(3));
                  Optional<Branch> b = sBcode.map(s -> {
                     try {
                        int bcode = Integer.parseInt(s);
                        return mapBranches.get(bcode);
                     } catch(NumberFormatException e) {
                        return null;
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
