package banking.customer.v3;

import java.util.Objects;
import java.util.Random;

public final class CustomerNumber implements Comparable<CustomerNumber> {
   // the key composes of two integers, e.g. 12345-67890
   private final int upper;
   private final int lower;

   private static final int UPPER_MIN_INCL =   10000;  // 5 digits at least
   private static final int UPPER_MAX_EXCL = 1_00000;  // 5 digits at most
   private static final int UPPER_DIGITS   = String.valueOf(UPPER_MAX_EXCL - 1).length();
   private static final String UPPER_FORMAT = String.format("%%0%dd", UPPER_DIGITS);

   private static final int LOWER_MIN_INCL =   10000;  // 5 digits at least
   private static final int LOWER_MAX_EXCL = 1_00000;  // 5 digits at most
   private static final int LOWER_DIGITS   = String.valueOf(LOWER_MAX_EXCL - 1).length();
   private static final String LOWER_FORMAT = String.format("%%0%dd", LOWER_DIGITS);

   private static final String CUSTOMER_NUMBER_FORMAT = UPPER_FORMAT + "-" + LOWER_FORMAT;

   // some utility methods
   public static int getUpperMinIncl() { return UPPER_MIN_INCL; }
   public static int getUpperMaxExcl() { return UPPER_MAX_EXCL; }   
   public static int getLowerMinIncl() { return LOWER_MIN_INCL; }
   public static int getLowerMaxExcl() { return LOWER_MAX_EXCL; }
   public static String getCustomerNumberFormat() { return CUSTOMER_NUMBER_FORMAT; }
   
   // the sole, private constructor
   private CustomerNumber(int upper, int lower) {
      if (upper < UPPER_MIN_INCL || UPPER_MAX_EXCL <= upper)
         throw new IllegalArgumentException();
      if (lower < LOWER_MIN_INCL || LOWER_MAX_EXCL <= lower)
         throw new IllegalArgumentException();
      this.upper = upper;
      this.lower = lower;
   }

   // the sole, public factory method
   public static CustomerNumber of(int upper, int lower) {
      return new CustomerNumber(upper, lower);
   }

   // a kind of copy constructor
   public static CustomerNumber of(CustomerNumber number) {
      return new CustomerNumber(number.upper, number.lower);
   }

   // overriding methods inherited from Object and Comparable
   @Override
   public String toString() {
      return String.format(CUSTOMER_NUMBER_FORMAT, upper, lower);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null) return false;
      if (this.getClass() != o.getClass()) return false;
      return upper == ((CustomerNumber) o).upper
            && lower == ((CustomerNumber) o).lower;
   }

   @Override
   public int hashCode() {
      return Objects.hash(upper, lower);
   }

   @Override
   public int compareTo(CustomerNumber c) {
      int u = Integer.compare(upper, c.upper);
      if (u != 0) return u;

      return Integer.compare(lower, c.lower);
   }

   public static void main(String[] args) {
      int origin = CustomerNumber.getUpperMinIncl();
      int bound  = CustomerNumber.getUpperMaxExcl();

      // generate eight customer numbers
      new Random()
            .ints(8, origin, bound)
            .mapToObj(upper -> CustomerNumber.of(upper, new Random().nextInt(bound - origin) + origin))
            .forEach(System.out::println);
   }

}
