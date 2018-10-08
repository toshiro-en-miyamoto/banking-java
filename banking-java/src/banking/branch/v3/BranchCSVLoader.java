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

   private static final String CSV_PARSER_NOT_FOUND = "CSV Parser not found";
   private static final String BRANCH_CSV_NOT_FOUND = "Branch CSV file not found";
   private static final String BRANCH_CODE_NOT_INTEGER =
         "Non-integer (%s) found in field[0] of Branch CSV file%n";

   // default properties
   private static final String
         KEY_CSV_DIR     = "csv.dir",      VAL_CSV_DIR     = "data/branch",
         KEY_CSV_FILE    = "csv.filename", VAL_CSV_FILE    = "branch.csv",
         KEY_CSV_CHARSET = "csv.charset",  VAL_CSV_CHARSET = "UTF-8",
         KEY_CSV_PARSER  = "csv.parser",   VAL_CSV_PARSER  = "banking.util.csv.ExcelCSVParser";

   private static Pattern DIGIT_ONLY = Pattern.compile("^\\d+$");

   @Override
   public Map<Integer, Branch> loadBranches() {
      Map<Integer, Branch> map = new HashMap<>();
      try {
         map = Files.lines(CSVPATH, Charset.forName(CHARSET))
            .map(parser::parse)
            .filter(fields -> fields.size() >= 3)
            .filter(fields -> {
               Matcher matcher = DIGIT_ONLY.matcher(fields.get(0).trim());
               if (matcher.find())
                  return true;
               else {
                  System.err.printf(BRANCH_CODE_NOT_INTEGER, fields.get(0).trim());
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
            .collect(Collectors.toMap(Branch::getBranchCode, Function.identity()));
      } catch(IOException e) {
         System.err.println(BRANCH_CSV_NOT_FOUND);
      }
      return map;
   }

   static {
      String csvDir  = BranchSettings.get(KEY_CSV_DIR,  VAL_CSV_DIR);
      String csvFile = BranchSettings.get(KEY_CSV_FILE, VAL_CSV_FILE);
      CSVPATH = Paths.get(csvDir, csvFile);
      CHARSET = BranchSettings.get(KEY_CSV_CHARSET, VAL_CSV_CHARSET);

      String parserName = BranchSettings.get(KEY_CSV_PARSER, VAL_CSV_PARSER);
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
