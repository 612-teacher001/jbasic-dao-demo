# *T1.13* JDBC接続によるCRUD操作・新規登録 ～ 商品を登録してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### 今回のチュートリアル対象

- コミット：[1b25816](https://github.com/612-teacher001/jbasic-dao-demo/commit/1b25816)
- クラス：[`jp.example.app.t1.JdbcInsert`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcInsert.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

## 1. 概要

このチュートリアルで扱うプログラムは、productsテーブルにレコードを新規登録して、標準出力に全件表示するというものです。

このチュートリアルでは、**処理をメソッドとして別定義して再利用する方法** について解説します。

このプログラムでは、商品リストの表示処理や
ResultSet から Product のリストを生成する処理を
共通メソッドとして切り出しています。  
これにより、INSERT や SELECT など複数の処理で同じコードを繰り返し書かずに済むようになり、可読性と保守性を高めることができます。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

以下で解説する手順は `jp.example.app.t1.JdbcInsert#main(String[])` メソッドのコードに記述されている「手順-X」と記述されたコメントに対応しています。  
手順-9と手順-11での処理をメソッドとして共通化して再利用できるようにしているので、それ以外の手順についての解説は割愛します。

### 手順-1. 全商品を表示する
- ここはこれまでのチュートリアルで扱ったものと同じです。
<!--
- 主キー検索のチュートリアルでの内容を同じです。[商品IDで検索してみた・基本形編](./12-jdbc-id_01.md) を参照してください。
-->

### 手順-2. 商品を新規登録するメッセージを表示する
```java
Display.showMessageln("【商品登録】");
```
- `Display#showMessageil(String)' メソッドを呼び出して、「【商品登録】」というかんたんなタイトルを表示します。
- `Display` クラスについては [Display クラス 利用ガイド](../../manuals/provided/display.md) を参照してください。

### 手順-3. キーボードから登録する情報を取得する（カテゴリID、商品名、価格、数量）
```java
int categoryId = Keyboard.getInputNumber("カテゴリID：");
String name = Keyboard.getInputString("商品名：");
int price = Keyboard.getInputNumber("価格：");
int quantity = Keyboard.getInputNumber("数量：");
```
- `Keyboard#getInputNumber(String)` および `Keyboard#getInputString(String)` メソッドによって、商品のフィールド値の入力を変数に格納します。
- `Keyboard` クラスについては [Keyboard クラス 利用ガイド](../../manuals/provided/keyboard.md) を参照してください。

### 手順-4. 入力値をもとに登録する商品をインスタンス化する
```java
Product product = new Product(categoryId, name, price, quantity);
```
- 新規登録なのでこの時点では商品IDは未定なので、商品ID以外のフィールドでインスタンス化します。

### 手順-5. 実行するSQLを設定する
```java
String sql = "INSERT INTO products (category_id, name, price, quantity) VALUES (?, ?, ?, ?)";
```
- プレースホルダ付きSQLを設定します。  
このプレースホルダは、実行直前に商品インスタンスのフィールド値で置換します。

### 手順-6. データベース接続情報を取得する
### 手順-7. データベース接続オブジェクトを取得する
### 手順-8. SQL実行オブジェクトを取得する
- ここはこれまでのチュートリアルで扱ったものと同じです。

### 手順-9. SQLのプレースホルダを商品インスタンスのフィールド値で置換する
```java
pstmt.setInt(1, product.getCategoryId());
pstmt.setString(2, product.getName());
pstmt.setInt(3, product.getPrice());
pstmt.setInt(4, product.getQuantity());
```
- `PreparedStatement#setInt` と `PreparedStatement#setString` メソッドを使ってプレースホルダを実行直前に商品インスタンスのフィールド値で置換しています。

- **補足：** JDBCのリファレンスなどでは、この操作を「プレースホルダに値をバインドする」と表現することがあります。本チュートリアルでは「置換」と表記を統一しています。


### 手順-10. SQLを実行する
```java
pstmt.executeUpdate();
```
- INSERT文を実行するので `PreparedStatement#executeUpdate()` メソッドを呼び出します。  
SQL実行オブジェクトのSQL実行メソッド `executeXXXX()` についてはチュートリアルの全検索 [手順-5. SQLを実行して結果セットを取得する](./11-jdbc-all_01.md#rs_execute) を参照してください。

### 手順-11. 商品リストを表示する
- ここはこれまでのチュートリアルで扱ったものと同じです。

## 4. まとめ

ここのコードで学ぶべきポイント：

  - INSRT文の実行には `executeUpdate` メソッドの利用すること
  - プレースホルダに入力値をセットして安全に SQL を組み立てる方法
  - 共通処理をメソッドとして切り出し、コードを簡潔に保つ考え方

---

## 実習問題

以下の課題を進めるにあたっては `jp.example.exercise.t1.insert` パッケージの中にクラスを作成してください。  
なお、クラス名は `Exercise11` のように「Exercise + 課題番号」の形式にして `main` メソッドに処理を記述してください。  
たとえば、課題-11の場合は「`Exercise11`」というクラスを作成します。

**［課題-21］**  [データベース概要](../00-database.md) を参考にして、キーボードから取得した入力値を元にemployeesテーブルに新しく担当者を登録して全件表示するプログラムを作ってください。
このとき、`convertToList` および `showProductList` メソッドを呼び出してコードを簡略化してください。  
また、必要があれば新たなメソッドを定義して利用することも考えてみましょう。

参考：convertToList, showProductList メソッドは [JdbcSelectAll](./11-jdbc-all_01.md) で定義しています

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

