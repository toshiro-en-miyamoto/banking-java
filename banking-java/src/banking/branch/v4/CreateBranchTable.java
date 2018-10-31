package banking.branch.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class CreateBranchTable {

   private static void createTable() {
      try (
            Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
            Statement update = conn.createStatement();
         ){
            update.executeUpdate("drop table if exists Branch");
            update.executeUpdate("create table Branch (BranchCode integer, BranchName char(32), PostalCode char(8))");
            update.executeUpdate("insert into Branch (BranchCode, BranchName, PostalCode) values(301,'札幌支店','060-0001')");
            update.executeUpdate("insert into Branch (BranchCode, BranchName, PostalCode) values(704,'北九州支店','802-0006')");
            update.executeUpdate("insert into Branch (BranchCode, BranchName, PostalCode) values(701,'福岡支店','812-0011')");
         } catch (SQLException e) {
            System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
         }
   }

   private static void printTable() {
      try (
            Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
            Statement query = conn.createStatement();
         ){
            ResultSet rs = query.executeQuery("select BranchCode, BranchName, PostalCode from Branch");
            while(rs.next()) {
               int bcode = rs.getInt("BranchCode");
               String name = rs.getString("BranchName");
               String pcode = rs.getString("PostalCode");
               System.out.printf("%d: %s, %s%n", bcode,  name, pcode);
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
