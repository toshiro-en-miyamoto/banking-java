# オブジェクトの同値関係と前後関係

Day 2の後半で、コレクション・フレームワークのSetとListの違いを次のように述べた。

インタフェースSet&lt;E>は、
* 数学の集合論における「集合(Set)」をモデルとしている。
* 数学の「集合」の定義に従い、重複する要素は存在できない、という制約を設けている。
* 数学の「集合」の定義に従い、要素の並び順(ordering)を考えない。

インタフェースList&lt;E>は、
* 数学の代数学における「列(Sequence)」をモデルとしている。
* 重複する要素が存在しても構わない。
* 「Ｎ番目にある要素」という言葉をもつ。したがって、要素を並び替えることができる。

重複する要素とは、値の等しい2つのインスタンス、と言い換えることができる。同じ型の2つのインスタンスの値が等しいか否かは、クラスObjectのequals()によって決定される。Setを使う時は、その要素の同値関係を定義すべく、Object.equals()をオーバーライドする必要がある。

要素の並び順を決定するとは、2つのインスタンスの値を比較して、どちらが前でどちらが後かを判定することである。インスタンスの自然な並び順(後述)を定義したければ、インタフェースComparable&lt;T>を実装しなければならない。コレクション・フレームワークは、Comparable&lt;T>のもつ唯一の抽象メソッド、compareTo()の戻り値によってインスンタスの自然な前後関係を判定するからである。

以降の節で、Object.equals()とComparable&lt;T>.compareTo()の実装例を紹介する。

## Object.equals()

Object.equals()の宣言を次に示す(line 2)。このメソッドは、インスタンス(this)と、引数に渡されたObject(other)とが、等しければtrueを、等しくなければfalseを戻す。

    java.lang.Object
     1:  public class Object {
     2:     public boolean equals(Object other) { ... }
    (以下省略)
    ??:  }

何をもってオブジェクトが等しいとするのか、次に示すインタフェースBranchを例に考えてみよう。Branchは、支店コード(Branch code: int)、支店名(Branch name: String)、支店の所在地の郵便番号(Postal code: String)の、3つのフィールドを持っている。

    banking.branch.v4.Branch.java
     3:  public interface Branch extends Comparable<Branch> {
     4:  
     5:     int getBranchCode();
     6:     String getBranchName();
     7:     String getPostalCode();
    (以下省略)
    ??:  }
    
    インスタンスの例:
    (301,"札幌支店","060-0001")
    (701,"福岡支店","812-0011")

もし、Branchを実装するクラスがObject.equals()をオーバーライドしなければ、次に示すインスタンスは、値の異なるインスタンスだとみなされる。クラスObjectのequals()は、すべてのフィールドが一致したときに同値と判断するからである。

    インスタンスの例: Object.equals()をオーバーライドしない場合
    (701,"札幌支店","060-0001")
    (701,"福岡支店","812-0011")

支店名と郵便番号の値にかかわらず、支店コードが同じならば同値だと定義するためには、Branchを実装するクラスが次のようにObject.equals()をオーバーライドしなければならない。

    banking.branch.v4.PrototypeBranch.java
     3:  final class PrototypeBranch implements Branch {
     4:     private final int branchCode;
     5:     private final String branchName;
     6:     private final String postalCode;
    (途中省略)
    40:     @Override public boolean equals(Object o) {
    41:        if (this == o) return true;
    42:        if (o == null) return false;
    43:        if (this.getClass() != o.getClass()) return false;
    44:        return branchCode == ((PrototypeBranch) o).branchCode;
    45:     }
    (以下省略)
    ??:  }

* line 41: 同一のインスタンスならば(this == o)、フィールドの値を見るまでもなく同値である。
* line 42: 引数がnullならばfalseを戻せと定められている。(Java API doc, Object.equals())
* line 43: 引数に渡されたオブジェクトをPrototypeBranchにキャストできるかを検査しなければならない。
* line 44: 3つのフィールドのうち、支店コードだけを比較し、その結果を戻す。

**補足:**
equals()のパラメータの型は常にObjectである。誤って、PrototypeBranchやBranchなど、実装するクラスやインタフェースの型にしてはならない。このようなミスを防止するために@Overrideでequals()を修飾している(line 40)。@Overrideの詳細は「アノテーション」(Day 7, 3/4)にて述べる。


## Object.hashCode()

Object.hashCode()は、インスンタスのハッシュ値を戻すメソッドである。T型のインスタンス、t1とt2があり、t1.equals(t2)がtrueならば、t1.hashCode()とt2.hashCode()も等しいことが要求されている。したがって、equals()をオーバーライドするなら、hashCode()もオーバーライドせざるを得ない。Object.hashCode()の宣言を次に示す(line 3)。

    java.lang.Object
     1:  public class Object {
     2:     public boolean equals(Object other) { ... }
     3:     public int hashCode() { ... }
    (以下省略)
    ??:  }

