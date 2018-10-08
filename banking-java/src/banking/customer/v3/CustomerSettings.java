package banking.customer.v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

final class CustomerSettings {
   private static final Properties settings;

   private static final String PROP_DIR  = "data/customer";
   private static final String PROP_FILE = "customer.properties";
   private static final Charset PROP_CHARSET = StandardCharsets.UTF_8;
   private static final String PROP_FILE_NOT_FOUND = "%s not found%n";

   public static String get(String key) {
      return settings.getProperty(key);
   }

   public static String get(String key, String defaultVal) {
      return settings.getProperty(key, defaultVal);
   }

   static {
       Path path = Paths.get(PROP_DIR, PROP_FILE);
       settings = new Properties();

       try (BufferedReader reader = Files.newBufferedReader(path, PROP_CHARSET))
       {
           settings.load(reader);
       } catch (IOException e) {
           System.err.printf(PROP_FILE_NOT_FOUND, PROP_FILE);
       }
   }

}
