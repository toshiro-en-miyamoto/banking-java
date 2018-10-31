package banking.customer.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class CreateCustomerTable {

   private static void createTable(Connection conn) throws SQLException {
      try (Statement createTable = conn.createStatement();) {
         createTable.executeUpdate("drop table if exists Customer");
         createTable.executeUpdate("create table Customer (CustomerNo1 integer, CustomerNo2 integer, BranchCode integer)");
      }
   }

   private static void populateTable(Connection conn) throws SQLException {
      final String SQL = "insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (?, ?, ?)";

      conn.setAutoCommit(false);
      try(PreparedStatement insert = conn.prepareStatement(SQL);) {
         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (59082, 90388, 301)
         insert.setInt(1, 59082);
         insert.setInt(2, 90388);
         insert.setInt(3, 301);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (40478, 81098, 704)
         insert.setInt(1, 40478);
         insert.setInt(2, 81098);
         insert.setInt(3, 704);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (42219, 82253, 704)
         insert.setInt(1, 42219);
         insert.setInt(2, 82253);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (90944, 27526, 701)
         insert.setInt(1, 90944);
         insert.setInt(2, 27526);
         insert.setInt(3, 701);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (13700, 46678, 701)
         insert.setInt(1, 13700);
         insert.setInt(2, 46678);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (99155, 51171, 701)
         insert.setInt(1, 99155);
         insert.setInt(2, 51171);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (18827, 78489, null)
         insert.setInt(1, 18827);
         insert.setInt(2, 78489);
         insert.setNull(3, java.sql.Types.INTEGER);
         insert.executeUpdate();

         // insert into Customer (CustomerNo1, CustomerNo2, BranchCode) values (63855, 36217, null)
         insert.setInt(1, 63855);
         insert.setInt(2, 36217);
         insert.setNull(3, java.sql.Types.INTEGER);
         insert.executeUpdate();

         conn.commit();
      } catch (SQLException e) {
         System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
         conn.rollback();
      } finally {
         conn.setAutoCommit(true);
      }
   }

   private static void printTable(Connection conn) throws SQLException {
      try (Statement query = conn.createStatement();) {
         ResultSet rs = query.executeQuery("select CustomerNo1, CustomerNo2, BranchCode from Customer");
         while(rs.next()) {
            int cno1 = rs.getInt(1);
            int cno2 = rs.getInt(2);

            int iBcode = rs.getInt(3);
            if(rs.wasNull())
               System.out.printf("%d-%d: ***%n", cno1, cno2);
            else
               System.out.printf("%d-%d: %s%n", cno1, cno2, iBcode);
         }         
      }
   }

   public static void main(String[] args) {
      try (Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");) {
         createTable(conn);
         populateTable(conn);
         printTable(conn);
      } catch (SQLException e) {
         System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
      }
   }

}
