package banking.customer.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

final class CreateCustomerTable {

   private static void createTable() {
      try (
         Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
      ){
         conn.setAutoCommit(false);

         try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE IF EXISTS customer");
            stmt.executeUpdate("CREATE TABLE customer (numHigh INTEGER, numLow INTEGER, bcode INTEGER)");

            final String SQL = "INSERT INTO customer VALUES(?, ?, ?)";
            PreparedStatement insertCustomer = conn.prepareStatement(SQL);

            // INSERT INTO customer VALUES(59082, 90388, 301)
            insertCustomer.setInt(1, 59082);
            insertCustomer.setInt(2, 90388);
            insertCustomer.setInt(3, 301);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(40478, 81098, 704)
            insertCustomer.setInt(1, 40478);
            insertCustomer.setInt(2, 81098);
            insertCustomer.setInt(3, 704);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(42219, 82253, 704)
            insertCustomer.setInt(1, 42219);
            insertCustomer.setInt(2, 82253);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(90944, 27526, 701)
            insertCustomer.setInt(1, 90944);
            insertCustomer.setInt(2, 27526);
            insertCustomer.setInt(3, 701);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(13700, 46678, 701)
            insertCustomer.setInt(1, 13700);
            insertCustomer.setInt(2, 46678);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(99155, 51171, 701)
            insertCustomer.setInt(1, 99155);
            insertCustomer.setInt(2, 51171);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(18827, 78489, NULL)
            insertCustomer.setInt(1, 18827);
            insertCustomer.setInt(2, 78489);
            insertCustomer.setNull(3, java.sql.Types.INTEGER);
            insertCustomer.executeUpdate();

            // INSERT INTO customer VALUES(63855, 36217, NULL)
            insertCustomer.setInt(1, 63855);
            insertCustomer.setInt(2, 36217);
            insertCustomer.setNull(3, java.sql.Types.INTEGER);
            insertCustomer.executeUpdate();

            conn.commit();
         } catch (SQLException e) {
            conn.rollback();
         } finally {
            conn.setAutoCommit(true);
         }
      } catch (SQLException e) {
         System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
      }
   }

   private static void printTable() {
      try (
         Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
      ){
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM customer");
         while(rs.next()) {
            int high = rs.getInt(1);
            int low  = rs.getInt(2);

            Optional.ofNullable(rs.getString(3))
               .ifPresentOrElse(
                  strBcode -> {
                     System.out.printf("%d-%d: %s%n", high, low, strBcode);
                  }, () -> {
                     System.out.printf("%d-%d: ***%n", high, low);
                  }
            );
         }
      } catch (SQLException e) {
         System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
      }
   }

   public static void main(String[] args) {
      createTable();
      printTable();
   }

}
