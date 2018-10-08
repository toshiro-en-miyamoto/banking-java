package banking.branch.v3;

final class PrototypeBranch implements Branch {
   private final int branchCode;
   private final String branchName;
   private final String postalCode;

   @Override public int getBranchCode() { return branchCode; }
   @Override public String getBranchName() { return branchName; }
   @Override public String getPostalCode() { return postalCode; }

   static Branch of(int branchCode, String name, String postalCode) {
      return new PrototypeBranch(branchCode, name, postalCode);
   }

   static Branch of(Branch branch) {
      return new PrototypeBranch(
            ((PrototypeBranch) branch).branchCode,
            ((PrototypeBranch) branch).branchName,
            ((PrototypeBranch) branch).postalCode);
   }

   private PrototypeBranch(int branchCode, String name, String postalCode) {
      if (branchCode < Branch.getBranchCodeMinIncl() || Branch.getBranchCodeMaxExcl() < branchCode)
         throw new IllegalArgumentException("Branch code");
      if (name == null)
         throw new IllegalArgumentException("Branch name");
      if (postalCode == null)
         throw new IllegalArgumentException("Branch postalCode");

      this.branchCode = branchCode;
      this.branchName = new String(name);
      this.postalCode = new String(postalCode);
   }

   @Override public String toString() {
      return String.format("(%s) %s %s", Branches.toBranchCode(this), postalCode, branchName);
   }

   @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;
      return branchCode == ((PrototypeBranch) o).branchCode;
   }

   @Override public int hashCode() {
      return Integer.hashCode(branchCode);
   }

   @Override public int compareTo(Branch b) {
      return Integer.compare(branchCode, ((PrototypeBranch) b).branchCode);
   }

}
