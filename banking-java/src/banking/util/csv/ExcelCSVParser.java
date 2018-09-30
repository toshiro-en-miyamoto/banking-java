package banking.util.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This CSV parser is specialized for Microsoft Excel style CSV file.
 * According to Jeffrey Friedl (1), the values, separated by commas, are
 * either "raw" (just sitting there between the commas, or within double quotes
 * (and within the double quotes, a double quote itself is represented by
 * a pair of double quotes in a row).
 * </p> 
 * (1) "Parsing CSV Files", chapter 5, Mastering Regular Expressions, Third Edition,
 * by Jeffrey E.F. Friedl, O'Reilly, 2006.
 */
public final class ExcelCSVParser implements CSVParser {
   private static final String REGEX =
         // Puts a double quoted field into group(1), an unquoted field into group(2).
         " \\G(?:^|,)                                     \n"+
         " (?:                                            \n"+
         "    # either a double-quoted field...           \n"+
         "    \"  ( [^\"]*+ (?: \"\" [^\"]*+  )*+  ) \"   \n"+
         " |  # ...or...                                  \n"+
         "    # some non-quoted/non-comma text...         \n"+
         "    ( [^\",]*+ )                                \n"+
         " )                                              \n";

   /**
    * @see CSVParser
    */
   @Override
   public List<String> parse(String line) {
      Matcher mMain = Pattern.compile(REGEX, Pattern.COMMENTS).matcher(line);
      Matcher mQuote = Pattern.compile("\"\"").matcher("");
      List<String> result = new ArrayList<>();
      while (mMain.find()) {
         if (mMain.start(2) >= 0)
            // The field is unquoted, so we can use it as is.
            result.add(
                  mMain.group(2)
                  );
         else
            // The field is quoted, so we must replace paired double quotes with one.
            result.add(
                  mQuote.reset(mMain.group(1)).replaceAll("\"")
                  );
      }
      return result;
   }

}
