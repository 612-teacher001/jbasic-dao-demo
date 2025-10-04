# *T2.22*　DAOによるCRUD操作・更新 ～ 商品を更新してみた・洗練編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[1c51f09](https://github.com/612-teacher001/jbasic-dao-demo/commit/1c51f09)
- クラス：[`jp.example.app.t2.DaoUpdate`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoUpdate.java)  
　　　[`jp.example.app.dao.ProductDAO`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/dao/ProductDAO.java)

---

## 1. 概要

このチュートリアルでは、**DAOを使ったレコード更新の方法** について解説します。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

---

## 3. 解説

今回のチュートリアルは、更新処理と登録処理で共通している部分をまとめて補助メソッド化した部分について解説します。

## 3.1. `DaoUpdate` クラス

- `DaoUpdate` クラスには変更はありません。

## 3.2. `ProductDAO` クラス
```java
public class ProductDAO extends BaseDAO {

	/**
	 * クラス定数
	 */
	// SQL文字列群
  ...（中略）...

	private static final String SQL_UPDATE = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";
	
  ...（中略）...

	/**
	 * 商品を登録する
	 * @param product 登録対象商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	public void insert(Product product) throws SQLException {
		// 商品スタンスを引数として渡してProductDAO#insert() メソッドの呼出し
		this.saveProduct(product);
	}

	/**
	 * 商品を更新する
	 * @param product 更新対象商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	public void update(Product product) throws SQLException {
		// 商品スタンスを引数として渡してProductDAO#insert() メソッドの呼出し
		this.saveProduct(product);
	}

...（中略）...

	/**
	 * 商品を保存する
	 * 引数の商品インスタンスのidが0なら登録、それ以外なら更新が実行される
	 * @param product 処理対象の商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	private void saveProduct(Product product) throws SQLException {
		// 1. 実行するSQLの取得
		String sql = "";
		if (product.getId() == 0) {
			// 登録の場合
			sql = SQL_INSERT;
		} else {
			// 更新の場合
			sql = SQL_UPDATE;
		
		}
		
		try (// 2. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(sql)) {
			// 3. プレースホルダをパラメータに置換
			pstmt.setInt(1, product.getCategoryId());
			pstmt.setString(2, product.getName());
			pstmt.setInt(3, product.getPrice());
			pstmt.setInt(4, product.getQuantity());
			if (sql.equals(SQL_UPDATE)) {
				pstmt.setInt(5, product.getId()); // 絞り込み条件のプレースホルダの
			}
			// 4. SQLの実行
			pstmt.executeUpdate();
		}
	}
	
}
```
- クラスヘッダは途中省略しました。
- 登録と更新で処理コードはほとんど重複しており、更新で絞り込み検索を行っている最後のプレースホルダを置換するコードだけが違っています。  
- この部分を `saveProduct(Product)` メソッドとして別定義しました。
- `ProductDAO#insert(Product)` メソッドも `ProductDAO#update(Product)` メッソドも、重複している部分をまとめ `saveProduct(Product)` メソッドを呼び出すように変更しました。
- `saveProduct(Product)` メソッドでは引数の商品インスタンスの `id` フィールドの値が0か非0かで、登録と更新を判断しています。

	| `id` の条件 | 判定 | 説明 |
	| ---------- | ---- | ---- |
	| `id == 0`  | 登録 | 新しく生成したインスタンスには商品IDは初期値として '0' が自動的に設定される |
	| `id != 0`  | 更新 | DBから取得したインスタンスには商品IDがすでに設定されている |

- `saveProduct(Product)` メソッドでは引数の商品インスタンスの `id` フィールドの値が0か非0かで、登録と更新を判断しています。

	- **登録の場合：`id == 0`**  
新しく生成した `Product` インスタンスでは、`int` 型のフィールド `id` はクラスのデフォルト値として 0 が自動的に設定されます。  
このため、まだデータベースに登録されていない新規商品であることを `id == 0` で判定できます。 
	- **更新の場合：`id != 0`**  
データベースから取得したインスタンスにはすでに商品IDが設定されているため、更新対象として判定されます。 |


## 5. まとめ

ここのコードで学ぶべきポイント：

- クラスのインスタンス化ではフィールドのデータ型によってデフォルト値が決まっている  
主なデータ型に対応して準備されているデフォルト値を下表にまとめた：

	| データ型 | デフォルト初期値        |
	| ------- | ---------------------- |
	| int     | 0                      |
	| double  | 0.0                    |
	| boolean | `false`                |
	| char    | `\u0000`（`null`文字）  |
	| String  | `null`                 |
	| Object  | `null`                 |


---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
