# *T1.6* JDBC接続によるCRUD操作・更新 ～ 商品を更新してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### チュートリアルの対象

  - 対象コミット：[83f5ddf](https://github.com/612-teacher001/jbasic-dao-demo/commit/83f5ddf)
  - 対象クラス：[`jp.example.app.t1.JdbcUpdate`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcUpdate.java)



## 1. 概要

このチュートリアルでは、レコードの更新をどう実現するかという方法を説明します。

処理の流れは以下のとおりです：

1. 更新対象商品の確認（商品一覧の表示）
2. 更新対象商品の選択
3. データベースから対象商品の取得
4. 更新情報の取得
5. データベースに対して更新（UPDATE文の実行）
6. 更新結果の確認（商品一覧の再表示）

ただし、商品一覧の再表示については、、過去のチュートリアルで説明しているので解説では省略しています。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcUpdate#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-1. キーボードから更新する商品の商品IDを取得する
```java
int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
```
- `Keyboard#getInputNumber(String)` メソッドを呼び出して、キーボードから更新対象の商品IDを取得します。

### 手順-2 ～ 3. データベース接続情報の取得とデータベース接続オブジェクトを取得する
```java
DbConfigure configure = new DbConfigure();
try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());) {
  ...
}
```
- データベース接続情報を取得して、データベース接続オブジェクトを生成します。
- この処理では大きく分けて、「更新対象商品の取得」と「商品の更新」という2つの処理を行うため、データベース接続オブジェクトは共通とします。
- 具体的な処理はこのデータベース接続オブジェクトを取得する `try-with-resources` ブロック内に記述することで、ひとつのデータベース接続オブジェクトで
実現しています。

### 手順-4. 更新対象商品を取得する
```java
//   4-1. 実行するSQLを設定
String sql = "SELECT * FROM products WHERE id = ?";
//   4-2. SQL実行オブジェクトを取得
Product target = null;
try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
  //   4-3. プレースホルダを商品IDで置換
  pstmt.setInt(1, targetId);
  //   4-4. SQLの実行と結果セットの取得
  try (ResultSet rs = pstmt.executeQuery();) {
    //   4-5. 結果セットを商品インスタンスに変換
    if (rs.next()) {
      target = new Product(
            rs.getInt("id"),
            rs.getInt("category_id"),
            rs.getString("name"),
            rs.getInt("price"),
            rs.getInt("quantity")
          );
    }
  }
}
```
- この `try-with-resources` ブロックでは更新対象の商品を取得しています。
- 4-5では結果セットの各フィールドの値を直接 `Product`クラスのコンストラクタに渡してインスタンス化しています。  
この記述をすることで、productsテーブルの変更でフィールドが増えても、`Product` クラスのコンストラクタを見直すだけで済むので、修正漏れのリスクを低減できます。

### 手順-5. 取得した商品インスタンスの存在チェック
```java
if (target == null) {
  Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
  return;
}
```
- 手順-4で取得した商品が 「`null` であれば指定されたIDの商品は存在しない」という判定をしています。
- 商品が存在しない場合はプログラムを終了します。

### 手順-6. 商品を更新する
#### 6-1. 更新情報を取得する<a id="step6"></a>
```java
int categoryId = Keyboard.getInputNumber("カテゴリID（現在の値：" + target.getCategoryId() + "）：");
String name = Keyboard.getInputString("商品名（現在の値：" + target.getName() + "）：");
int price = Keyboard.getInputNumber("価格（現在の値：" + target.getPrice() + "）：");
int quantity = Keyboard.getInputNumber("数量（現在の値：" + target.getQuantity() + "）：");
```
- `Keyboard#getInputNumber(String)` メソッドまたは `Keyboard#getInputString(String)` メソッドを呼び出して、キーボードから商品の更新情報を取得します。

#### 6-2. 取得した更新情報で再設定する
```java
target.setCategoryId(categoryId);
target.setName(name);
target.setPrice(price);
target.setQuantity(quantity);
```
- 取得した更新情報を用いて、更新対象商品を書き換えます。

#### 6-3. 実行するSQLを設定する
```java
sql = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";

```
- すべてのフィールドを書き換えるという方針で更新します。

#### 6-4 ～ 6-6. プレースホルダの置換とSQLを実行する
```java
// 6-4. SQL実行オブジェクト取得
try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
  //   6-5. プレースホルダを更新情報で置換
  pstmt.setInt(1, target.getCategoryId());
  pstmt.setString(2, target.getName());
  pstmt.setInt(3, target.getPrice());
  pstmt.setInt(4, target.getQuantity());
  pstmt.setInt(5, target.getId());
  //   6-6. SQLの実行
  pstmt.executeUpdate();
}
```
- UPDATE文を実行するため `PreparedStatement#executeUpdate()` メソッドを呼び出します。  
戻り値は再利用しません。

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - 更新処理では「更新対象商品の取得」と「商品情報の更新」に分けて考える
  - キーボード入力は必要最低限とし、入力値チェックは省略

---

## 5. 関連チュートリアルの参照
- 手順-4〜6は前々回のチュートリアルと同じ処理です。
- 詳細は [JDBC接続によるCRUD操作・全検索 ～ try-with-resourcesの導入編](./11-jdbc-all_03.md) を参照してください。

---

## 6. 補足： 「更新対象商品の取得」と 「実際の更新処理」 の二段階に分けることについて
ここまでで main メソッドを一連の処理として理解できました。
このサンプルでは、更新対象商品の取得と実際の更新処理を一連の main メソッドで行っています。  
実務的には、更新対象を取得する処理と更新情報を反映する処理をメソッドとして分けることが可能です。

このように処理を分けると、次のようなメリットがあります：

- 更新対象取得と更新処理のロジックを独立してテストできる
- 同じ取得処理を他の画面や処理でも再利用しやすい
- チュートリアル後半でメソッド化する際にスムーズに説明できる

初学者は、まず main メソッドの流れを追うだけでも十分理解できます。  
ここまで分けて考えられる必要はなく、処理の全体像を理解することを優先してください。

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
