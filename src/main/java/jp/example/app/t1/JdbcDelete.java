package jp.example.app.t1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.example.app.bean.Product;
import jp.example.app.provided.configure.DbConfigure;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * テーブルからレコードを削除するプログラム
 */
public class JdbcDelete {

	public static void main(String[] args) {
		// 手順-1. 全件表示
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-2. 商品を削除するメッセージを表示
		Display.showMessageln("【商品削除】");
		// 手順-3. キーボードから削除する商品のIDを取得
		int targetId = Keyboard.getInputNumber("削除する商品のIDを入力してください：");
		// 手順-4. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		try (// 手順-5. データベース接続オブジェクトを取得
			 Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());) {
			// 手順-6. 削除対象商品を取得
			Product target = JdbcUpdate.getProductById(conn, targetId);
			// 手順-7. 削除対象商品の存在チェック
			if (target == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			// 削除を実行
			deleteById(conn, targetId);
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}
		// 手順-12. 全件表示
		System.out.println();
		JdbcAll.main(new String[] {});

	}

	/**
	 * 指定した商品IDの商品を削除する
	 * @param conn データベース接続オブジェクト
	 * @param targetId 削除する商品のID
	 * @throws SQLException
	 */
	private static void deleteById(Connection conn, int targetId) throws SQLException {
		// 実行するSQLの設定
		String sql = "DELETE FROM products WHERE id = ?";
		try (// SQL実行オブジェクトを取得
			 PreparedStatement pstmt = conn.prepareStatement(sql);) {
			// SQLのプレースホルダを商品IDで置換
			pstmt.setInt(1, targetId);
			// SQLの実行
			pstmt.executeUpdate();
		}
		
	}

}
