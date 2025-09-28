# *T2.1*　DAOによるCRUD操作・検索 ～ DAOを導入してみる・基本形編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：なし
- クラス：なし

---

## 1. 概要

このチュートリアルでは、**DAOを使った全件検索の方法** について解説します。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

---

## 3. 手順ごとの解説

### 手順-1. ProductDAOをインスタンス化する
```java
try {
  // 手順-1. ProductDAOをインスタンス化
  dao = new ProductDAO();
  ...（中略）...
} catch (SQLException e) {
  ...（中略）...
} finally {
  ...（中略）...
}
```
- `ProductDAO`をインスタンス化します。  
DAOのインスタンス化ではデータベース接続情報を読み込み接続します。
- インスタンス化の際に `SQLException` が発生した場合にはスローされるのでtry-catch文で捕捉します。  
`finally` ブロックではデータベース接続オブジェクトの解放処理を実行します。
- finallyブロックでデータベース接続オブジェクトを解放するために、try-catch-finally文の直前で `ProductDAO` を `null` で初期化しています。
- ProductDAOについては [ProductDAO クラス利用ガイド](./productdao.md) を参照してください。


### 手順-2. すべての商品の商品リストを取得
```java
List<Product> productList = dao.findAll();
```
- `ProductDAO#findAll()` メソッドを呼び出して全件検索を実行し、結果として商品リストが戻されます。

### 手順-3. 取得した商品を表示
```java
Utils.showProductList(productList);
```
- `Utils#showProductList(List<Product>)` メソッドを呼び出して商品リストを表示しています。
- `Utils` クラスについては [Utils クラス利用ガイド](./utils.md) を参照してください。

### 手順-4. ProductDAOを破棄
```java
dao.close();
```
- ProductDAOをクローズします。このメソッドにより、ProductDAOのprivateフィールドであるデータベース接続オブジェクトは破棄されます。  
このチュートリアルではProductDAOは `AutoClosable` インタフェースを実装していないので、try-with-resourcesによる自動破棄はできません。

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - ProductDAOはproductsテーブルのCRUD操作を実行する責務を担う
  - ProductDAOをインスタンス化すると、同時にデータベース接続オブジェクトを取得するのでデータベースに接続した状態になる
  - 全件検索を実行する場合は `ProductDAO#findAll()` メソッドを呼び出すだけで結果の商品リストを取得できる

## 5. 問題点
この `ProductDAO` のままでは以下のような問題があります：

  - try-with-resourcesによるリソースの自動破棄ができない  
  　　⇒ 【解決策】`ProductDAO`に `AutoClosable` インタフェースを実装する
  - 他のテーブルにアクセスするDAOについてもコンストラクタの中でデータベース接続オブジェクトを取得する必要がある  
  　　⇒ 【解決策】`ProductDAO` が継承すべき基底クラス `BaseDAO` クラスに共通の処理を集中する



---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
