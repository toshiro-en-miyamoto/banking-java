# Collection, Set, Listの比較

インタフェースCollection<E>は(line 1)、Collectionフレームワークの継承関係のルートに位置する。例えば、インタフェースSet<E>やインタフェースList<E>は(line 2-3)、Collection<E>を直接継承するサブタイプである。

     1:  interface Collection<E> extends Comparable<E>{...}
     2:  interface Set<E> extends Collection<E> {...}
     3:  interface List<E> extends Collection<E> {...}


下の表に、この3つのインタフェースが宣言するメソッドを整理した。表中に示したチェックマーク(✓)は、メソッドが宣言されていることを示す。例えば、メソッドget(int index):EはインタフェースListには宣言されているが、CollectionとSetには宣言されていない。それぞれインタフェースがどのメソッドを宣言するかを比較した結果から、以下の特徴が分かる。

* 宣言するメソッドの観点では、CollectionとSetに違いがない。
* Listには、メソッドget(int):EやindexOf(Object):intなど、要素の位置を示すint型の値を扱うメソッドが宣言されている。
* Listには、要素を並び替えるためのメソッドsort(Comparator<? super E> c)が宣言されている。
* Listには、メソッドreplaceAll(UnaryOperator<E>)がある。
* Setは、上記のようなメソッドを宣言していない。

以上の特徴から、SetとListの違いを次のように整理できる。

インタフェースCollection<E>は、
* 継承関係の頂点に位置するので、重複要素排除のような制約を設けない。
* Collectionワレームワークのなかで最も簡素なインタフェースである。

インタフェースSet<E>は、
* 数学の集合論における「集合(Set)」をモデルとしている。
* 数学の「集合」の定義に従い、重複する要素は存在できない、という制約を設けている。
* 数学の「集合」の定義に従い、要素の並び順(ordering)を考えない。
* 要素の並び順を考えないので、「Ｎ番目にある要素」という言葉を持たないし、要素の並び替えもない。
* 以上の特徴を除けば、インタフェースCollection<E>との違いがない。

インタフェースList<E>は、
* 数学の代数学における「列(Sequence)」をモデルとしている。
* 重複する要素が存在しても構わない。
* 「Ｎ番目にある要素」という言葉をもつ。したがって、要素を並び替えることができる。
* 以上の特徴は、Java言語の配列 T[ ]に似ている。
* Java言語の配列 T[ ]のサイズ(.length)は固定だが、List<E>のサイズ(.size())は可変である。
* Java言語の配列 T[ ]の要素はプリミティブ型でも参照型でもよいが、List<E>の要素の型は参照型でなければならない。

下表のヘッダー左端の、CはCollection<E>、SはSet<E>、LはList<E>である。

    C S L Return Method Description
    Collection, Set, List のインスタンスの状態を知る
    ✓ ✓ ✓ boolean isEmpty() Returns true if this collection contains no elements.
    ✓ ✓ ✓ int size()  Returns the number of elements in this collection.
    ✓ ✓ ✓ boolean contains(Object o)  Returns true if this collection contains the specified element.
    ✓ ✓ ✓ boolean containsAll(Collection<?> c)  Returns true if this collection contains all of the elements in the specified collection.
    すべての要素にアクセスする
    ✓ ✓ ✓ default void  forEach(Consumer<? super T> action) Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
    ✓ ✓ ✓ Iterator<E> iterator()  Returns an iterator over the elements in this collection.
    要素を検索する
        ✓ ListIterator<E> listIterator()  Returns a list iterator over the elements in this list (in proper sequence).
        ✓ ListIterator<E> listIterator(int index) Returns a list iterator over the elements in this list (in proper sequence), starting at the specified position in the list.
        ✓ int indexOf(Object o) Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the element.
        ✓ int lastIndexOf(Object o) Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element.
    要素を並び替える
        ✓ default void  sort(Comparator<? super E> c) Sorts this list according to the order induced by the specified Comparator.
    要素を追加する
    ✓ ✓ ✓ boolean add(E e)  Ensures that this collection contains the specified element (optional operation).
        ✓ void  add(int index, E element) Inserts the specified element at the specified position in this list (optional operation).
    ✓ ✓ ✓ boolean addAll(Collection<? extends E> c) Adds all of the elements in the specified collection to this collection (optional operation).
        ✓ boolean addAll(int index, Collection<? extends E> c)  Inserts all of the elements in the specified collection into this list at the specified position (optional operation).
    要素の値を知る
    ✓ ✓ ✓ E Iterator<E>.next()  Returns the next element in the iteration.
        ✓ E get(int index)  Returns the element at the specified position in this list.
    要素の値を書き換える
        ✓ E set(int index, E element) Replaces the element at the specified position in this list with the specified element (optional operation).
        ✓ default void  replaceAll(UnaryOperator<E> operator) Replaces each element of this list with the result of applying the operator to that element.
    要素を削除する
    ✓ ✓ ✓ void  clear() Removes all of the elements from this collection (optional operation).
        ✓ E remove(int index) Removes the element at the specified position in this list (optional operation).
    ✓ ✓ ✓ boolean remove(Object o)  Removes a single instance of the specified element from this collection, if it is present (optional operation).
    ✓ ✓ ✓ boolean removeAll(Collection<?> c)  Removes all of this collection's elements that are also contained in the specified collection (optional operation).
    ✓ ✓ ✓ default boolean removeIf(Predicate<? super E> filter) Removes all of the elements of this collection that satisfy the given predicate.
    ✓ ✓ ✓ boolean retainAll(Collection<?> c)  Retains only the elements in this collection that are contained in the specified collection (optional operation).
    Collection, Set, Listのインスタンスのコピーを作る
    ✓ ✓ ✓ <T> T[] toArray(T[] a)  Returns an array containing all of the elements in this collection.
    ✓ ✓ ✓ Object[]  toArray() Returns an array containing all of the elements in this collection; the runtime type of the returned array is that of the specified array.
        ✓ List<E> subList(int fromIndex, int toIndex) Returns a view of the portion of this list between the specified fromIndex, inclusive, and toIndex, exclusive.

