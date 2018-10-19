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
            stmt.executeUpdate("CREATE TABLE customer (upper INTEGER, lower INTEGER, bcode INTEGER)");

            final String SQL = "INSERT INTO customer VALUES(?, ?, ?)";
            PreparedStatement insert = conn.prepareStatement(SQL);

            // INSERT INTO customer VALUES(59082, 90388, 301)
            insert.setInt(1, 59082);
            insert.setInt(2, 90388);
            insert.setInt(3, 301);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(40478, 81098, 704)
            insert.setInt(1, 40478);
            insert.setInt(2, 81098);
            insert.setInt(3, 704);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(42219, 82253, 704)
            insert.setInt(1, 42219);
            insert.setInt(2, 82253);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(90944, 27526, 701)
            insert.setInt(1, 90944);
            insert.setInt(2, 27526);
            insert.setInt(3, 701);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(13700, 46678, 701)
            insert.setInt(1, 13700);
            insert.setInt(2, 46678);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(99155, 51171, 701)
            insert.setInt(1, 99155);
            insert.setInt(2, 51171);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(18827, 78489, NULL)
            insert.setInt(1, 18827);
            insert.setInt(2, 78489);
            insert.setNull(3, java.sql.Types.INTEGER);
            insert.executeUpdate();

            // INSERT INTO customer VALUES(63855, 36217, NULL)
            insert.setInt(1, 63855);
            insert.setInt(2, 36217);
            insert.setNull(3, java.sql.Types.INTEGER);
            insert.executeUpdate();

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
         Statement select = conn.createStatement();
         ResultSet rs = select.executeQuery("SELECT * FROM customer");
         while(rs.next()) {
            int upper = rs.getInt(1);
            int lower = rs.getInt(2);

            Optional.ofNullable(rs.getString(3))
               .ifPresentOrElse(
                  strBcode -> {
                     System.out.printf("%d-%d: %s%n", upper, lower, strBcode);
                  }, () -> {
                     System.out.printf("%d-%d: ***%n", upper, lower);
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
