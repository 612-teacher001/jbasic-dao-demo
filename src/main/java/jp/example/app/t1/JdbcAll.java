package jp.example.app.t1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.provided.configure.DbConfigure;

/**
 * テーブルの全レコードを取得して表示するプログラム
 */
public class JdbcAll {

	/**
	 * productsテーブルのすべての商品を取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 取得したレコードは主キー(id)の昇順で並べ替える
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		
		// 手順-1. 実行するSQLを設定
		String sql = "SELECT * FROM products ORDER BY id";
		// 手順-2. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		
		try {
			// 手順-3. 接続情報をもとにデータベース接続オブジェクトを取得
			Connection conn 
				= DriverManager.getConnection(
									 		  configure.getUrl(), 
									 		  configure.getUser(), 
									 		  configure.getPassword());
			// 参考：configureの値を一時変数に代入して渡す方法
			// String url = configure.getUrl(); 
			// String username = configure.getUser(); 
			// String password = configure.getPassword();
			// Connection conn = DriverManager.getConnection(url, username, password);
			
			// 手順-4. SQL実行オブジェクトを取得
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 手順-5. SQLの実行と結果セットを取得
			ResultSet rs = pstmt.executeQuery();
			// 手順-6. 結果セットから商品リストに変換
			//   6-1. 商品リストを初期化
			List<Product> productList = new ArrayList<Product>();
			//   6-2. 結果セットの各行を順に処理
			while (rs.next()) {
				// 6-3. 読み出したレコードのすべてのフィールド値を取得
				int id = rs.getInt("id");
				int categoryId = rs.getInt("category_id");
				String name  = rs.getString("name");
				int price = rs.getInt("price");
				int quantity = rs.getInt("quantity");
				// 6-4. 取得したフィールドから商品をインスタンス化
				Product product = new Product(id, categoryId, name, price, quantity);
				// 6-5. 商品インスタンスを商品リストに追加
				productList.add(product);
				// 参考：addメソッドの引数で直接インスタンス化する方法（6-4と6-5を統合する例）
				// productList.add(new Product(id, categoryId, name, price, quantity));
			}
			// 手順-7. 商品リストのすべての要素を表示
			//   7-1. 見出し行の表示
			System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
							  "商品ID", "カテゴリID", "商品名", "価格", "数量");
			//   7-2. すべての要素の表示
			for (Product product : productList) {
				// 7-3. 要素の表示
				System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
									product.getId(),
									product.getCategoryId(),
									product.getName(),
									product.getPrice(),
									product.getQuantity()
								 );
			}
			
			// 手順-8. データベース接続関連のオブジェクトの解放
			rs.close();
			pstmt.close();
			conn.close();
			
		} catch (SQLException e) {
			// 手順-9. 例外処理の実行：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		}
	}

}
