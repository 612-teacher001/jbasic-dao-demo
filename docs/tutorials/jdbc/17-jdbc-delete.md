# *T1.7* JDBC接続によるCRUD操作・削除 ～ 商品を削除してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### チュートリアルの対象

  - 対象コミット：[374680f](https://github.com/612-teacher001/jbasic-dao-demo/commit/374680f)
  - 対象クラス：[`jp.example.app.t1.JdbcDelete`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcDelete.java)



## 1. 概要

このチュートリアルでは、レコードの削除をどう実現するかという方法を説明します。

処理の流れは以下のとおりです：

1. 削除対象商品の確認（商品一覧の表示）
2. データベース接続の取得
3. DELETE文の実行
4. 削除結果の確認（商品一覧の再表示）

ただし、過去のチュートリアルで説明しているので商品の再表示ついては解説を省略しています。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcDelete#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-1. キーボードから削除する商品の商品IDを取得する
```java
int targetId = Keyboard.getInputNumber("削除する商品のIDを入力してください：");
```
- `Keyboard#getInputNumber(String)` メソッドを呼び出して、キーボードから削除対象の商品IDを取得します。

### 手順-2. 実行するSQLを設定する
```java
String sql = "DELETE FROM products WHERE id = ?";
```
- プレースホルダ `?` を使って安全に削除対象を指定します。

### 手順-3. データベース接続情報を取得
```java
DbConfigure configure = new DbConfigure();
```
- データベース接続情報を取得して、データベース接続オブジェクトを生成します。

### 手順-4 ～ 6. SQL実行オブジェクトを用いてSQLを実行する
```java
// 手順-4. データベース接続関連オブジェクトを取得
try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
    PreparedStatement pstmt = conn.prepareStatement(sql);
  ) {
  // 手順-5. プレースホルダを商品IDで置換
  pstmt.setInt(1, targetId);
  // 手順-6. SQLを実行
  pstmt.executeUpdate();
} catch (SQLException e) {
  // 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
  e.printStackTrace();
}
```
- DELETE文を実行するために `PreparedStatement#executeUpdate()` メソッドを呼出しています。

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - 削除処理では「SQL 文の準備 → プレースホルダ設定 → 実行」という単純なステップで実現できる

---

## 5. 関連チュートリアルの参照
- 手順-4〜6は前々回のチュートリアルと同じ処理です。
- 詳細は [JDBC接続によるCRUD操作・全検索 ～ try-with-resourcesの導入編](./11-jdbc-all_03.md) を参照してください。

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