前節のBranchを例にとれば、支店コードだけをもとに同値関係を判定するようequals()を実装したのだから、その判定に矛盾しないように支店コードだけをもとにハッシュ値を生成しなければならない(line 48)。

    banking.branch.v4.PrototypeBranch.java
     3:  final class PrototypeBranch implements Branch {
     4:     private final int branchCode;
     5:     private final String branchName;
     6:     private final String postalCode;
    (途中省略)
    47:     @Override public int hashCode() {
    48:        return Integer.hashCode(branchCode);
    49:     }
    (以下省略)
    ??:  }

Integer.hashCode()は、引数の数値からハッシュ値を生成するユーティリティ・メソッドである。他のプリミティブ型にもhashCode()が用意されている。

## Comparable&lt;T>.compareTo()

ListやSortedSetなど、要素の並び替え(sort)をサポートするコレクションでは、要素の自然順序(natural ordering)という概念が登場する。自然数を例にとれば、小さい数字が前で大きい数字が後、と考えるのが自然順序である。文字型ならば、Aが前でZが後、と考えるだろう。では、現実の世界を抽象化したオブジェクト、例えば支店の並び順はどのようにして決まるのだろうか。その並び順を決定するのが、インタフェースComparable&lt;T>である。

     java.lang.Comparable
     1:  public interface Comparable<T> {
     2:     int compareTo(T o);
    (以下省略)
    ??:  }

compareTo()は、インスタンスthisと引数oとを比較し、次の値を戻すことにより、型パラメータTのインスタンスの前後関係を決定する。

* thisが前で、oが後ならば、負の値を戻す
* thisとoが等しければ、0(ゼロ)を戻す
* thisが後で、oが前ならば、正の値を戻す

「thisとoが等しければ、0(ゼロ)を戻す」というcompareTo()の振る舞いと、Object.equals()の振る舞いが矛盾してはならない。支店コードだけをもとに同値関係を判定するようequals()を実装したのだから、その判定に矛盾しないように支店コードだけをもとに前後関係を判定しなければならない(line 48)。

    (再掲) banking.branch.v4.Branch.java
     3:  public interface Branch extends Comparable<Branch> {
     4:  
     5:     int getBranchCode();
     6:     String getBranchName();
     7:     String getPostalCode();
    (以下省略)
    ??:  }
    
    banking.branch.v4.PrototypeBranch.java
     3:  final class PrototypeBranch implements Branch {
     4:     private final int branchCode;
     5:     private final String branchName;
     6:     private final String postalCode;
    (途中省略)
    51:     @Override public int compareTo(Branch b) {
    52:        return Integer.compare(branchCode, ((PrototypeBranch) b).branchCode);
    53:     }
    (以下省略)
    ??:  }

Integer.compare()は、compareTo()の戻り値の定義に従う振る舞いを実装したユーティリティ・メソッドである。他のプリミティブ型にもcompare()が用意されている。

## 複数のフィールドをもとに同値と前後を判定する

前節までに紹介したコードは、ひとつのフィールドだけをもとに同値関係と前後関係を判断していた。複数のフィールドから同値と前後を判断するコードも難しくはない。次に示す、CustomerNumberを例に実装方法を考えてみよう。CustomerNumberは、ふたつのフィールド(upper, lower)を持っている(line 8-9)。

Object.equals()の実装は(line 57-63)、そのふたつのフィールドがともに同一であることをもって同値としている(line 61-62)。Object.hashCode()の実装は(line 66-68)、そのふたつのフィールドからハッシュ値を生成している(line 67)。Comparable&lt;T>.compareTo()の実装は(line 71-76)、ふたつのフィールドを比較している。

    banking.customer.v4.CustomerNumber.java
     6:  public final class CustomerNumber implements Comparable<CustomerNumber> {
     7:     // the key composes of two integers, e.g. 12345-67890
     8:     private final int upper;
     9:     private final int lower;
    (途中省略)
    56:     @Override
    57:     public boolean equals(Object o) {
    58:        if (this == o) return true;
    59:        if (o == null) return false;
    60:        if (this.getClass() != o.getClass()) return false;
    61:        return upper == ((CustomerNumber) o).upper
    62:              && lower == ((CustomerNumber) o).lower;
    63:     }
    64:  
    65:     @Override
    66:     public int hashCode() {
    67:        return Objects.hash(upper, lower);
    68:     }
    69:  
    70:     @Override
    71:     public int compareTo(CustomerNumber c) {
    72:        int u = Integer.compare(upper, c.upper);
    73:        if (u != 0) return u;
    74:  
    75:        return Integer.compare(lower, c.lower);
    76:     }

Objects.hash()は、引数からハッシュ値を生成するユーティリティ・メソッドである。

(Day 7, 2/4終わり)

リンク:

* Day 7, 1/4 - 「目次」はこちら
* Day 7, 3/4 - 「アノテーション」はこちら
* Day 7, 4/4 - 「問題と解答」はこちら
