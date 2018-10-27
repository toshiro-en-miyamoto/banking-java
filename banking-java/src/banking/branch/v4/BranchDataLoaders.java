package banking.branch.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

final class BranchDataLoaders {
   private static BranchDataLoader loader = null;

   static BranchDataLoader getLoader() {
      loader = new BranchDataLoader() {
         @Override
         public Map<Integer, Branch> loadBranches() {
            Map<Integer, Branch> map = new HashMap<>();

            try (
               Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
            ){
               Statement query = conn.createStatement();
               ResultSet rs = query.executeQuery("select BranchCode, BranchName, PostalCode from Branch");
               while(rs.next()) {
                  int bcode = rs.getInt(1);
                  String name = rs.getString(2);
                  String pcode = rs.getString(3);
                  map.put(bcode, PrototypeBranch.of(bcode, name, pcode));
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
