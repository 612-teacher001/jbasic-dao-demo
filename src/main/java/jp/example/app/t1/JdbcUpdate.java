package jp.example.app.t1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.provided.configure.DbConfigure;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * テーブルにレコードを更新するプログラム
 */
public class JdbcUpdate {

	public static void main(String[] args) {
		// 手順-1. 全件表示
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-2. 商品を更新するメッセージを表示
		Display.showMessageln("【商品更新】");
		// 手順-3. キーボードから更新する商品のIDを取得
		int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
		
		// 手順-4. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		try (// 手順-５. データベース接続オブジェクトを取得（例外処理対象：リソース自動解放のしくみで管理される）
			 Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());) {
			
			// 更新対象商品を取得（前回のチュートリアルの手順-6～手順-10までの処理をメソッド化）
			Product target = getProductById(conn, targetId);
			
			// 手順-11. 商品の存在チェック：存在しない場合はメッセージを表示してプログラムを終了
			if (target == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			
			// 手順-12. 更新情報を取得（カテゴリID、商品名、価格、数量）
			int categoryId = Keyboard.getInputNumber("カテゴリID（現：" + target.getCategoryId() + "）：");
			String name = Keyboard.getInputString("商品名（現：" + target.getName() + "）：");
			int price = Keyboard.getInputNumber("価格（現：" + target.getPrice() + "）：");
			int quantity = Keyboard.getInputNumber("数量（現：" + target.getQuantity() + "）：");
			// 手順-13. 更新情報で更新対象商品を更新
			target.setCategoryId(categoryId);
			target.setName(name);
			target.setPrice(price);
			target.setQuantity(quantity);
			
			// 更新処理の実行（前回のチュートリアルの手順-14～手順-17までの処理をメソッド化）
			updateProduct(conn, target);
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}
		// 手順-17. 商品リストを表示
		System.out.println();
		JdbcAll.main(new String[] {});
	}

	/**
	 * 商品を更新する
	 * @param conn データベース接続オブエジェクト
	 * @param target 更新対象商品インスタンス
	 * @throws SQLException
	 */
	private static void updateProduct(Connection conn, Product target) throws SQLException {
		// 実行するSQLを取得
		String sql = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = conn.prepareStatement(sql);) {
			// SQLのプレースホルダを商品インスタンスのフィールド値で置換（例外処理対象）
			pstmt.setInt(1, target.getCategoryId());
			pstmt.setString(2, target.getName());
			pstmt.setInt(3, target.getPrice());
			pstmt.setInt(4, target.getQuantity());
			pstmt.setInt(5, target.getId());
			// SQLの実行
			pstmt.executeUpdate();
		}
	}

	/**
	 * 指定されたIDの商品を取得する
	 * @param conn データベース接続オブエジェクト
	 * @param targetId 取得する商品のID
	 * @return 指定された商品IDの商品が見つかった場合は商品インスタンス、それ以外はnull
	 * @throws SQLException
	 */
	private static Product getProductById(Connection conn, int targetId) throws SQLException {
		// 実行するSQLを設定
		String sql = "SELECT * FROM products WHERE id = ?";
		Product target = null;
		try (// SQL実行オブジェクトを取得（例外処理対象）
			 PreparedStatement pstmt = conn.prepareStatement(sql);) {
			// SQLのプレースホルダを商品IDで置換（例外処理対象）
			pstmt.setInt(1, targetId);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 結果セットから商品インスタンスに変換
				List<Product> list = JdbcKeyword.convertToList(rs);
				if (!list.isEmpty()) {
					target = list.get(0);
				}
			}
		}
		return target;
	}

}
