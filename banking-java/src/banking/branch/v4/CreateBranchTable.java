package banking.branch.v4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

final class CreateBranchTable {
   // Download JDBC driver from 'https://bitbucket.org/xerial/sqlite-jdbc/downloads/'.
   // The latest driver as of 2018-10-15 is sqlite-jdbc-3.23.1.jar (2018-05-29, 6.4MB)
   // With Eclipse, right click on a Java project, select Build Path → Add External Archives...,
   // then the driver jar file will appear in Referenced Libraries of the project.

   // Running this class will create the file banking.db in the working directory (user.dir).

   public static void main(String[] args) {
      try (
            Connection conn = DriverManager.getConnection("jdbc:sqlite:banking.db");
            Statement stmt = conn.createStatement();
            )
      {
         stmt.executeUpdate("DROP TABLE IF EXISTS branch");
         stmt.executeUpdate("CREATE TABLE branch (bcode INTEGER, name STRING, pcode STRING)");
         stmt.executeUpdate("INSERT INTO branch VALUES(301,'札幌支店','060-0001')");
         stmt.executeUpdate("INSERT INTO branch VALUES(704,'北九州支店','802-0006')");
         stmt.executeUpdate("INSERT INTO branch VALUES(701,'福岡支店','812-0011')");

         ResultSet rs = stmt.executeQuery("SELECT * FROM branch");
         while(rs.next()) {
            int bcode = rs.getInt("bcode");
            String name = rs.getString("name");
            String pcode = rs.getString("pcode");
            System.out.printf("%d: %s, %s%n", bcode,  name, pcode);
         }
      } catch (SQLException e) {
         System.out.printf("%s (%s:%d)%n", e.getMessage(), e.getSQLState(), e.getErrorCode());
      }
   }

}
