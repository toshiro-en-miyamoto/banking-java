package banking.customer.v3;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;

final class CustomerDataLoaders {
   private static CustomerDataLoader loader = null;
   private static final String DATA_LOADER_NOT_FOUND = "Customer Data Loader not found";

   // default properties
   private static final String
         KEY_DATA_LOADER = "data.loader",  VAL_DATA_LOADER = "banking.customer.v3.CustomerCSVLoader";

   static CustomerDataLoader getLoader() {
      if (loader == null) {
         String loaderName = CustomerSettings.get(KEY_DATA_LOADER, VAL_DATA_LOADER);
         try {
            Class<?> loaderClass = Class.forName(loaderName);
            Constructor<?> constructor = loaderClass.getConstructor();
            loader = (CustomerDataLoader) constructor.newInstance();
         } catch (Exception e) {
            System.err.println(DATA_LOADER_NOT_FOUND);
            loader = new CustomerDataLoader() {
               @Override public Map<CustomerNumber, Customer> loadCustomers() {
                  return Collections.emptyMap();
               }
            };
         }
      }
      return loader;
   }

}
