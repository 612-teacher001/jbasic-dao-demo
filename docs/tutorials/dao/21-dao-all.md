# *T2.1*　DAOによるCRUD操作・全検索 ～ データベースに接続してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[832d30a](https://github.com/612-teacher001/jbasic-dao-demo/commit/832d30a)
- クラス：[`jp.example.app.t2.DaoAll`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoAll.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

---

## 1. 概要
このチュートリアルでは、DAOを利用して productsテーブルの **すべてのレコードを取得し表示する方法** を説明します。

DAO（Data Access Object）パターンを導入することで、データベース操作を専用クラスにまとめ、呼び出し側では「検索して結果を受け取るだけ」というシンプルなコードを書けるようになります。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。  
また、DAOクラスの概要については別途用意している [`ProductDAO` クラス利用ガイド](./productdao.md) を参照してください。

---

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t2.DaoAll#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-1：DAOを宣言する
```java
ProductDAO dao = null;
```
- `ProductDAO` を `try/finally` ブロックの外で宣言しておくことで、`finally` ブロック内で確実に `close()` メソッドを呼び出せるようにしています。
- 初期値は `null` にしています。


### 手順-2：DAOをインスタンス化する
``` java
dao = new ProductDAO();
```
- データベースに接続し、操作の準備を行います。
- `ProductDAO` はJDBCの接続やリソース管理を内部で行っています。


### 手順-3：すべての商品を取得して商品リストを生成する
``` java
List<Product> list = dao.findAll();
```
- `ProductDAO#findAll()` メソッドを呼び出すことで、productsテーブルのすべての商品レコードを取得します。
- 取得されたリストは 主キー(`id` フィールド)の昇順で並べ替えられています。


## 手順-4. 商品リストのすべての要素を表示する
```java
showProducts(list);
```
- `showProduct(List<Product>)` メソッドを呼び出して取得した商品リストを標準出力に表示します。
- `showProduct(List<Product>)` メソッドは以下の処理を実行します：

  1. 商品リストの件数を表示
  2. 見出し行を表示
  3. 商品リストの各要素を順に表示

### 手順-5：DAOをクローズする
```java
dao.close();
```
- DAO が保持しているリソース（JDBC 接続など）を解放します。
- `close()` メソッドが例外を投げる可能性があるため、再度 `try-catch` で囲んでいます。 

---


## 4. まとめ

ここのコードで学ぶべきポイント：

  - DAOクラスを導入することにより、SQLやJDBCのリソース管理はDAO内に隠蔽され、呼び出し側は簡潔に書ける
  - 全件検索は `ProductDAO#findAll()` で簡単に実行できる
  - 今後はこのDAOを用いて「主キー検索」「登録」「更新」「削除」などのCRUD操作に進める

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
