package banking.branch.v3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Branches {
   private static final Map<Integer, Branch> branches = new HashMap<>();

   public static Map<Integer, Branch> getMapBranchCodeToBranch() {
      return Collections.unmodifiableMap(branches);
   }

   public static void main(String...args) {
      Map<Integer, Branch> map = getMapBranchCodeToBranch();
      System.out.printf("The number of branches: %d%n", map.size());
   }

   static {
      branches.putAll(BranchDataLoaders.getLoader().loadBranches());
   }

}
