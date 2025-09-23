# *T1.11* JDBC接続によるCRUD操作・全件検索 ～ データベースに接続してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### 今回のチュートリアル対象

- コミット：[85d0a58](https://github.com/612-teacher001/jbasic-dao-demo/commit/85d0a58)
- クラス：[`jp.example.app.t1.JdbcAll`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcAll.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

## 1. 概要

このチュートリアルで扱うプログラムは、productsテーブルからすべてのレコードを取得して、標準出力に表示するというものです。

このチュートリアルでは、**JDBCの基本の流れ（接続 → 実行 → 結果取得 → 表示）を実現する方法** について解説します。

処理結果の表示仕様は以下のとおりです：
  - **取得順序**：主キー（`id` フィールド）の昇順
  - **出力形式**：商品ID、カテゴリID、商品名、価格、数量
  - **例外処理**：SQL実行中にエラーが発生した場合にはスタックトレースを表示

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

以下で解説する手順は `jp.example.app.t1.JdbcAll#main(String[])` メソッドのコードに記述されている「手順-X」と記述されたコメントに対応しています。  
実際にコードを確認しながら読み進めてください。

### 手順-1. SQLを設定する
```java
String sql = "SELECT * FROM products ORDER BY id";
```
- 「全件を取得し主キーの昇順に並べ替える」という操作を実行するSQL文です。
- `ORDER BY`句を使って、常に同じ順序で取得できるようにします。
- 最近ではすべてのフィールドを指定する場合に、アスタリスク `*` を使うことは避ける傾向があります。  
たとえすべてのフィールドを取得する場合でも、カンマ区切りでフィールド名を書き出すことが推奨されています。

### 手順-2. データベース接続情報を取得する
``` java
DbConfigure configure = new DbConfigure();
```
- `DbConfigure` オブジェクトでデータベース接続情報を取得しています。  
`DbConfigure` オブジェクトについては [DbConfigure クラス使用ガイド](../../manuals/provided/dbconfigure.md) を参照してください。

### 手順-3. データベース接続オブジェクトを取得する
```java
Connection conn 
  = DriverManager.getConnection(
      configure.getUrl(), 
      configure.getUser(), 
      configure.getPassword());

// 参考：configureの値を一時変数に代入して渡す方法
// String url = configure.getUrl(); 
// String username = configure.getUser(); 
// String password = configure.getPassword();
// Connection conn = DriverManager.getConnection(url, username, password);
```
- `DriverManager.getConnection(String, String, String)` メソッドによってデータベース接続オブジェクト（`Connection` オブジェクト）が生成されます。  
このデータベース接続オブジェクトがSQL実行の出発点になります。

- このオブジェクトを取得することで、実際にデータベースに接続した状態になります。

- `DbConfigure` オブジェクトの `getter` メソッドを使ってデータベース接続オブジェクトを取得するコードのほうが行数も少なく、シンプルになります。  
一方で、コメントになっている参考の方法では、引数にどんな値を渡しているかがわかりやすく、またデバッグの際にも変数の確認がしやすいというメリットがあります。  
結果として、どちらのコードも正しく動作します。

### 手順-4. SQL実行オブジェクトを取得する
``` java
PreparedStatement pstmt = conn.prepareStatement(sql);
```
- `PreparedStatement` はSQLを実行するためのオブジェクトです。  
これ以降、`PreparedStatement` オブジェクトのことをSQL実行オブジェクトといいます。  
- SQL実行オブジェクトには、一般に `PreparedStatement` のほかに `Statement` が用意されています。  
 `PreparedStatement` と `Statement` については以下のような違いがあります：

  | 項目       | `Statement`                     | `PreparedStatement`                  |
  | ---------- | ------------------------------  | ------------------------------------ |
  | SQLの書き方 | SQLを文字列として直接記述         | プレースホルダ `?` に後から値を設定 |
  | 実行速度    | 逐次解析されるため低速になりやすい | あらかじめコンパイルされるため高速 |
  | 安全性      | ユーザー入力をそのまま埋め込むためSQLインジェクションの危険あり |SQL文と値を分離して扱うため安全 |
  | 用　途      | 単発処理                        | 繰り返し処理・安全性重視               |


👉 **SQLインジェクション**：ユーザー入力をそのままSQL文に組み込んでしまうことで意図しないSQLを実行できてしまうという脆弱性を利用した攻撃手法

- このように安全性や再利用性の観点から、単純な SELECT であっても PreparedStatement を使うのが一般的です。
したがって、このチュートリアルでも PreparedStatement を利用して説明していきます。  
特に学習段階では「SQLを実行するときは PreparedStatement を使う」と統一して覚えることで、INSERT / UPDATE / DELETE などの処理でも同じパターンで書けるようになり、理解がスムーズになります。

### 手順-5. SQLを実行して結果セットを取得する
```java
ResultSet rs = pstmt.executeQuery();
```
- SQLの実行は、`executeQuery()` と `executeUpdate()` の２つのメソッドがあります：

  | メソッド           | 実行されるSQLの種類                 | 戻り値(型)     | 備考                        |
  | ----------------- | ---------------------------------- | ------------- | --------------------------- |
  | `executeQuery()`  | SELECT文を実行する                  |  `ResultSet`  | テーブルの状態を変更しないSQL |
  | `executeUpdate()` | INSERT/UPDATE/DELETEの各文を実行する | `int`         | テーブルの状態を変更するSQL   |

  `executeUpdate`メソッドについては後述します。  
  なお、このチュートリアルでは `Statement`は使用せず、`PreparedStatement` を使用しています。

- 結果セットはSQLで実行された結果のレコードが格納されています。  
結果セット内で参照できるレコードの位置情報（何行目か）を示すしくみが備わっています。このチュートリアルではそのしくみを **カーソル** または **ポインタ** といいます。 
このカーソルを次のレコード（行）に移動するには `ResultSet#next()` メソッドを使います。  
`ResultSet#next()` メソッドは、次のレコードがある場合には `true`、それ以外は `false` を返します。  
また結果セットを取得直後の初期状態では、このポインタは先頭レコードの前の位置を指しています。

### 手順-6. 結果セットから商品リストに変換する
```java
//   6-1. 商品リストを初期化
List<Product> productList = new ArrayList<Product>();
//   6-2. 結果セットの各行を順に処理
while (rs.next()) {
  // 6-3. 読み出したレコードのすべてのフィールド値を取得
  int id = rs.getInt("id");
  int categoryId = rs.getInt("category_id");
  String name  = rs.getString("name");
  int price = rs.getInt("price");
  int quantity = rs.getInt("quantity");
  // 6-4. 取得したフィールドから商品をインスタンス化
  Product product = new Product(id, categoryId, name, price, quantity);
  // 6-5. 商品インスタンスを商品リストに追加
  productList.add(product);

  // 参考：addメソッドの引数で直接インスタンス化する方法（6-4と6-5を統合する例）
  // productList.add(new Product(id, categoryId, name, price, quantity));
}
```
- `rs.next()` メソッドによるレコード移動ですべてのレコードを走査します。
- 各フィールド値を一時変数に格納し、それらの変数を使って新しい商品インスタンスを生成しています。  
これは「**コンストラクタで必要な値をすべて渡して安全に初期化する**」という考え方に基づいています。  
- コメントにしている参考のコードは、商品クラスのインスタンス化と商品リストへの追加を1行にまとめています。  
6-4と6-5の手順を同時にまとめたコードになっています。  
6-4と6-5に分けても参考のように統合しても、どちらでも構いません。

### 手順-7. 商品リストのすべての要素を表示する
```java
//   7-1. 見出し行の表示
System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
          "商品ID", "カテゴリID", "商品名", "価格", "数量");
//   7-2. すべての要素の表示
for (Product product : productList) {
  // 7-3. 要素の表示
  System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
            product.getId(),
            product.getCategoryId(),
            product.getName(),
            product.getPrice(),
            product.getQuantity()
            );
}
```
- 画面表示には `System.out.printf` メソッドを使って整形して表示しています。
- `System.out.printf` メソッドについては [`System.out.printf()` メソッドの使い方](../../appendix/printf.md) を参照してください。

### 手順-8. データベース接続関連のオブジェクトを解放する
```java
rs.close();
pstmt.close();
conn.close();
```
- 一般にオブジェクトは呼出されてメモリ上に確保されるとどこからでも参照できる状態になります。  
しかし、どこからも参照されなくなると自動的にそのオブジェクトは破棄されます。  
このような仕組みを **ガベージコレクション** といいます。  
- データベース接続関連オブジェクトはインスタンス化して、どこからも参照されない状態になってもメモリから破棄されることはありません。  
JDBC の接続やステートメントは OS のリソース（ソケットやハンドル） を直接使用しているため、GC では自動解放されません。そのため必ず close() を呼び出す必要があります。

### 手順-9. 例外処理を実行する
```java
} catch (SQLException e) {
  // 手順-9. 例外処理の実行：スタックトレースを表示（必要最低限のエラー情報を表示）
  e.printStackTrace();
}
```
- 手順-3から手順-8までのすべての手順でSQL例外 `SQLException` が発生する可能性があります。  
この `SQLException` は **チェック例外** なので、**「必ずtry-catchする必要がある例外」** として捕捉して処理する必要があります。  
ここではスタックトレースを表示するにとどめています。    
詳しくは、以下の **『4. 例外処理について』** を参照してください。


## 4. 例外処理について

例外処理については、例外発生の原因確認が簡単に行えればよいので、このチュートリアルでは学習目的のために統一してスタックトレースを表示するだけにしています。  
実務では、ログ出力と絡めてスタックトレース表示だけでは不十分なことが多いので、別途共通した例外処理を実装することがあります。

スタックトレースには以下のような基本情報が含まれています：

  | 項目        | 内容                           |
  | ----------- | ----------------------------- |
  | 例外の種類   | どのような例外が発生したか      |
  | 発生した場所 | どのファイルの何行目で発生したか |

## 5. まとめ

ここのコードで学ぶべきポイント：

  - SQLの実行手順（接続 → 実行 → 結果取得）
  - `ResultSet` からオブジェクトへの変換方法
  - コンストラクタ処理化による安全なオブジェクトの生成
  - 標準出力への整形表示
  - 例外処理の基本的で簡易な実装

### 残された問題

このチュートリアルでは以下の問題が残されています：
  - try-with-resourcesによるデータベース接続関連オブジェクトの解放

この問題については、次のチュートリアルで取り上げます。

---

## 実習問題

以下の課題を進めるにあたっては `jp.example.exercise.t1.all` パッケージの中にクラスを作成してください。  
なお、クラス名は `Exercise11` のように「Exercise + 課題番号」の形式にして `main` メソッドに処理を記述してください。  
たとえば、課題-11の場合は「`Exercise11`」というクラスを作成します。

**［課題-11］** `products` テーブルのすべての商品の商品IDと商品名と価格を取得するSQLを作成して、そのSQLを使ってレコードを取得・表示するプログラムを作ってください。

**［課題-12］** [データベース概要](../00-database.md) を参考にして `employees` テーブルの全件を取得するSQL文を作成し、そのSQLを利用して全件を表示するプログラムを作ってください。

**［課題-13］** 身近なテーマで新しいテーブルを設計し、いくつかレコードを登録してください。その後、全件を取得するSQL文を作成し、それを利用して全レコードを表示するプログラムを作ってください。テーブルの作成とレコードの登録はpsqlから手作業で操作しましょう。  

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

