# *T1.2* JDBC接続によるCRUD操作・キーワード検索 ～ キーワードで商品を検索してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### チュートリアルの対象

  - 対象コミット：[341d0ff](https://github.com/612-teacher001/jbasic-dao-demo/commit/341d0ff)
  - 対象クラス：[`jp.example.app.t1.JdbcKeyword`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcKeyword.java)



## 1. 概要

このチュートリアルでは、キーワード検索をどう実現するかという方法を説明します。  

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcKeyword#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

今回のチュートリアルでは手順-7のみを取り上げて説明します。

### 手順-7. 結果セットから商品リストに変換する
```java
// 手順-7. 結果セットから商品リストに変換：convertToList(ResultSet)メソッドの呼び出し
list = convertToList(rs);

// while (rs.next()) {
//   int id = rs.getInt("id");
//   int categoryId = rs.getInt("category_id");
//   String name = rs.getString("name");
//   int price = rs.getInt("price");
//   int quantity = rs.getInt("quantity");
//   list.add(new Product(id, categoryId, name, price, quantity));
// }
```
- コメントにしている部分を `convertToList(ResultSet)` メソッドにまとめ、そのメソッドを呼び出しています。  
引数に指定されている `rs` は `try-with-resources` で宣言されているので、`null` を考慮する必要はありません。
- `convertToList(ResultSet)` メソッドについて [convertToListメソッドについて](#converttolist) を参照してください。<a id="step-7"></a>

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - 処理をメソッドにまとめることで `main` メソッドの見通しが良くなる
  - 変換処理を共通化することで再利用性が向上する
  - データベースの扱いをアプリケーションに適した形（リスト）に変換する

---

## 5. 関連チュートリアルの参照
- 手順-6〜9は前々回のチュートリアルと同じ処理です。
- 詳細は [JDBC接続によるCRUD操作・全検索 ～ try-with-resourcesの導入編](./11-jdbc-all_03.md) を参照してください。

---

## 6. 参考メモ：convertToListメソッドについて<a id="converttolist"></a>

- 検索結果は 結果セットである `ResultSet` オブジェクトとして返されますが、いろいろな機能があるため扱いやすいとはいえません。  
そこで `List<Product>` 型に変換しておくと、アプリケーション内で扱いやすくなります。
- このような事情のために、結果セットから `List<Product>` 型の商品リストに変換する処理をまとめて名前をつけたものが `convertToList` メソッドです：
```java
public static List<Product> convertToList(ResultSet rs) throws SQLException {
    List<Product> list = new ArrayList<>();
    while (rs.next()) {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getInt("price"));
        product.setQuantity(rs.getInt("quantity"));
        list.add(product);
    }
    return list;
}
```
- ポイントは以下のとおりです：

  1）`rs.next()` メソッドで1件ずつレコードを読み出す  
  2）`Product`インスタンスに値を詰め替える  
  3）詰め替えた `Product` インスタンスを `List` に追加する

最終的に複数の商品インスタンスを扱う商品リストが完成します。  
この内容は細かく理解できなくてもチュートリアルの進行には支障ありません。  
処理の流れを整理するために参考にしてください。


[ジャンプ元に戻る](#step-7)


---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
