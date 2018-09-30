package banking.branch.v3;

final class PrototypeBranch implements Branch {
   private final int code;
   private final String name;
   private final String postalcode;

   @Override public int getCode() { return code; }
   @Override public String getName() { return name; }
   @Override public String getPostalcode() { return postalcode; }

   public static Branch of(int code, String name, String postalcode) {
      return new PrototypeBranch(code, name, postalcode);
   }

   private PrototypeBranch(int code, String name, String postalcode) {
      this.code = code;
      this.name = name;
      this.postalcode = postalcode;
   }

   @Override public String toString() {
      return String.format(Branch.CODE_FORMAT, code);
   }

   @Override public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;
      return code == ((PrototypeBranch) o).code;
   }

   @Override public int hashCode() {
      return Integer.hashCode(code);
   }

   @Override public int compareTo(Branch b) {
      return Integer.compare(code, ((PrototypeBranch) b).code);
   }

}
