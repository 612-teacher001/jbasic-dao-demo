# *T1.13* JDBC接続によるCRUD操作・削除 ～ 商品を削除してみた・基本形編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### 今回のチュートリアル対象

- コミット：[6fe8cab](https://github.com/612-teacher001/jbasic-dao-demo/commit/6fe8cab)
- クラス：[`jp.example.app.t1.JdbcDelete`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcDelete.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

## 1. 概要

このチュートリアルで扱うプログラムは、productsテーブルのレコードを削除して、標準出力に全件表示するというものです。

このチュートリアルでは、**指定したIDの商品を削除する方法** について解説します。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

以下で解説する手順は `jp.example.app.t1.JdbcDelete#main(String[])` メソッドのコードに記述されている「手順-X」と記述されたコメントに対応しています。  

これらはそれぞれデータベースにアクセスする必要があるので、ひとつのデータベース接続オブジェクトを取得してから個々の操作を実行するようにします。

### 手順-1. 全商品を表示する
### 手順-2. 商品削除のメッセージを表示する
### 手順-3. キーボードから削除する商品のIDを取得する
### 手順-4. データベース接続情報を取得する
### 手順-5. データベース接続オブジェクトを取得する
- ここはこれまでのチュートリアルで扱ったものと同じです。

### 手順-6. 削除対象の商品を取得する
```java
Product target = JdbcUpdate.getProductById(conn, targetId);
```
- [商品を更新してみた・共通メソッド化編](./31-jdbc-update_02.md) で定義した `getProductById(Connection, int)` メソッドを呼び出しています。  
このメソッドを呼び出すことで引数に指定した商品IDの商品を取得することができます。

### 手順-7. 削除対象商品の存在チェックをする
- ここはこれまでのチュートリアルで扱ったものと同じです。

### 手順-8. 実行するSQLを設定する
```java
String sql = "DELETE FROM products WHERE id = ?";
```
- 実行するDELETE文を設定します。
WHERE句で削除する対象を限定しないと、テーブル内のすべてのレコードが削除されます。

### 手順-9. SQL実行オブジェクトを取得する
### 手順-10. SQLのプレースホルダを商品IDで置換する
- ここはこれまでのチュートリアルで扱ったものと同じです。

### 手順-11. SQLを実行する
```java
pstmt.executeUpdate();
```
- DELETE文の実行には `PreparedStatement#executeUpdate()` メソッドを呼び出します。

### 手順-12. 商品リストを表示する

## 4. まとめ

ここのコードで学ぶべきポイント：

  - DELETE文の実行には `executeUpdate` メソッドを使用すること
  - DELETE文を実行するときは必ず WHERE 句で削除対象を限定すること


---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

