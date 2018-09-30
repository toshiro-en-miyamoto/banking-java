package banking.branch.v3;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;

final class BranchDataLoaders {
   private static BranchDataLoader loader = null;

   static BranchDataLoader getLoader() {
      if (loader == null) {
         String loaderName = BranchSettings.get("branch.data.loader");
         try {
            Class<?> loaderClass = Class.forName(loaderName);
            Constructor<?> constructor = loaderClass.getConstructor();
            loader = (BranchDataLoader) constructor.newInstance();
         } catch (Exception e) {
            System.err.println("BranchDataLoader not found");
            loader = new BranchDataLoader() {
               @Override public Map<Integer, Branch> loadBranches() {
                  return Collections.emptyMap();
               }
            };
         }
      }
      return loader;
   }

}
