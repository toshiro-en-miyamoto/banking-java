package banking.util.csv;

import java.util.List;

/**
 * This interface represents a variety of CSV parsers.
 * It contains a sole method that transforms a CSV line to a List of fields.
 *
 */
public interface CSVParser {
   /**
    * Returns the List of CSV fields.
    * Each element in the List corresponds to the CSV field of given String.
    * Empty fields are represented by empty String ("").
    * @param line - the CSV line to be parsed
    * @return the List containing fields of the CSV line
    */
   List<String> parse(String line);
}
