package banking.branch.v3;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;

final class BranchDataLoaders {
   private static BranchDataLoader loader = null;
   private static final String DATA_LOADER_NOT_FOUND = "Branch Data Loader not found";

   // default properties
   private static final String
         KEY_DATA_LOADER = "data.loader",  VAL_DATA_LOADER = "banking.branch.v3.BranchCSVLoader";

   static BranchDataLoader getLoader() {
      if (loader == null) {
         String loaderName = BranchSettings.get(KEY_DATA_LOADER, VAL_DATA_LOADER);
         try {
            Class<?> loaderClass = Class.forName(loaderName);
            Constructor<?> constructor = loaderClass.getConstructor();
            loader = (BranchDataLoader) constructor.newInstance();
         } catch (Exception e) {
            System.err.println(DATA_LOADER_NOT_FOUND);
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
