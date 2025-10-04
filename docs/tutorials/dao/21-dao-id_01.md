# *T2.1*　DAOによるCRUD操作・検索 ～ 商品IDで検索してみる・基本編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[7ee19e5](https://github.com/612-teacher001/jbasic-dao-demo/commit/7ee19e5)
- クラス：[`jp.example.app.t2.DaoId`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoId.java)  
　　　[`jp.example.app.dao.ProductDAO`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/dao/ProductDAO.java)

---

## 1. 概要

このチュートリアルでは、**DAOを使った主キー検索の方法** について解説します。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

---

## 3. 手順ごとの解説

DAOを利用した手順はCRUD操作に関係なくほぼ同じになります。

### 手順-1. ProductDAOをインスタンス化する
```java
try (// 手順-1. ProductDAOをインスタンス化
     ProductDAO dao = new ProductDAO();) {
  ...（中略）...
} catch (SQLException e) {
  ...（中略）...
}
```
- `ProductDAO`をインスタンス化します。
`ProductDAO` は `AutoCloseable` インタフェースを実装しているので、リソースが自動破棄されます。

### 手順-2. すべての商品の商品リストを取得して表示する
```java
// 手順-2. 全件表示
List<Product> list = dao.findAll();
Utils.showProductList(list);
```
- `ProductDAO#findAll()` メソッドを呼び出して全件検索を実行し、結果として商品リストが戻されます。  
これは商品の参考として表示します。
- `Utils#showProductList(List<Product>)` メソッドで商品リストを表示します。

### 手順-3. キーボードから入力された検索する商品のIDを取得する 
```java
// 手順-3. キーボードから入力された検索する商品のIDを取得
System.out.println();
int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
System.out.println();
```
- `Keyboard#getInputNumber(String)` メソッドを呼び出して検索する商品のIDを取得します。
- このメソッドの前後の `System.out.println()` は空行を表示して表示上のバランスを取っています。

### 手順-4. 入力された商品IDから商品を取得する
```java
// 手順-4. 入力された商品IDから商品を取得
Product product = dao.findById(targetId);
```
- `ProductDAO#findById(int)` メソッドを呼び出して主キー検索を実行しています。  
実行結果は、商品が見つかった場合は商品インスタンス、商品が見つからなかった場合は `null` が返されます。  

### 手順-5. 取得した商品を存在チェックする
```java
// 手順-5. 取得した商品の存在チェック
if (product == null) {
  Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
  return;
}
```
- 商品が見つからなかった場合の実行結果は `null` なので、メッセージを表示して、`return` でプログラムを終了します。  

### 手順-6. 取得した商品を表示する 
```java
// 手順-6. 取得した商品を表示
Utils.showProduct(product);
```
- 検索結果は商品インスタンスになるので、`Utils#showProduct(Product)` メソッドを呼び出して表示しています。

---

## 4. ProductDAOの追加メソッド

### 4.1. `ProductDAO#findById(int)` メソッド
`ProductDAO`クラスの `findById(int)` メソッドを再掲します。  
詳細は [`jp.example.app.dao.ProductDAO`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/dao/ProductDAO.java) を参照してください。

```java
public class ProductDAO extends BaseDAO {
  ...（中略）...
  private static final String SQL_FIND_BY_ID = "SELECT * FROM products WHERE id = ?";
  ...（中略）...
  public Product findById(int targetId) throws SQLException {
    // 1. 戻り値の初期化
    Product product = null;
    try (// 2. SQL実行オブジェクトを取得
        PreparedStatement  pstmt = this.conn.prepareStatement(SQL_FIND_BY_ID);) {
      // 3. プレースホルダをパラメータで置換
      pstmt.setInt(1, targetId);
      
      try (// 4. SQLの実行と結果セットの取得
          ResultSet rs = pstmt.executeQuery();) {
        // 5. 結果セットを商品インスタンスに変換
        product = convertToProduct(rs);
      }
    }
    // 6. 戻り値の返却
    return product;
  }
}
```
- 主キー検索を実行するメソッドです。
テーブル定義を参照すればproductsテーブルの主キーは `id` なので、引数に指定された商品IDで `id` フィールドを検索します。
- 主キー検索なので、引数に指定された商品IDに合致した商品が見つかった場合は商品インスタンス、見つからなかった場合は `null` になります。
- 実行するSQLは文字列定数として定義したので、SQLを格納する変数を宣言する必要はありません。
- 補助メソッドとして 'convertToProduct(ResultSet)' メソッドを利用します。  
'convertToList(ResultSet)' と 'convertToProduct(ResultSet)' の違いは以下のとおりです：
  | 補助メソッド | 呼び出し元メソッド | 目的 |
  | ----------- | ---------------- | ---- |
  | `convertToList()`    | `findAll()`  | 複数件の検索結果をまとめてリスト化   |
  | `convertToProduct()` | `findById()` | 単一件の検索結果を商品インスタンスに変換 |

## 5. まとめ

ここのコードで学ぶべきポイント：

  - ProductDAOはproductsテーブルのCRUD操作を実行する責務を担う
  - ProductDAOをインスタンス化すると、同時にデータベース接続オブジェクトを取得するのでデータベースに接続した状態になる
  - 主キー検索を実行する場合は `ProductDAO#findById(int)` メソッドを呼び出すだけで結果の商品インスタンスを取得できる
  - 検索結果が存在しない場合は `null` かどうかで判断する

## 6. 問題点
この `ProductDAO` のままでは以下のような問題があります：

  - `convertToList(ResultSet)` メソッド、`convertToProduct(ResultSet)` メソッド内で商品インスタンスを生成する処理が重複している  
  　　⇒ 【解決策】結果セットから商品インスタンスを生成する新たな補助メソッドを定義し利用する

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
