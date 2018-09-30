package banking.branch.v3;

public interface Branch extends Comparable<Branch> {

   int getCode();      // relevant to Branch.equals()
   String getName();
   String getPostalcode();

   static final int SELFSERVICE_BRANCH  = 0;
   static final int MIN_CODE_INCL = 1;
   static final int MAX_CODE_EXCL = 1_000;

   static boolean isValidBranch(int b) {
      return MIN_CODE_INCL <= b && b < MAX_CODE_EXCL;
   }

   static Branch empty() {
      return new Branch() {
         @Override public int getCode() { return SELFSERVICE_BRANCH; }
         @Override public String getName() { return "Self-service"; }
         @Override public String getPostalcode() { return "(n/a)"; }
         @Override public int compareTo(Branch o) { return -1; }  // it comes before any real branch
      };
   }

   static final String CODE_FORMAT = "%03d";
   static final String INFO_FORMAT = CODE_FORMAT + ": %s: %s";  // code, name, postal code

}
