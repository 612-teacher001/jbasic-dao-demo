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
 * データベースからすべての商品を取得するプログラム
 */
public class JdbcAll {

	/**
	 * productsテーブルのすべてのレコードを取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 取得したレコードは主キー(id)の昇順で並べ替える
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-1. データベース接続関連のオブジェクとの初期化
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 手順-2. 実行するSQLを設定
			String sql = "SELECT * FROM products ORDER BY id";
			// 手順-3. データベース接続オブジェクトを取得
			// 3.1 データベース接続情報の取得
			DbConfigure configure = new DbConfigure();
			String url = configure.getUrl();
			String user = configure.getUser();
			String pass = configure.getPassword();
			// 3.2 データベース接続オブジェクトの取得
			conn = DriverManager.getConnection(url, user, pass);
			// 手順-4. SQL実行オブジェクトを取得
			pstmt = conn.prepareStatement(sql);
			// 手順-5. SQLの実行と結果セットを取得
			rs = pstmt.executeQuery();
			// 手順-6. 結果セットから商品リストに変換
			// 6.1 商品リストの初期化
			List<Product> list = new ArrayList<>();
			while (rs.next()) {
				// 6.2 結果セットのすべてのレコードからの商品インスタンスを生成
				int id = rs.getInt("id");
				int categoryId = rs.getInt("category_id");
				String name = rs.getString("name");
				int price = rs.getInt("price");
				int quantity = rs.getInt("quantity");
				Product bean = new Product(id, categoryId, name, price, quantity);
				// 6.3 商品インスタンスを商品リストに追加
				list.add(bean);
			}
			// 手順-7. 商品リストを表示
			// 7.1 見出し行の表示
			System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
							  "商品ID", "カテゴリID", "商品名", "価格", "数量");
			// 7.2 すべての要素の表示
			for (Product bean : list) {
				// 7.3 要素の表示
				System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
									bean.getId(),
									bean.getCategoryId(),
									bean.getName(),
									bean.getPrice(),
									bean.getQuantity()
								 );
			}
		
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		} finally {
			// 手順-8. データベース接続関連のオブジェクとのクローズ処理
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
				e.printStackTrace();
			}
		}
		
	}

}
