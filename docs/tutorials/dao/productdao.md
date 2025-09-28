# ProductDAO クラス利用ガイド

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)


## 1. 役割

`ProductDAO` クラスは、productsテーブルにアクセスしてCRUD操作を行う **DAO（Data Access Object）** クラスです。

- データベース接続を内部で管理します。
- 主に以下の操作を提供します：

  - 全件取得：`findAll()`
  - 主キー検索：`findById(int)`（将来的に追加予定）
  - 結果セットをリストに変換：`convertToList(ResultSet)`

現段階では、呼び出す側で **全件検索** に焦点を当てて、内部のSQL実行や `ResultSet` の操作は意識する必要はありません。

## 2. 配布と利用形態

- **配布形態**
  - `jp.example.app.dao` パッケージに含まれるクラスとして提供

- **利用方法**
  
  1. プロジェクトに `ProductDAO` を含める
  2. 他のクラス（例：`DaoAll`）からはインスタンス化して利用する
```java
try {
    ProductDAO dao = new ProductDAO();
    List<Product> list = dao.findAll();
    showProducts(list);  // DaoAll 側で用意した表示用ユーティリティ
    dao.close();
} catch (SQLException e) {
    e.printStackTrace();
}
```
---

## 3. 使用例

### 3.1 全件検索：`ProductDAO#findAll()` メソッド
```java
ProductDAO dao = new ProductDAO();
List<Product> products = dao.findAll();
for (Product p : products) {
    System.out.printf("%d: %s (%d円)\n", p.getId(), p.getName(), p.getPrice());
}
dao.close();
```
### 3.2 主キー検索：'ProductDAO#findById(int)' メソッド
```java
int id = 3;
ProductDAO dao = newProductDAO();
Product bean = dao.finndById(id);
System.out.printf("%d: %s (%d円)\n", p.getId(), p.getName(), p.getPrice());
dao.close();
```
### 3.3 商品登録：`ProductDAO#insert(Product)` メソッド
```java
Product product = new Product(3, "妖怪村", 400);
ProductDAO dao = new ProductDAO();
dao.insert(product);

List<Product> products = dao.findAll();
for (Product p : products) {
    System.out.printf("%d: %s (%d円)\n", p.getId(), p.getName(), p.getPrice());
}
dao.close();
```
### 3.3 商品更新：`ProductDAO#update(Product)` メソッド
```java
Product product = new Product(3, "妖怪村", 400);
ProductDAO dao = new ProductDAO();
dao.update(product);

List<Product> products = dao.findAll();
for (Product p : products) {
    System.out.printf("%d: %s (%d円)\n", p.getId(), p.getName(), p.getPrice());
}
dao.close();
```

### 3.2 注意点
- SQLの実行時に問題が発生すると、`SQLException` がスローされます。
- 実務では、DAOで発生したすべての例外は `DAOException` として一括して処理することが多くなります。

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./20-dao.md)

