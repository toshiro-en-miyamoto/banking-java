package banking.customer.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class CreateCustomerTable {

   public static void main(String[] args) {
      try (
            Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
            Statement stmt = conn.createStatement();
            )
      {
         stmt.executeUpdate("DROP TABLE IF EXISTS customer");
         stmt.executeUpdate("CREATE TABLE customer (cnumhigh INTEGER, cnumlow INTEGER, bcode INTEGER)");
         stmt.executeUpdate("INSERT INTO customer VALUES(59082, 90388, 301)");
         stmt.executeUpdate("INSERT INTO customer VALUES(40478, 81098, 704)");
         stmt.executeUpdate("INSERT INTO customer VALUES(42219, 82253, 704)");
         stmt.executeUpdate("INSERT INTO customer VALUES(90944, 27526, 701)");
         stmt.executeUpdate("INSERT INTO customer VALUES(13700, 46678, 701)");
         stmt.executeUpdate("INSERT INTO customer VALUES(99155, 51171, 701)");
         stmt.executeUpdate("INSERT INTO customer VALUES(18827, 78489, NULL)");
         stmt.executeUpdate("INSERT INTO customer VALUES(63855, 36217, NULL)");

         ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
         while(rs.next()) {
            int cnumh = rs.getInt("cnumhigh");
            int cnuml = rs.getInt("cnumlow");
            int bcode = rs.getInt("bcode");
            if (bcode == 0) // branch code == NULL
               System.out.printf("%d-%d: ***%n", cnumh, cnuml);
            else
               System.out.printf("%d-%d: %d%n", cnumh, cnuml, bcode);
         }
      } catch (SQLException e) {
         System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
      }
   }

}
