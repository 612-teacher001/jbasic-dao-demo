# *T2.1*　DAOによるCRUD操作・検索 ～ DAOを導入してみる・リソース自動破棄編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[5fe18af](https://github.com/612-teacher001/jbasic-dao-demo/commit/5fe18af)
- クラス：[`jp.example.app.t2.DaoAll`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoAll.java)

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
  ProductDAO dao = new ProductDAO();
  ...（中略）...
} catch (SQLException e) {
  ...（中略）...
}
```
- `ProductDAO`をインスタンス化します。  
`ProductDAO` クラスに `AutoClosable` インタフェースを実装したので、finallyブロックでデータベース接続オブジェクトを解放する必要がなくなりました。
- ProductDAOについては [ProductDAO クラス利用ガイド](./productdao.md) を参照してください。

### 手順-2. すべての商品の商品リストを取得する
### 手順-3. 取得した商品を表示する
- これまでのチュートリアルで扱ったものと同じです。  
[基本形編](./21-dao-all_01.md) を参照してください。


### 手順-4. ProductDAOを破棄する
- DAOはtry-with-resourcesにより自動破棄に変更になったため削除しました。

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - ProductDAOに `AutoClosable` インタフェースを実装したのでtry-with-resourcesを利用することでリソースは自動破棄されるようになる

## 5. 問題点
この `ProductDAO` のままでは以下のような問題があります：

  - 他のテーブルにアクセスするDAOについてもコンストラクタの中でデータベース接続オブジェクトを取得する必要がある  
  　　⇒ 【解決策】`ProductDAO` が継承すべき基底クラス `BaseDAO` クラスに共通の処理を集中する



---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
