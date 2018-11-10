# アノテーション

ここでは、アノテーションについて紹介する。Java SE 5で登場して以来、アノテーションを使用したツールが広く利用されるようになり、アノテーションの言語仕様や使用方法を理解することが求められようになった。

アノテーションとは、クラスやメソッドなどの構成要素に注釈を付け加える仕組みである。次のコードは、TimeTravelクラスに@Preliminaryという注釈、すなわちアノテーションを付け加えている。

    @Preliminary public class TimeTravel { ... }

アノテーションを付け加えたからといって、プログラムの挙動が変わるわけではない。むしろ、アノテーションに従って何らかの補助的な働きをするツールがなければ、アノテーションをつける意味はない。アノテーションの記述方法も、そのようなツールが決定するのである。そのいくつかを次に示す。

* JUnit - @Testや@RepeatedTestなどのアノテーションを定義し、コード・テストを効率化できる仕組みを提供する。
* Java EE - @Injectや@SessionScopedなどのアノテーションを定義し、ディペンデンシー・インジェクションの仕組みを提供する。
* Javaコンパイラ - @Overrideや@FunctionalInterfaceなどのアノテーションを定義し、エラー検知の精度を向上している。

アノテーションを用いて記述した注釈情報は、ソース・コード内に存在するのは言うまでもないが、それだけではない。アノテーションの種類によっては、クラス・ファイルやJVMメモリー空間にまでコピーされるものもある。先に紹介したツールの多くは、クラス・ファイルやJVMメモリー空間にコピーされた注釈情報を読み取って、そのツールの機能を実現するのである。


## アノテーション型 (Annotation Types)

**補足:**
まずアノテーション型の定義方法を簡単に紹介するが、アノテーション型を用いて注釈情報を記述する方法を紹介することが狙いである。アノテーション型を定義することが狙いではない。

注釈情報を読み取って何らかの働きをするツールは、独自の**アノテーション型**を定義しなければならない。アノテーション型はインタフェースと似た型であり、Java言語仕様によってその定義方法が定められている。次のコードは、RequestForEnhancementという名前のアノテーション型を定義している(line 7-12)。その際、メソッドを宣言することもできる(line 8-11)。そのメソッドを、アノテーション型の**要素(element)**と呼ぶ。

    RequestForEnhancement.java
     3:  /**
     4:   * Describes the "request-for-enhancement" (RFE) that led to the presence of
     5:   * the annotated API element.
     6:   */
     7:  public @interface RequestForEnhancement {
     8:     int id();           // Unique ID number associated with RFE
     9:     String synopsis();  // Synopsis of RFE
    10:     String engineer();  // Name of engineer who implemented RFE
    11:     String date();      // Date RFE was implemented.
    12:  }

次に示すコードは、Preliminaryという名前のアノテーション型を定義している。要素を持たないアノテーション型を**マーカー・アノテーション型(marker annotation type)**と呼ぶ。

    Preliminary.java
     3:  /**
     4:   * Indicates that the annotated API element is subject to change.
     5:   */
     6:  public @interface Preliminary {
     7:  }

次に示すコードは、Copyrightという名前のアノテーション型を定義している。要素をひとつだけ宣言しているものを**単一要素アノテーション型(single-element annotation type)**と呼ぶ。その要素の名前は**value**とするのが慣例である。

    Copyright.java
     3:  /**
     4:   * Associates a copyright notice with the annotated API element.
     5:   */
     6:  public @interface Copyright {
     7:     String value();
     8:  }

アノテーション型の要素には、既定値を指定することができる(engineer, date)。

    RequestForEnhancementDefault.java
     3:  public @interface RequestForEnhancementDefault {
     4:     int id();
     5:     String synopsis();
     6:     String engineer() default "[unassigned]";
     7:     String date() default "[unimplemented]";
     8:  }

## アノテーションを用いた注釈情報の記述方法

