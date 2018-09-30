## Stream&lt;T>.collect() #1


     <R> R collect(Supplier<R> supplier,
                   BiConsumer<R,? super T> accumulator,
                   BiConsumer<R,R> combiner)


**Type parameters:**

* T - type of the stream elements
* R - type of the result

**Method parameters:**

* supplier - a function that creates a new result container R. For a parallel execution, this function may be called multiple times and must return a fresh value each time.
* accumulator - an associative, non-interfering, stateless function for incorporating an additional element T into a result R
* combiner - an associative, non-interfering, stateless function for combining two values ((R)a + (R)b = (R)c), which must be compatible with the accumulator function


The internal logic:

     R result = supplier.get();
     for (T element : this stream)
         accumulator.accept(result, element);
     return result;

A sample code:

     List<String> asList
        = stringStream.collect(ArrayList::new,
                               ArrayList::add,
                               ArrayList::addAll);

## Stream.collect() #2

Performs a mutable reduction operation on the elements of this stream using a Collector.

     <R,A> R collect(Collector<? super T,A,R> collector);

Collectors classes make the above code much simpler as:

     List<String> asList
        = stringStream.collect(Collectors.toList());


**Type parameters:**

* T - the type of the stream elements
* A - the intermediate accumulation type of the Collector
* R - type of the result


## Collector

     public interface Collector<T,A,R> {
        Supplier<A> supplier();
        BiConsumer<A,T> accumulator();
        BinaryOperator<A> combiner();
        ...
     }

**Type parameters:**

* T - the type of input elements to the reduction operation
* A - the mutable accumulation type of the reduction operation
* R - the result type of the reduction operation

**Method parameters:**

* supplier - A function that creates and returns a new mutable result container.
* accumulator - A function that folds a value into a mutable result container.
* combiner - A function that accepts two partial results and merges them.

**Note:**
Compare the Collectors' methods with the parameters of Stream&lt;T>.collect() #1.


## Collectors (1/4) - basic

### toSet()

Returns a Collector that accumulates the input elements into a new Set.

     static <T>
        Collector<T,?,Set<T>>
        toSet();

There are no guarantees on the type, mutability, serializability, or thread-safety of the Set returned; if more control over the returned Set is required, use toCollection(Supplier).

### toList()

Returns a Collector that accumulates the input elements into a new List.

     static <T>
        Collector<T,?,List<T>>
        toList();

There are no guarantees on the type, mutability, serializability, or thread-safety of the List returned; if more control over the returned List is required, use toCollection(Supplier).

### toCollection()

Returns a Collector that accumulates the input elements into a new Collection, in encounter order.

     static <T,C extends Collection<T>>
        Collector<T,?,C>
        toCollection(Supplier<C> collectionFactory);

### toMap() - basic

Returns a Collector that accumulates elements into a Map whose keys and values are the result of applying the provided mapping functions to the input elements.

     static <T,K,U>
        Collector<T,?,Map<K,U>>
        toMap(Function<? super T,? extends K> keyMapper,
              Function<? super T,? extends U> valueMapper);

It is common for either the key or the value to be the input elements. In this case, the utility method Function.identity() may be helpful. For example, the following produces a Map mapping students to their grade point average: 

     Map<Student, Double> studentToGPA
        = students.stream().collect(toMap(Function.identity(),
                                          student -> computeGPA(student)));
 
