# *T1.1*　JDBC接続によるCRUD操作・全検索 ～ データベースに接続してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

---
### 今回のチュートリアル対象

- コミット：[2251346](https://github.com/612-teacher001/jbasic-dao-demo/commit/2251346)
- クラス：[`jp.example.app.t1.JdbcAll`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcAll.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

---

## 1. 概要

このチュートリアルで扱うプログラムは、`products` テーブルのすべてのレコードを取得し、標準出力に表示するというものです。

  - **取得順序**：主キー（`id` フィールド）の昇順
  - **出力形式**：商品ID、カテゴリID、商品名、価格、数量
  - **例外処理**：SQL実行中にエラーが発生した場合にはスタックトレースを表示

このチュートリアルでは、**データベース接続**、**SQL実行**、**結果のリスト変換**、**表示** までの手順を解説します。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcAll#main(String[])` メソッドのコードに記述されている「手順-X」と記述されたコメントに対応しています。  
実際にコードを確認しながら読み進めてください。

### 手順-1：SQLを設定する

```java
String sql = "SELECT * FROM products ORDER BY id";
```
- 「全件を取得し主キーの昇順に並べ替える」という操作を実行するSQL文です。
- `ORDER BY`句を使って、常に同じ順序で取得できるようにします。
---

### 手順-2：データベース接続オブジェクトを取得する
``` java
DbConfigure configure = new DbConfigure();
String url = configure.getUrl();
String user = configure.getUser();
String password =  configure.getPassword();
Connection conn = DriverManager.getConnection(url, user, password);
```
- `DbConfigure` オブジェクトで接続情報を取得しています。  
`DbConfigure` オブジェクトについては [DbConfigure クラス使用ガイド](../../manuals/provided/dbconfigure.md) を参照してください。

- `DriverManager.getConnection` メソッドによってデータベース接続オブジェクト（`Connection` オブジェクト）が生成されます。  
このデータベース接続オブジェクトがSQL実行の出発点になります。

- このオブジェクトを取得することで、実際にデータベースに接続した状態になります。

---

### 手順-3：SQL実行オブジェクトを取得する
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


👉 **SQLインジェクション**：ユーザー入力をそのままSQL文に組み込んでしまうことで意図しないSQLを実行できてしまうという脆弱性を利用しソフトウェアの脅威

このように安全性や再利用性の観点から、単純な SELECT であっても PreparedStatement を使うのが一般的です。  
したがって、このチュートリアルでも PreparedStatement を利用して説明していきます。

---

### 手順-4：SQLを実行して結果セットを取得する<a id="execute"></a>
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
結果セット内で参照できるレコードの位置情報を示すしくみが備わっています。このチュートリアルではそのしくみを **レコードポインタ** といいます。 
このレコードポインタを次のレコードに移動するには `ResultSet#next()` メソッドを使います。  
`ResultSet#next()` メソッドは、次のレコードがある場合には `true`、それ以外は `false` を返します。  
また結果セットを取得直後の初期状態では、このポインタは先頭レコードの前の位置を指しています。

---

### 手順-5：結果セットから商品リストに変換する
```java
List<Product> list = new ArrayList<>();
while (rs.next()) {
    int id = rs.getInt("id");
    int categoryId = rs.getInt("category_id");
    String name = rs.getString("name");
    int price = rs.getInt("price");
    int quantity = rs.getInt("quantity");

    Product bean = new Product(id, categoryId, name, price, quantity);
    list.add(bean);
}
```
- `rs.next()` によるレコード移動ですべてのレコードを走査します。
- 各フィールド値を一時変数に格納し、それらの変数を使って新しい商品インスタンスを生成しています。  
これは「**インスタンス化した段階で初期値が設定された完全なインスタンスの状態を作る**」という考え方に基づいています。

---

### 手順-6：商品リストを表示する
```java
System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n",
                  "商品ID", "カテゴリID", "商品名", "価格", "数量");
for (Product bean : list) {
    System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
                      bean.getId(),
                      bean.getCategoryId(),
                      bean.getName(),
                      bean.getPrice(),
                      bean.getQuantity());
}
```
- 画面表示には `System.out.printf` メソッドを使って整形して表示しています。
- `System.out.printf` メソッドについては [`System.out.printf()` メソッドの使い方](../../appendix/printf.md) を参照してください。

---

## 4. 例外処理について

例外処理については、例外発生の原因確認が簡単に行えればよいので、このチュートリアルでは学習目的のために統一してスタックトレースを表示するだけにしています。  
実務では、ログ出力と絡めてスタックトレース表示だけでは不十分なことが多いので、別途共通した例外処理を実装することがあります。

スタックトレースには以下のような基本情報が含まれています：

| 項目        | 内容                           |
| ----------- | ----------------------------- |
| 例外の種類   | どのような例外が発生したか      |
| 発生した場所 | どのファイルの何行目で発生したか |

---

## 5. まとめ

ここのコードで学ぶべきポイント：

  - SQLの実行手順（接続 → 実行 → 結果取得）
  - `ResultSet` からオブジェクトへの変換方法
  - コンストラクタ処理化による安全なオブジェクトの生成
  - 標準出力への整形表示
  - 例外処理の基本的で簡易な実装

---

## 課題

以下の課題を進めるにあたっては `jp.example.exercise.t1` パッケージの中にクラスを作成してください。  
なお、クラス名は `Exercise11` のように「Exercise + 課題番号」の形式にして `main` メソッドに処理を記述してください。  
たとえば、課題-11の場合は「`Exercise11`」というクラスを作成します。

**［課題-11］** `products` テーブルのすべての商品の商品IDと商品名と価格を取得するSQLを作成して、そのSQLを使ってレコードを取得・表示するプログラムを作ってください。

**［課題-12］** [データベース概要](../00-database.md) を参考にして `employees` テーブルの全件を取得するSQL文を作成し、そのSQLを利用して全件を表示するプログラムを作ってください。

**［課題-13］** 身近なテーマで新しいテーブルを設計し、いくつかレコードを登録してください。その後、全件を取得するSQL文を作成し、それを利用して全レコードを表示するプログラムを作ってください。テーブルの作成とレコードの登録はpsqlから手作業で操作しましょう。  

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