一般的に言えば、アノテーションを用いて注釈情報を記述するには、次の形式に従う。

* クラスやメソッドをアノテーション型で修飾する
* アノテーション型が宣言する要素の値を記述する

次に示すコードは、メソッドtravelThroughTravelを(line 15-16)、アノテーション型RequestForEnhancementで修飾し(line 9-14)、その要素の値を記述している(line 10-13)。

    TimeTravel.java
     9:     @RequestForEnhancement(
    10:           id = 2868724,
    11:           synopsis = "Provide time-travel functionality",
    12:           engineer = "Mr. Peabody",
    13:           date = "4/1/2004"
    14:        )
    15:     public static void travelThroughTime(Date destination) {
    16:     }

既定値をもつ要素は、値の記述を省略しても良い。次に示すコードでは、メソッドbalanceFederalBudgetを(line 22-23)、アノテーション型RequestForEnhancementDefaultで修飾しているが(line 18-21)、値の記述を省略した2つの要素(engineerとdate)は既定値をもつことになる。

    TimeTravel.java
    18:     @RequestForEnhancementDefault(
    19:           id = 4561414,
    20:           synopsis = "Balance the federal budget"
    21:        )
    22:     public static void balanceFederalBudget() {
    23:     }

既定値をもたない要素は、値の記述を省略できない。値を記述しなければコンパイル・エラーとなる。

### マーカー・アノテーション

次に示すコードは、クラスTimeTravelを(line 7-25)、アノテーション型Preliminaryで修飾している(line 6)。Preliminaryは、要素を持たないマーカー・アノテーション型なので、値の記述部分を囲むカッコを省略できる。

    TimeTravel.java
     6:  @Preliminary
     7:  public class TimeTravel {
    (途中省略)
    25:  }