And the following produces a Map mapping a unique identifier to students: 

     Map<String, Student> studentIdToStudent
        = students.stream().collect(toMap(Student::getId,
                                          Function.identity());

### toMap() - with mergeFunction

Returns a Collector that accumulates elements into a Map whose keys and values are the result of applying the provided mapping functions to the input elements.

     static <T,K,U>
        Collector<T,?,Map<K,U>>
        toMap(Function<? super T,? extends K> keyMapper,
              Function<? super T,? extends U> valueMapper,
              BinaryOperator<U> mergeFunction);

There are multiple ways to deal with collisions between multiple elements mapping to the same key. The other forms of toMap simply use a merge function that throws unconditionally, but you can easily write more flexible merge policies. For example, if you have a stream of Person, and you want to produce a "phone book" mapping name to address, but it is possible that two persons have the same name, you can do as follows to gracefully deals with these collisions, and produce a Map mapping names to a concatenated list of addresses: 

     Map<String, String> phoneBook
        = people.stream().collect(toMap(Person::getName,
                                        Person::getAddress,
                                        (s, a) -> s + ", " + a));

### toMap() - with mergeFunction & mapSupplier

Returns a Collector that accumulates elements into a Map whose keys and values are the result of applying the provided mapping functions to the input elements.

     static <T,K,U,M extends Map<K,U>>
        Collector<T,?,M>
        toMap(Function<? super T,? extends K> keyMapper,
              Function<? super T,? extends U> valueMapper,
              BinaryOperator<U> mergeFunction,
              Supplier<M> mapSupplier);

### groupingBy() - basic

Returns a Collector implementing a "group by" operation on input elements of type T, grouping elements according to a classification function, and returning the results in a Map.

     static <T,K>
        Collector<T,?,Map<K,List<T>>>
        groupingBy(Function<? super T,? extends K> classifier);

This produces a result similar to: 

     groupingBy(classifier, toList());

### groupingBy - with downstream Collector

Returns a Collector implementing a cascaded "group by" operation on input elements of type T, grouping elements according to a classification function, and then performing a reduction operation on the values associated with a given key using the specified downstream Collector.

     static <T,K,A,D>
        Collector<T,?,Map<K,D>>
        groupingBy(Function<? super T,? extends K> classifier,
                   Collector<? super T,A,D> downstream);

Usage sample:

     Map<City, Set<String>> namesByCity
        = people.stream().collect(groupingBy(Person::getCity,
                                             mapping(Person::getLastName, toSet())));
 
### groupingBy - with mapFactory & downstream Collector

Returns a Collector implementing a cascaded "group by" operation on input elements of type T, grouping elements according to a classification function, and then performing a reduction operation on the values associated with a given key using the specified downstream Collector.

     static <T,K,D,A,M extends Map<K,D>>
        Collector<T,?,M>
        groupingBy(Function<? super T,? extends K> classifier,
                   Supplier<M> mapFactory,
                   Collector<? super T,A,D> downstream);

Usage sample:

     Map<City, Set<String>> namesByCity
        = people.stream().collect(groupingBy(Person::getCity, TreeMap::new,
                                             mapping(Person::getLastName, toSet()))); 

### mapping()

Adapts a Collector accepting elements of type U to one accepting elements of type T by applying a mapping function to each input element before accumulation.

     static <T,U,A,R>
        Collector<T,?,R>
        mapping(Function<? super T,? extends U> mapper,
                Collector<? super U,A,R> downstream);

The mapping() collectors are most useful when used in a multi-level reduction, such as downstream of a groupingBy or partitioningBy. For example, given a stream of Person, to accumulate the set of last names in each city:

     Map<City, Set<String>> lastNamesByCity
        = people.stream().collect(groupingBy(Person::getCity,
                                             mapping(Person::getLastName, toSet())));

## Collectors (2/4) - reduction

### counting()

Returns a Collector accepting elements of type T that counts the number of input elements.

     static <T>
        Collector<T,?,Long>
        counting();

### summingDouble()

Returns a Collector that produces the sum of a double-valued function applied to the input elements.

     static <T>
        Collector<T,?,Double>
        summingDouble(ToDoubleFunction<? super T> mapper);

### averagingDouble()

Returns a Collector that produces the arithmetic mean of a double-valued function applied to the input elements.

     static <T>
        Collector<T,?,Double>
        averagingDouble(ToDoubleFunction<? super T> mapper);

### joining() - basic

Returns a Collector that concatenates the input elements into a String, in encounter order.


     static
        Collector<CharSequence,?,String>
        joining();


### joining() -with delimiter

Returns a Collector that concatenates the input elements, separated by the specified delimiter, in encounter order.


     static
        Collector<CharSequence,?,String>
        joining(CharSequence delimiter);

### joining() - with delimiter & prefix/suffix

Returns a Collector that concatenates the input elements, separated by the specified delimiter, with the specified prefix and suffix, in encounter order.


     static
        Collector<CharSequence,?,String>
        joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix);

### maxBy()

Returns a Collector that produces the maximal element according to a given Comparator, described as an Optional<T>.

     static <T>
        Collector<T,?,Optional<T>>
        maxBy(Comparator<? super T> comparator);

### minBy()

Returns a Collector that produces the minimal element according to a given Comparator, described as an Optional<T>.

     static <T>
        Collector<T,?,Optional<T>>
        minBy(Comparator<? super T> comparator);

### summarizingDouble()

Returns a Collector which applies an double-producing mapping function to each input element, and returns summary statistics for the resulting values.

     static <T>
        Collector<T,?,DoubleSummaryStatistics>
        summarizingDouble(ToDoubleFunction<? super T> mapper);


## Collectors (3/4) - cocurrent versions

### toConcurrentMap() - basic

Returns a concurrent Collector that accumulates elements into a ConcurrentMap whose keys and values are the result of applying the provided mapping functions to the input elements.

     static <T,K,U>
        Collector<T,?,ConcurrentMap<K,U>>
        toConcurrentMap(Function<? super T,? extends K> keyMapper,
                        Function<? super T,? extends U> valueMapper);

