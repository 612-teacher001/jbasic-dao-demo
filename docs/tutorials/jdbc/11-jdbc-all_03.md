# *T1.3* JDBC接続によるCRUD操作・全検索 ～ try-with-resourcesの導入編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### チュートリアルの対象

  - 対象コミット：[f8068fc](https://github.com/612-teacher001/jbasic-dao-demo/commit/f8068fc)
  - 対象クラス：[`jp.example.app.t1.JdbcAll`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcAll.java)



## 1. 概要

前回のチュートリアルでは、データベース接続関連オブジェクトのクローズ処理を実装することによってメモリリークの可能性の低減を図りました。  
ただし、このままだとクローズ処理が冗長になり、ソースコードも長くなってしまうという問題がありました。

このチュートリアルでは、データベース接続関連オブジェクトが `AutoClosable` インタフェースの実装クラスであることに注目して、クローズ処理を省略する方法を説明します。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcAll#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-1. SQLを設定する
- 前回までは `try` ブロック内で設定していましたが、今回は `try` ブロックの外部で取得するように変更しています。
- 処理内容は前回と同じです。 

### 手順-2. データベース接続情報を取得する
- 前回までは `try` ブロック内で設定していましたが、今回は `try` ブロックの外部で取得するように変更しています。  
- 処理内容は前回と同じです。

### 手順-3 ～ 手順-5：データベース接続関連オブジェクトを取得する
```java
try (// 手順-3. データベース接続オブジェクトの取得
    Connection conn = DriverManager.getConnection(url, user, pass);
    // 手順-4. SQL実行オブジェクトを取得
    PreparedStatement pstmt = conn.prepareStatement(sql);
    // 手順-5. SQLの実行と結果セットを取得
    ResultSet rs = pstmt.executeQuery();
  ) {
```
- `try` の後ろの `(``)` 内でリソースを宣言すると、ブロック終了時に自動でクローズされます。  
- このような `try` 文を **try-with-resources** といいます。  
本来は意図的にリソースを解放する必要がありますが、`try-with-resources` を使うと自動的にクローズされ、明示的な処理を省略できます。  
これにより、明示的に close() を呼ぶ必要がなくなり、コードが簡潔になります。

---

### 手順-6：結果セットから商品リストに変換する
- 前回の処理と変更はありません。  
前々回は ResultSet から Product インスタンスに変換し、リストに格納して表示する処理を行いました。
---

### 手順-7：商品リストを表示する
- 前回の処理と変更はありません。  
前々回は ResultSet から Product インスタンスに変換し、リストに格納して表示する処理を行いました。
---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - データベース接続関連オブジェクトは意図的に解放する必要があるが、`try-with-resources` という構文を利用するとクローズ処理を明示的に書く必要がなくなる
  - JDBC の `Connection`、`PreparedStatement`、`ResultSet` は `AutoCloseable` を実装しているため、`try-with-resources` によって安全にリソース管理できる

---

## 5. 前々回手順の参照について
- 手順-6〜7は前々回のチュートリアルと同じ処理です。
- 詳細は [JDBC接続によるCRUD操作・全検索 ～ データベースに接続してみた編](./11-jdbc-all_01.md) を参照してください。

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
