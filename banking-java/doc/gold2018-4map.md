### put

    V put(K key, V value)

Associates the specified value with the specified key in this map (optional operation).
If the map previously contained a mapping for the key, the old value is replaced by the specified value.
(A map m is said to contain a mapping for a key k if and only if m.containsKey(k) would return true.)

Parameters:
* key - key with which the specified value is to be associated
* value - value to be associated with the specified key

Returns:
the previous value associated with key, or null if there was no mapping for key.
(A null return can also indicate that the map previously associated null with key,
if the implementation supports null values.)

Note:

    put(k, v);
       before     after       return
       (k, a   )  (k, v   )   a
       (k, null)  (k, v   )   null
       ---------  (k, v   )   null

### putIfAbsent

    V putIfAbsent(K key, V value)

If the specified key is not already associated with a value (or is mapped to null)
 associates it with the given value and returns null,
else
 returns the current value.

Implementation Requirements:
The default implementation is equivalent to, for this map:

    V v = map.get(key);
    if (v == null)
       v = map.put(key, value);
    
    return v;
 
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.

Parameters:
* key - key with which the specified value is to be associated
* value - value to be associated with the specified key

Returns:
the previous value associated with the specified key, or null if there was no mapping for the key.
(A null return can also indicate that the map previously associated null with the key,
if the implementation supports null values.)

Note:

    putIfAbsent(k, v);
       before     after       return
       (k, a   )  (k, a   )   a
       (k, null)  (k, v   )   null
       ---------  (k, v   )   null

### replace

    V replace(K key, V value)

Replaces the entry for the specified key only if it is currently mapped to some value.

Implementation Requirements:
The default implementation is equivalent to, for this map:

    if (map.containsKey(key)) {
       return map.put(key, value);
    } else
       return null;
 
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.

Parameters:
* key - key with which the specified value is associated
* value - value to be associated with the specified key

Returns:
the previous value associated with the specified key, or null if there was no mapping for the key.
(A null return can also indicate that the map previously associated null with the key, if the implementation supports null values.)

Note:

    replace(k, v);
       before     after       return
       (k, a   )  (k, v   )   a
       (k, null)  (k, v   )   null
       ---------  ---------   null

### replace [with old value]

    boolean replace(K key, V oldValue, V newValue)

Replaces the entry for the specified key only if currently mapped to the specified value.

Implementation Requirements:
The default implementation is equivalent to, for this map:
 
    if (map.containsKey(key) && Objects.equals(map.get(key), value)) {
       map.put(key, newValue);
       return true;
    } else
       return false;
 
The default implementation does not throw NullPointerException for maps that do not support null values
if oldValue is null unless newValue is also null.
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.

Parameters:
* key - key with which the specified value is associated
* oldValue - value expected to be associated with the specified key
* newValue - value to be associated with the specified key

Returns:
true if the value was replaced

Note:

    replace(k, a, v);
       before     after       return
       (k, a   )  (k, v   )   true
       (k, null)  (k, null)   false
       ---------  ---------   false
    
    replace(k, b, v);
       before     after       return
       (k, a   )  (k, a   )   false
       (k, null)  (k, null)   false
       ---------  ---------   false

### compute

    V compute(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)

Attempts to compute a mapping for the specified key and its current mapped value (or null if there is no current mapping).
For example, to either create or append a String msg to a value mapping:

    map.compute(key, (k, v) -> (v == null) ? msg : v.concat(msg))

(Method merge() is often simpler to use for such purposes.)

If the function returns null, the mapping is removed (or remains absent if initially absent).
If the function itself throws an (unchecked) exception, the exception is re-thrown, and the current mapping is left unchanged.

Implementation Requirements:
The default implementation is equivalent to performing the following steps for this map,
then returning the current value or null if absent:

    V oldValue = map.get(key);
    V newValue = remappingFunction.apply(key, oldValue);
    if (oldValue != null ) {
       if (newValue != null)
          map.put(key, newValue);
       else
          map.remove(key);
    } else {
       if (newValue != null)
          map.put(key, newValue);
       else
          return null;
    }
 
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.
In particular, all implementations of sub-interface ConcurrentMap must document whether the function is applied once atomically only if the value is not present.

Parameters:
* key - key with which the specified value is to be associated
* remappingFunction - the function to compute a value

Returns:
the new value associated with the specified key, or null if none

Note:

    compute(k, (k, a) -> v);
    compute(k, (k, null) -> v);
       before     after       return
       (k, a   )  (k, v   )   v
       (k, null)  (k, v   )   v
       ---------  (k, v   )   v
    
    compute(k, (k, a) -> null);
    compute(k, (k, null) -> null);
       before     after       return
       (k, a   )  ---------   null
       (k, null)  ---------   null
       ---------  ---------   null