### toConcurrentMap() - with mergeFunction

Returns a concurrent Collector that accumulates elements into a ConcurrentMap whose keys and values are the result of applying the provided mapping functions to the input elements.

     static <T,K,U>
        Collector<T,?,ConcurrentMap<K,U>>
        toConcurrentMap(Function<? super T,? extends K> keyMapper,
                        Function<? super T,? extends U> valueMapper,
                        BinaryOperator<U> mergeFunction);

### toConcurrentMap() - with mergeFunction & mapSupplier

Returns a concurrent Collector that accumulates elements into a ConcurrentMap whose keys and values are the result of applying the provided mapping functions to the input elements.

     static <T,K,U,M extends ConcurrentMap<K,U>>
        Collector<T,?,M>
        toConcurrentMap(Function<? super T,? extends K> keyMapper,
                        Function<? super T,? extends U> valueMapper,
                        BinaryOperator<U> mergeFunction,
                        Supplier<M> mapSupplier);

### groupingByConcurrent - basic

Returns a concurrent Collector implementing a "group by" operation on input elements of type T, grouping elements according to a classification function.

     static <T,K>
        Collector<T,?,ConcurrentMap<K,List<T>>>
        groupingByConcurrent(Function<? super T,? extends K> classifier);

### groupingByConcurrent() - with downstream Collector

Returns a concurrent Collector implementing a cascaded "group by" operation on input elements of type T, grouping elements according to a classification function, and then performing a reduction operation on the values associated with a given key using the specified downstream Collector.

     static <T,K,A,D>
        Collector<T,?,ConcurrentMap<K,D>>
        groupingByConcurrent(Function<? super T,? extends K> classifier,
                             Collector<? super T,A,D> downstream);

### groupingByConcurrent() - with mapFactory & downstream Collector

Returns a concurrent Collector implementing a cascaded "group by" operation on input elements of type T, grouping elements according to a classification function, and then performing a reduction operation on the values associated with a given key using the specified downstream Collector.

     static <T,K,A,D,M extends ConcurrentMap<K,D>>
        Collector<T,?,M>
        groupingByConcurrent(Function<? super T,? extends K> classifier,
                             Supplier<M> mapFactory,
                             Collector<? super T,A,D> downstream);

## Collectors (4/4)

### collectingAndThan()

Adapts a Collector to perform an additional finishing transformation.

     static <T,A,R,RR>
        Collector<T,A,RR>
        collectingAndThen(Collector<T,A,R> downstream,
                          Function<R,RR> finisher);

**Usage sample:**

     List<String> people
         = people.stream().collect(collectingAndThen(toList(), Collections::unmodifiableList));
 

### reducing() - basic

Returns a Collector which performs a reduction of its input elements under a specified BinaryOperator.

     static <T>
        Collector<T,?,Optional<T>>
        reducing(BinaryOperator<T> op);

**Usage sample:**

     Comparator<Person> byHeight = Comparator.comparing(Person::getHeight);
     Map<City, Person> tallestByCity
         = people.stream().collect(groupingBy(Person::getCity,
                                                 reducing(BinaryOperator.maxBy(byHeight))));
 
### reducing() - with identity

Returns a Collector which performs a reduction of its input elements under a specified BinaryOperator using the provided identity.

     static <T>
        Collector<T,?,T>
        reducing(T identity,
                 BinaryOperator<T> op);
 
### reducing() - with identity & mapper

Returns a Collector which performs a reduction of its input elements under a specified mapping function and BinaryOperator.

     static <T,U>
        Collector<T,?,U>
        reducing(U identity,
                 Function<? super T,? extends U> mapper,
                 BinaryOperator<U> op);

**Usage sample:**

     Comparator<String> byLength = Comparator.comparing(String::length);
     Map<City, String> longestLastNameByCity
        = people.stream().collect(groupingBy(Person::getCity,
                                             reducing(Person::getLastName,
                                             BinaryOperator.maxBy(byLength))));

### partitioningBy() - basic

Returns a Collector which partitions the input elements according to a Predicate, and organizes them into a Map<Boolean, List<T>>.

     static <T>
        Collector<T,?,Map<Boolean,List<T>>>
        partitioningBy(Predicate<? super T> predicate);

### partitioningBy() - with downstream Collector

Returns a Collector which partitions the input elements according to a Predicate, reduces the values in each partition according to another Collector, and organizes them into a Map<Boolean, D> whose values are the result of the downstream reduction.

     static <T,D,A>
        Collector<T,?,Map<Boolean,D>>
        partitioningBy(Predicate<? super T> predicate,
                       Collector<? super T,A,D> downstream);


 (End)
 