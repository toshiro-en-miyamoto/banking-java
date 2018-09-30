package banking.branch.v3;

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
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import banking.util.csv.CSVParser;

public final class BranchCSVLoader implements BranchDataLoader {
   private static final Path CSVPATH;
   private static final String CHARSET;
   private static CSVParser parser;

   private static Pattern DIGIT_ONLY = Pattern.compile("^\\d+$");

   @Override
   public Map<Integer, Branch> loadBranches() {
      Map<Integer, Branch> map = new HashMap<>();
      try {
         map = Files
            .lines(CSVPATH, Charset.forName(CHARSET))
            .map(parser::parse)
            .filter(fields -> fields.size() >= 3)
            .filter(fields -> {
               Matcher matcher = DIGIT_ONLY.matcher(fields.get(0).trim());
               if (matcher.find())
                  return true;
               else {
                  System.err.printf("Non-integer (%s) found in field[0] of Branch CSV file%n", fields.get(0));
                  return false;
               }
            })
            .map(fields -> {
               return PrototypeBranch.of(
                  Integer.parseInt(fields.get(0).trim()),
                  fields.get(1).trim(),
                  fields.get(2).trim()
               );
            })
            .collect(Collectors.toMap(Branch::getCode, Function.identity()));
      } catch(IOException e) {
         System.err.println("Branch CSV file not found");
      }
      return map;
   }

   static {
      CSVPATH = Paths.get(BranchSettings.get("csv.filename"));
      CHARSET = BranchSettings.get("csv.encoding");

      String parserName = BranchSettings.get("csv.parser");
      try {
         Class<?> parserClass = Class.forName(parserName);
         Constructor<?> constructor = parserClass.getConstructor();
         parser = (CSVParser) constructor.newInstance();
      } catch (Exception e) {
         System.err.println("CSV Parser not found");
         parser = new CSVParser() {
            @Override public List<String> parse(String line) {
               return Collections.emptyList();
            }
         };
      }
   }

}
