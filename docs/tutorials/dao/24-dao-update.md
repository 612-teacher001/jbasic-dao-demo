# *T2.4*　DAOによるCRUD操作・更新 ～ 商品情報を更新してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[03b657d](https://github.com/612-teacher001/jbasic-dao-demo/commit/03b657d)
- クラス：[`jp.example.app.t2.DaoUpdate`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoUpdate.java)
- DAOクラス：[`jp.example.app.dao.ProductDAO`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/dao/ProductDAO.java)


👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

---

## 1. 概要
このチュートリアルでは、DAOを利用して productsテーブルの **商品情報を更新し表示する方法** を説明します。

処理の流れは以下のとおりです：

- キーボードから更新対象の商品IDを入力
- 該当商品を取得して存在チェック
- 現在値を確認しながら更新情報を入力
- DAO を通じてデータベースを更新
- 更新後の商品一覧を表示

DAO（Data Access Object）パターンを導入することで、データベース操作を専用クラスにまとめ、呼び出し側では「検索して結果を受け取るだけ」というシンプルなコードを書けるようになります。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。  
また、DAOクラスの概要については別途用意している [`ProductDAO` クラス利用ガイド](./productdao.md) を参照してください。

---

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t2.DaoUpdate#main(String[] args)` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

👉 DAOの宣言・インスタンス化、リスト表示、DAOクローズの部分は前回のチュートリアル（[全件検索](./21-dao-all.md)）と同じ流れです。  
ここでは **新しく登場する処理**（更新処理）に絞って説明します。

### 手順-1：DAOを宣言する
- 前回の処理と変更はありません。  

### 手順-2：DAOをインスタンス化する
- 前回の処理と変更はありません。  

### 手順-3：キーボードから入力された更新する商品のIDを取得する
``` java
int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
```
- キーボードから入力された商品IDを更新対象商品のIDとして取得します。  
また、簡略化のため入力値チェックは省略しています。

### 手順-4：商品IDに合致する商品インスタンスを取得する
```java
Product target = dao.findById(targetId);
```
- `ProductDAO#findById(int)` メソッドを呼び出すことで、商品IDに合致した商品のインスタンスを取得します。  
- 一致する商品が存在しない場合は `null` が返ります。

### 手順-5：取得した商品インスタンスの存在チェックを行う
- 手順-4で取得した商品が 「`null` であれば指定されたIDの商品は存在しない」という判定をしています。
- 商品が存在しない場合はプログラムを終了します。

### 手順-6：更新情報を入力し、商品インスタンスを更新する
```java
//   6-1. キーボードから更新情報を取得
int categoryId = Keyboard.getInputNumber("カテゴリID（現在の値：" + target.getCategoryId() + "）：");
String name = Keyboard.getInputString("商品名（現在の値：" + target.getName() + "）：");
int price = Keyboard.getInputNumber("価格（現在の値：" + target.getPrice() + "）：");
int quantity = Keyboard.getInputNumber("数量（現在の値：" + target.getQuantity() + "）：");
//   6-2. 商品インスタンスを取得した更新情報で再設定
target.setCategoryId(categoryId);
target.setName(name);
target.setPrice(price);
target.setQuantity(quantity);
//   6-3. 取得した情報で更新
dao.update(target);
```
- 6-1と6-2については、[JDBC接続によるCRUD操作・更新](../jdbc/16-jdbc-update.md) の手順-6を参照してください。
- JDBC編ではUPDATE文を直接発行していましたが、DAOを利用することで呼び出し側は `update()` を呼び出すだけで更新を実行しています。  
商品インスタンスに新しい値をセットするだけで更新でき、SQL構築はDAOに隠蔽されています。  
`ProductDAO#update(Product)` メソッドについては [ProductDAO クラス利用ガイド](./productdao.md)を参照してください。

### 手順-7：DAOをクローズする
- 前回の処理と変更はありません。  

---


## 4. まとめ

ここのコードで学ぶべきポイント：

  - DAO を利用することで、SQLや JDBC リソースを意識せず更新処理が行える
  - 商品インスタンスを取得してからフィールド値を変更し、DAO.update() でデータベースを更新する流れが理解できる
  - 一覧系の処理では、（手順-0 で行ったように）検索対象一覧を事前に表示しておくことで、検索対象をイメージしやすくなる  
また、結果リストを整形して出力することで、実行結果が確認しやすくなる
---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

