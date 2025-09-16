# *T2.2*　DAOによるCRUD操作・主キー検索 ～ 指定IDの商品を取得してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[de29194](https://github.com/612-teacher001/jbasic-dao-demo/commit/de29194)
- クラス：[`jp.example.app.t2.DaoAll`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoId.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

---

## 1. 概要
このチュートリアルでは、DAOを利用して productsテーブルの **指定した商品IDのレコードを取得し表示する方法** を説明します。

DAO（Data Access Object）パターンを導入することで、データベース操作を専用クラスにまとめ、呼び出し側では「検索して結果を受け取るだけ」というシンプルなコードを書けるようになります。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。  
また、DAOクラスの概要については別途用意している [`ProductDAO` クラス利用ガイド](./productdao.md) を参照してください。

---

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t2.DaoId#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-1：DAOを宣言する
- 前回の処理と変更はありません。  
詳細は[前回のチュートリアル](./21-dao-all.md)を参照してください。

### 手順-2：DAOをインスタンス化する
- 前回の処理と変更はありません。  
詳細は[前回のチュートリアル](./21-dao-all.md)を参照してください。

### 手順-3：検索する商品IDをキーボードから取得する
```java
int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
```
- 検索したい商品IDを数値としてキーボードから入力された値を取得します。
- 入力値のチェックは省略しています（チュートリアルでは簡略化のため）。

### 手順-4：取得した商品IDの商品インスタンスを取得する
```java
Product target = dao.findById(targetId);
```
- `ProductDAO#findById(int)` メソッドを呼び出し、該当商品を取得します。
- 該当する商品が存在しない場合は null が返ります。


## 手順-5. 商品インスタンスを表示する
```java
showProducts(target);
```
- `showProduct(Product)` メソッドは以下の処理を実行します：

  1. 商品インスタンスの存在をチェック
  2. 見出し行を表示
  3. 商品インスタンスを表示

### 手順-6：DAOをクローズする
- 前回の処理と変更はありません。  
詳細は[前回のチュートリアル](./21-dao-all.md)を参照してください。

---


## 4. まとめ

ここのコードで学ぶべきポイント：

  - DAOクラスを導入することにより、SQLやJDBCのリソース管理はDAO内に隠蔽され、呼び出し側は簡潔に書ける
  - 主キー検索は ProductDAO#findById(int id) を呼ぶだけで実現できる
  - 存在しない商品IDに対しても null チェックを入れることで安全に表示できる

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
