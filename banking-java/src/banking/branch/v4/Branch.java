package banking.branch.v4;

public interface Branch extends Comparable<Branch> {

   int getBranchCode();      // relevant to Branch.equals()
   String getBranchName();
   String getPostalCode();

   static int getBranchCodeMinIncl()   { return      1; }
   static int getBranchCodeMaxExcl()   { return  1_000; }
   static String getBranchCodeFormat() { return "%03d"; }

}
