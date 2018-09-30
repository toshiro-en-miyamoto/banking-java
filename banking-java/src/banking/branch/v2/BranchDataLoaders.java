package banking.branch.v2;

import java.util.HashMap;
import java.util.Map;

final class BranchDataLoaders {
   private static BranchDataLoader loader = null;

   static BranchDataLoader getLoader() {
      loader = new BranchDataLoader() {
         @Override
         public Map<Integer, Branch> loadBranches() {
            Map<Integer, Branch> map = new HashMap<>();
            map.put(301, PrototypeBranch.of(301,"札幌支店","060-0001"));
            map.put(704, PrototypeBranch.of(704,"北九州支店","802-0006"));
            map.put(701, PrototypeBranch.of(701,"福岡支店","812-0011"));
            return map;
         }         
      };
      return loader;
   }

}
