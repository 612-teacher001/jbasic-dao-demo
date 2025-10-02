# *T2.1*　DAOによるCRUD操作・検索 ～ 商品名でキーワード検索してみる編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)

---
### 今回のチュートリアル対象

- コミット：[a490ba2](https://github.com/612-teacher001/jbasic-dao-demo/commit/a490ba2)
- クラス：[`jp.example.app.t2.DaoKeyword`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t2/DaoKeyword.java)  
　　　[`jp.example.app.dao.ProductDAO`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/dao/ProductDAO.java)

---

## 1. 概要

このチュートリアルでは、**DAOを使ったキーワード検索の方法** について解説します。

---

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

---

## 3. 解説

前回までのチュートリアルまでで全件検索と主キー検索を扱ってきました。

これらの検索操作については大きく分けて

  - 全件検索のような固定のSQLに対する操作  
  - 実行時に決まる値による動的なSQLに対する操作

2つに分かれていました。今回の検索を含めてこれらの内容についてのまとめると以下のようになります：

| 検索の種類     | SQLのタイプ | おもな解説内容 |
| ------------- | ---------- | ------------- |
| 全件検索       | 実行時によらない固定したSQL（静的） | DAOを利用したSQLの基本的な実行方法       |
| 主キー検索     | 実行時に値が決まるSQL（動的）       | DAOを利用したプレースホルダ付きSQLの実行 |
| キーワード検索 | 実行時に値が決るSQL（動的）         | DAOを利用したプレースホルダ付きSQLの実行 |

このキーワード検索では実行されるSQLは動的なSQLになるので、前回とほぼ同じになります。

違う点は、検索フィールドと検索条件です。

| 検索の種類     | 検索フィールド             | 検索条件 |
| ------------- | ------------------------- | ------- |
| 全件検索       | なし                      | なし     |
| 主キー検索     | 主キー（`id` フィールド）   | 完全一致 |
| キーワード検索 | 商品名（`name` フィールド） | 部分一致 |

そこで、このチュートリアルでは、`DaoKeyword#main(String[])` メソッドのソースコードと `ProductDAO#findByNameLike(String)` メソッドを再掲して、総覧的に解説します。

## 3.1. `DaoKeyword` クラス
```java
public class DaoKeyword {

	/**
	 * 入力されたキーワードを元にproductsテーブルを商品名フィールドでキーワード検索して商品を取得して標準出力に表示する
	 *
	 * 特徴:
   
   ...（中略）..

	 */
	public static void main(String[] args) {
		
		try (// 手順-1. ProductDAOをインスタンス化
			 ProductDAO dao = new ProductDAO();) {
			// 手順-2. 全件表示
			List<Product> list = dao.findAll();
			Utils.showProductList(list);
			// 手順-3. キーボードから入力されたキーワードを取得
			System.out.println();
			String keyword = Keyboard.getInputString("検索キーワードを入力してください：");
			System.out.println();
			// 手順-4. 入力されたキーワードから商品リストを取得
			List<Product> productList = dao.findByNameLike(keyword);
			// 手順-5. 取得した商品リストの件数チェック
			if (productList.isEmpty()) {
				Display.showMessageln("指定されたキーワードを商品名に含む商品は見つかりませんでした。");
				return;
			}
			// 手順-6. 取得した商品リストを表示
			System.out.println();
			Utils.showProductList(productList);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレース（デバッグ用の詳細情報）を表示
			e.printStackTrace();
			return;
		}

	}

}
```
- メソッドヘッダは途中省略しました。
- 手順-1でデータベース接続関連の処理を実行します。  
データベース接続情報の取得とデータベース接続オブジェクトの取得をこの1行で完了していることに留意してください。
- 手順-2、手順-3は表示処理と入力受付の処理です。
これは標準出力を介した処置です。
- 手順4でキーワード検索が実行され結果を受け取る処理です。  
SQLの実行と結果の取得がこの1行で完了することに留意してください。
- 手順-5と手順-6では結果を表示しています。

### 3.2. `ProductDAO` クラス
```java
public class ProductDAO extends BaseDAO {

	/**
	 * クラス定数
	 */
	// SQL文字列群
  ...（中略）...

	private static final String SQL_FIND_BY_NAME_LIKE = "SELECT * FROM products WHERE NAME LIKE ?";
	
  ...（中略）...

	/**
	 * 指定されたキーワードが含まれる商品名の商品を取得する
	 * @param keyword 検索キーワード
	 * @return 商品リスト
	 * @throws SQLException
	 */
	public List<Product> findByNameLike(String keyword) throws SQLException {
		// 1. 戻り値の初期化
		List<Product> list = null;
		try (// 2. SQL実行オブジェクトの取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_FIND_BY_NAME_LIKE);) {
			// 3. プレースホルダをパラメータで置換：%は任意の文字列を表すため部分一致検索が可能
			pstmt.setString(1, "%" + keyword + "%");
			// 4. SQLの実行と結果セットの取得
			try (// 5. 結果セットを商品リストに変換
				 ResultSet rs = pstmt.executeQuery();) {
				// 5. 結果セットを商品リストに変換
				list = this.convertToList(rs);
			}
		}
		// 戻り値の返却
		return list;
	}

...（中略）...

}
```
- メソッドヘッダおよび `findByNameLike` メソッドは途中省略しました。
- SQLの準備・実行・結果変換までをDAO内部で完結させているため、呼び出し側は検索メソッドを呼ぶだけで商品リストを得られます。  
検索メソッドでは以下の処理を実行します：
  1. SQL実行オブジェクトの取得
  2. プレースホルダのパラメータバインド（このチュートリアルでは「パラメータで置換」と表現しています）
  3. SQLの実行と結果セットの取得
  4. 結果セットのリストやインスタンスへの変換
- `LIKE` 演算子による検索になりますが、プレースホルダをキーワードに置換する際には、キーワードの前後にワイルドカード文字 `%` を付加しています。  
ワイルドカード文字については [JDBC接続によるCRUD操作・キーワード検索 ～ 商品をキーワード検索してみた編 検索キーワードの扱いについて](../jdbc/13-jdbc-keyword.md#memo) を参照してください。

## 5. まとめ

ここのコードで学ぶべきポイント：

  - JDBC編と同じ検索処理 を、DAOを導入すると **呼び出し側は1行で済む**
  - SQL文・リソース管理をDAOに集約したことで 処理の重複や冗長さがなくなる
  - 全件検索・主キー検索も同様にDAO呼び出しだけで済むので、アプリケーションのコードがシンプルになる
  - DAOの役割は **データアクセスを隠蔽して呼び出しを簡単にすること** である

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [DAOによるCRUD操作](./20-dao.md)