### computeIfAbsent

    V computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)

If the specified key is not already associated with a value (or is mapped to null),
attempts to compute its value using the given mapping function and enters it into this map unless null.

If the function returns null no mapping is recorded. If the function itself throws an (unchecked) exception,
the exception is re-thrown, and no mapping is recorded.

The most common usage is to construct a new object serving as an initial mapped value or memoized result, as in:

    map.computeIfAbsent(key, k -> new Value(f(k)));
 
Or to implement a multi-value map, Map<K,Collection<V>>, supporting multiple values per key:
 
    map.computeIfAbsent(key, k -> new HashSet<V>().add(v));
 
Implementation Requirements:
The default implementation is equivalent to the following steps for this map, then returning the current value or null if now absent:

    if (map.get(key) == null) {
       V newValue = mappingFunction.apply(key);
       if (newValue != null)
          map.put(key, newValue);
    }
 
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.
In particular, all implementations of sub-interface ConcurrentMap must document whether the function is applied once atomically only if the value is not present.

Parameters:
* key - key with which the specified value is to be associated
* mappingFunction - the function to compute a value

Returns:
the current (existing or computed) value associated with the specified key, or null if the computed value is null

Note:

    computeIfAbsent(k, (k) -> v);
       before     after       return
       (k, a   )  (k, a   )   a
       (k, null)  (k, v   )   v
       ---------  (k, v   )   v
    
    computeIfAbsent(k, (k) -> null);
       before     after       return
       (k, a   )  (k, a   )   a
       (k, null)  (k, null)   null
       ---------  ---------   null

### computeIfPresent

    V computeIfPresent(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)

If the value for the specified key is present and non-null, attempts to compute a new mapping given the key and its current mapped value.
If the function returns null, the mapping is removed. If the function itself throws an (unchecked) exception, the exception is re-thrown,
and the current mapping is left unchanged.

Implementation Requirements:
The default implementation is equivalent to performing the following steps for this map, then returning the current value or null if now absent:

    if (map.get(key) != null) {
       V oldValue = map.get(key);
       V newValue = remappingFunction.apply(key, oldValue);
       if (newValue != null)
          map.put(key, newValue);
       else
          map.remove(key);
    }
 
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.
In particular, all implementations of sub-interface ConcurrentMap must document whether the function is applied once atomically only if the value is not present.

Parameters:
* key - key with which the specified value is to be associated
* remappingFunction - the function to compute a value

Returns:
the new value associated with the specified key, or null if none

Note:

    computeIfPresent(k, (k, a) -> v);
       before     after       return
       (k, a   )  (k, v   )   v
    
    computeIfPresent(k, (k, a) -> null);
       before     after       return
       (k, a   )  ---------   a
    
    computeIfPresent(k, (k, null) -> v);
    computeIfPresent(k, (k, null) -> null);
       before     after       return
       (k, null)  (k, null)   null
       ---------  ---------   null

### merge

    V merge(K key, V value, BiFunction<? super V,? super V,? extends V> remappingFunction)

If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value.
Otherwise, replaces the associated value with the results of the given remapping function, or removes if the result is null.
This method may be of use when combining multiple mapped values for a key. For example, to either create or append a String msg to a value mapping:
 
    map.merge(key, msg, String::concat)
 
If the function returns null the mapping is removed. If the function itself throws an (unchecked) exception, the exception is re-thrown,
and the current mapping is left unchanged.

Implementation Requirements:
The default implementation is equivalent to performing the following steps for this map, then returning the current value or null if absent:

    V oldValue = map.get(key);
    V newValue = (oldValue == null) ? value :
                 remappingFunction.apply(oldValue, value);
    if (newValue == null)
       map.remove(key);
    else
       map.put(key, newValue);
 
The default implementation makes no guarantees about synchronization or atomicity properties of this method.
Any implementation providing atomicity guarantees must override this method and document its concurrency properties.
In particular, all implementations of sub-interface ConcurrentMap must document whether the function is applied once atomically only if the value is not present.

Parameters:
* key - key with which the resulting value is to be associated
* value - the non-null value to be merged with the existing value associated with the key or,
if no existing value or a null value is associated with the key, to be associated with the key
* remappingFunction - the function to recompute a value if present

Returns:
the new value associated with the specified key, or null if no value is associated with the key

    merge(k, v, (a, v) -> r);
       before     after       return
       (k, a   )  (k, r   )   r
    
    merge(k, v, (a, v) -> null);
       before     after       return
       (k, a   )  ---------   null
    
    merge(k, v, ...);
       before     after       return
       (k, null)  (k, v   )   v
       ---------  (k, v   )   v

(end)
