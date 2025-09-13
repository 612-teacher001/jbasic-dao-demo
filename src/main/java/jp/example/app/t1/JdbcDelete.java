package jp.example.app.t1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.example.app.provided.configure.DbConfigure;
import jp.example.app.provided.io.Keyboard;

/**
 * データベースの格納されている商品を削除するプログラム
 */
public class JdbcDelete {

	public static void main(String[] args) {
		// 手順-0. 商品一覧を表示（前提処理：検索対象を選びやすくするため）
		// ※参考：main メソッドを呼び出して処理を再利用する例
		JdbcAll.main(new String[] {});
		System.out.println();
		
		// 手順-1. キーボードから削除する商品の商品IDを取得
		int targetId = Keyboard.getInputNumber("削除する商品のIDを入力してください：");
		// 手順-2. 実行するSQLを設定
		String sql = "DELETE FROM products WHERE id = ?";
		// 手順-3. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		// 手順-4. データベース接続関連オブジェクトを取得
		try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-5. プレースホルダを商品IDで置換
			pstmt.setInt(1, targetId);
			// 手順-6. SQLを実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		}
		
		// 手順-0. 商品一覧を表示（参考処理：SQLが成功したかどうかを確認するため）
		System.out.println("");
		JdbcAll.main(new String[] {});

	}

}
