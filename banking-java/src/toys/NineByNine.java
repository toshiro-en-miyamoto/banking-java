package toys;

import java.util.stream.IntStream;

final class NineByNine {

   public static void main(String[] args) {
      IntStream.range(1, 10)                                 // {1, 2, ..., 9}
      .peek(i -> System.out.println())                       // break line for each i
      .flatMap(i -> IntStream.range(1, 10).map(j -> i * j))  // {i*1, i*2, ..., i*9}
      .forEach(k -> System.out.printf(" %02d", k));
   }

}