すべての要素が既定値をもっているアノテーション型であれば、上記のコードと同様に、値の記述部分を囲むカッコを省略できる。また、次のコードのように、カッコを残しておいても構わない。

    TimeTravel.java
     6': @Preliminary()
     7:  public class TimeTravel {


### 単一要素アノテーション

次に示すコードは、クラスTimeTravelを(line 7-25)、アノテーション型Copyrightで修飾している(line 5)。Copyrightは、要素をひとつしか持たない単一要素アノテーション型であり、かつその要素の名前が**value**なので、要素名valueを省略し、値だけを記述することが許される。

    TimeTravel.java
     5:  @Copyright("2002 Yoyodyne Propulsion Systems, Inc.")
     6:  @Preliminary
     7:  public class TimeTravel {
    (途中省略)
    25:  }

すなわち、次のコードと同義である。このコードのように、要素名を記述しても構わない。

    TimeTravel.java
     5': @Copyright(value = "2002 Yoyodyne Propulsion Systems, Inc.")

value要素をもち、かつその他の要素が既定値をもつアノテーション型であれば、要素名を省略し、value要素の値だけを記述することが許される。

## Javaコンパイラが定義するアノテーション

Javaコンパイラは、エラー検知の精度を向上するためのアノテーションを定義している。

* @Override
* @Deprecated
* @SuppressWarnings
* @SafeVarargs
* @FunctionalInterface

このうち積極的に使用してほしいアノテーションは、@Overrideと@Deprecatedぐらいであろう。

* @SuppressWarningと@SafeVarargsは、コンパイルの際の警告メッセージを抑制するためのアノテーションであり、アノテーションによって警告を抑制するよりも、警告が出ないコードを書くことが優先されるべきである。
* @FunctionalInterfaceは、関数型インタフェースを定義するときに用いるが、自分で関数型インタフェースを定義する前に、JDKがすでに定義している関数型インタフェースで課題を解決できないかを検討すべきである。

それぞれのアノテーションを以下の節で紹介する。

### @Override

クラスObjectのパブリック・メソッド、equals()をオーバーライドしようとする場面を想像してほしい。

    class Foo1 {
       public boolean equals(Object o) { ... }
    }

次に示すコードは、equals()をオーバーロードしてしまっている。しかし、文法的に誤っているわけではないので、コンパイル・エラーになることはないし、コンパイラが警告してくれることもない。

    class Foo2 {
       public boolean equals(Foo o) { ... }
    }

次に示すコードは、equals()とは異なるメソッドを宣言しているにすぎない。この場合も、文法的に誤っているわけではないので、コンパイル・エラーになることはないし、コンパイラが警告してくれることもない。

    class Foo3 {
       public boolean equal(Object o) { ... }
    }

このようなケアレス・ミスは気づくにくい。スーパー・タイプのメソッドをオーバーライドする際は、@Overrideを用いてその意図をコンパイラに示すことによって、ケアレス・ミスを防ぐことができる。

    class Foo1 {
       @Override
       public boolean equals(Object o) { ... }
    }

ケアレス・ミスがコンパイル・エラーとなるのが@Overrideを用いる利点である。

    class Foo2 {
       @Override
       public boolean equals(Foo o) { ... }  // compile-time error
    }
    
    class Foo3 {
       @Override
       public boolean equal(Object o) { ... }  // compile-time error
    }

### @Deprecated

既存のメソッドを積極的に使用してもらいたくないとき(line 9-10)、その意図をコンパイラに示すアノテーションが@Deprecatedである(line 8)。使用してもらいたくない理由をJava Docのコメントに記しておくことが望ましい(line 4-7)。

     3:  class AnotherClass {
     4:     /**
     5:      * @deprecated
     6:      * betterMethod() is available as a better solution.
     7:      */
     8:     @Deprecated
     9:     static void deprecatedMethod() {
    10:     }
    11:  
    12:     static void betterMethod() {
    13:     }
    14:  }

@Deprecatedとは、推奨しないという意味である。あえて使用してもコンパイル・エラーにはならないが(line 11)、コンパイルした際に警告メッセージが表示されるのが@Deprecatedを用いる利点である。

    PredefinedAnnotation.java
     7:  class PredefinedAnnotation {
    (途中省略)
    10:     private static void callingDeprecatedMethod() {
    11:        AnotherClass.deprecatedMethod();
    12:     }
    
    $ javac -Xlint:deprecation PredefinedAnnotation.java
    PredefinedAnnotation.java:11: warning: [deprecation] deprecatedMethod() in AnotherClass has been deprecated
          AnotherClass.deprecatedMethod();
                      ^
    1 warning


### @SuppressWarnings

非推奨(@Deprecated)であることを承知の上で、それでもそのメソッドを使用せざるを得ないときがある。この場合は、"deprecation"という文字列を@SuppressWarningsに渡すことによって、警告メッセージを表示しないようコンパイラに指示することができる(line 9, JLS8 $9.6.4.6)。

    PredefinedAnnotation.java
     7:  class PredefinedAnnotation {
    (途中省略)
     9:     @SuppressWarnings("deprecation")
    10:     private static void callingDeprecatedMethod() {
    11:        AnotherClass.deprecatedMethod();
    12:     }

ジェネリクス型を用いる際に型パラメータを指定しないと(line 15-17, 21)、ヒープ汚染を引き起こす可能性があるため、コンパイラはunchecked警告を表示することがJava言語仕様によって定められている(JLS8 $4.12.2)。ジェネリクス型をサポートしていない、古いバージョンのコードと共存するときなどは、unchecked警告が表示されることも少なくない。このような場合には、@SuppressWarningsに"unchecked"の文字列を渡して警告メッセージを抑止することができる(line 19, JLS8 $9.6.4.5)。

    PredefinedAnnotation.java
    15:     static List getRawList() {
    16:        return new ArrayList();
    17:     }
    18:  
    19:     @SuppressWarnings("unchecked")
    20:     static void usingRawList() {
    21:        List<String> list = getRawList();
    22:        list.addAll(Collections.<String>emptyList());
    23:     }

SuppressWarningsの唯一の要素valueは、Stringの配列である。

    java.lang.SuppressWarnings
     1:  public @interface SuppressWarnings {
     2:     String[] value();
     3:  }

Stringの配列を記述する際、波括弧で値を囲むのが本来の記述方法である。上記の例のとおり、ひとつの値を記述するときは、波括弧を省略できる。波括弧で囲めば、複数の値を記述できる。

    ??:     @SuppressWarnings({"unchecked", "deprecation"})
    ??:     static void method1() { ... }


### @SafeVarargs

可変個数パラメータをとるメソッドを考えてみよう(line 32-33)。可変個数パラメータの型がジェネリクス型だと(line 32)、そのメソッドの宣言に対してunchecked警告が表示されるだけでなく、そのメソッドを実行する文に対してもunchecked警告が表示される(line 36)。

    PredefinedAnnotation.java
    32:     static void m(List<String>...lists) {
    33:     }
    34:  
    35:     public static void main(String... args) {
    36:        m(Collections.<String>emptyList());
    37:     }
    
    $ javac -Xlint:deprecation PredefinedAnnotation.java
    PredefinedAnnotation.java:32: warning: [unchecked] Possible heap pollution from parameterized vararg type List<String>
       static void m(List<String>...lists) {
                                    ^
    PredefinedAnnotation.java:36: warning: [unchecked] unchecked generic array creation for varargs parameter of type List<String>[]
          m(Collections.<String>emptyList());
           ^
    2 warnings


可変個数パラメータに関わるunchecked警告の表示を抑止するために、@SafeVarargsが用意されている(line 31)。このアノテーションによって、上記の警告は両方とも表示されなくなる。

    PredefinedAnnotation.java
    31:     @SafeVarargs
    32:     static void m(List<String>...lists) {
    33:     }

@SafeVarargsの代わりに@SuppressWarnings("unchecked")を使用すると、メソッドの宣言に対する警告が抑止されるが(line 32)、メソッド
を実行する文に対する警告が相変わらず表示されてしまう(line 36)。

### アノテーション @FunctionalInterface (再掲、Day 1b)

Java SE 8には、関数型インタフェースであることを明示するためのアノテーション、**@FunctionalInterface**が用意されている。

    Bank4'
    11':    @FunctionalInterface
    11:     interface Tester {
    12:        boolean test(Account account);
    13:     }

アノテーション@FunctionalInterfaceを指定するメリットは、宣言されている抽象メソッドの個数をコンパイラが検査してくれることである。誤って2つ以上の抽象メソッドを宣言すると、コンパイル・エラーになる。

    Bank4''
    11':    @FunctionalInterface                // compile-time error
    11:     interface Tester {                  // Tester is not functional
    12:        boolean test(Account account);
    12':       boolean check(Account account);
    13:     }

関数型インタフェースは、スーパー・タイプから継承した抽象メソッドも含めて、宣言されている抽象メソッドは1つだけ、という要件を満たさなければならない。次に示すインタフェースBarは、自身の宣言するメソッドmethodBarと、スーパー・タイプであるインタフェースFooの宣言するメソッドmethodFooと、合計2つの抽象メソッドを宣言しているとみなされる。それゆえ、インタフェースBarにアノテーション@FunctionalInterfaceを適用するとコンパイル・エラーとなる。

     1:  interface Foo {
     2:     void methodFoo();
     3:  }
     4:
     5:  @FunctionalInterface         // compile-time error
     6:  interface Bar extends Foo {  // Foo is not functional
     7:     void methodBar();
     8:  }


(Day 7, 3/4終わり)

リンク:

* Day 7, 1/4 - 「目次」はこちら
* Day 7, 2/4 - 「同値と前後」はこちら
* Day 7, 4/4 - 「問題と解答」はこちら

