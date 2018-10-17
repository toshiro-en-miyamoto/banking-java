package banking.branch.v4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Branches {
   // Key: Branch code, Value: Branch instance
   private static final Map<Integer, Branch> branches = new HashMap<>();

   public static Map<Integer, Branch> getMapBranchCodeToBranch() {
      return Collections.unmodifiableMap(branches);
   }

   public static Branch copy(Branch branch) {
      return PrototypeBranch.of(branch);
   }

   public static String toBranchCode(Branch branch) {
      return String.format(Branch.getBranchCodeFormat(), branch.getBranchCode());
   }

   public static void main(String...args) {
      Map<Integer, Branch> map = getMapBranchCodeToBranch();
      System.out.printf("The number of branches: %d%n", map.size());
      map.values().stream().forEach(System.out::println);
   }

   static {
      branches.putAll(BranchDataLoaders.getLoader().loadBranches());
   }

}
