package banking.customer.v3;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import banking.branch.v3.Branch;
import banking.branch.v3.Branches;
import banking.util.csv.CSVParser;

public final class CustomerCSVLoader implements CustomerDataLoader {
   private static final Path CSVPATH;
   private static final String CHARSET;
   private static CSVParser parser;

   private static final String CSV_PARSER_NOT_FOUND = "CSV Parser not found";
   private static final String CUSTOMER_CSV_NOT_FOUND = "Customer CSV file not found";
   private static final String CUSTOMER_NUM1_NOT_INTEGER =
         "Non-integer (%s) found in field[0] of Customer CSV file%n";
   private static final String CUSTOMER_NUM2_NOT_INTEGER =
         "Non-integer (%s) found in field[1] of Customer CSV file%n";

   // default properties
   private static final String
         KEY_CSV_DIR     = "csv.dir",      VAL_CSV_DIR     = "data/customer",
         KEY_CSV_FILE    = "csv.filename", VAL_CSV_FILE    = "customer.csv",
         KEY_CSV_CHARSET = "csv.charset",  VAL_CSV_CHARSET = "UTF-8",
         KEY_CSV_PARSER  = "csv.parser",   VAL_CSV_PARSER  = "banking.util.csv.ExcelCSVParser";

   private static Pattern DIGIT_ONLY = Pattern.compile("^\\d+$");

   @Override
   public Map<CustomerNumber, Customer> loadCustomers() {
      Map<Integer, Branch> mapBranches = Branches.getMapBranchCodeToBranch();
      Map<CustomerNumber, Customer> map = new HashMap<>();
      try {
         map = Files.lines(CSVPATH, Charset.forName(CHARSET))
            .map(parser::parse)
            .filter(fields -> fields.size() >= 3)
            .filter(fields -> {
               Matcher matcher1 = DIGIT_ONLY.matcher(fields.get(0).trim());
               if (!matcher1.find()) {
                  System.err.printf(CUSTOMER_NUM1_NOT_INTEGER, fields.get(0).trim());
                  return false;
               }
               Matcher matcher2 = DIGIT_ONLY.matcher(fields.get(1).trim());
               if (!matcher2.find()) {
                  System.err.printf(CUSTOMER_NUM2_NOT_INTEGER, fields.get(1).trim());
                  return false;
               }
               return true;
            })
            .map(fields -> {
               int customerNum1 = Integer.parseInt(fields.get(0).trim());
               int customerNum2 = Integer.parseInt(fields.get(1).trim());
               CustomerNumber n = CustomerNumber.of(customerNum1, customerNum2);

               Optional<Branch> b;
               try {
                  int branchCode = Integer.parseInt(fields.get(2).trim());
                  b = Optional.ofNullable(mapBranches.get(branchCode));
               } catch (NumberFormatException e) {
                  b = Optional.empty();
               }

               return PrototypeCustomer.of(n, b);
            })
            .collect(Collectors.toMap(Customer::getCustomerNumber, Function.identity()));
      } catch(IOException e) {
         System.err.println(CUSTOMER_CSV_NOT_FOUND);
      }
      return map;
   }


   static {
      String csvDir  = CustomerSettings.get(KEY_CSV_DIR,  VAL_CSV_DIR);
      String csvFile = CustomerSettings.get(KEY_CSV_FILE, VAL_CSV_FILE);
      CSVPATH = Paths.get(csvDir, csvFile);
      CHARSET = CustomerSettings.get(KEY_CSV_CHARSET, VAL_CSV_CHARSET);

      String parserName = CustomerSettings.get(KEY_CSV_PARSER, VAL_CSV_PARSER);
      try {
         Class<?> parserClass = Class.forName(parserName);
         Constructor<?> constructor = parserClass.getConstructor();
         parser = (CSVParser) constructor.newInstance();
      } catch (Exception e) {
         System.err.println(CSV_PARSER_NOT_FOUND);
         parser = new CSVParser() {
            @Override public List<String> parse(String line) {
               return Collections.emptyList();
            }
         };
      }
   }

}
