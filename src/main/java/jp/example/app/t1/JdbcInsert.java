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
 * テーブルにレコードを新規登録するプログラム
 */
public class JdbcInsert {

	public static void main(String[] args) {
		// 手順-1. 全件表示
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-2. 商品を新規登録するメッセージを表示
		Display.showMessageln("【商品登録】");
		// 手順-3. キーボードから登録する情報を取得（カテゴリID、商品名、価格、数量）
		int categoryId = Keyboard.getInputNumber("カテゴリID：");
		String name = Keyboard.getInputString("商品名：");
		int price = Keyboard.getInputNumber("価格：");
		int quantity = Keyboard.getInputNumber("数量：");
		// 手順-4. 入力値をもとに登録する商品をインスタンス化
		Product product = new Product(categoryId, name, price, quantity);
		// 手順-5. 実行するSQLを設定
		String sql = "INSERT INTO products (category_id, name, price, quantity) VALUES (?, ?, ?, ?)";
		// 手順-6. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		try (// 手順-7. データベース接続オブジェクトを取得（例外処理対象：リソース自動解放のしくみで管理される）
			 Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 // 手順-8. SQL実行オブジェクトを取得（例外処理対象）
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-9. SQLのプレースホルダを商品インスタンスのフィールド値で置換（例外処理対象）
			pstmt.setInt(1, product.getCategoryId());
			pstmt.setString(2, product.getName());
			pstmt.setInt(3, product.getPrice());
			pstmt.setInt(4, product.getQuantity());
			// 手順-10. SQLの実行
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}
		// 手順-11. 商品リストを表示
		System.out.println();
		JdbcAll.main(new String[] {});
	}

}
