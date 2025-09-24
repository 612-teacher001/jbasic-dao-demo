package jp.example.app.t1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.example.app.bean.Product;
import jp.example.app.provided.configure.DbConfigure;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

public class JdbcId {

	/**
	 * キーボードから入力された商品IDに合致する商品をproductsテーブルから取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 全商品の一覧を表示する
	 *  - 検索する商品の商品IDをキーボードから入力する
	 *  - 未入力または空文字が入力された場合はプログラムを終了する
	 *  - 入力値のエラーチェックはしない
	 *  - 商品が見つからない場合は「指定されたIDの商品は見つかりませんでした。」と表示して終了する
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-1. 全商品を表示
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-2. キーボードから入力された商品IDを取得
		int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
		// 手順-3. 実行するSQLを設定
		String sql = "SELECT * FROM products WHERE id = ?";
		// 手順-4. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		
		Product product = null;
		try (// 手順-5. データベース接続オブエジェクトを取得（例外処理対象：リソース自動解放のしくみで管理される）
			 Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 // 手順-6. SQL実行オブジェクトを取得（例外処理対象）
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-7. SQLのプレースホルダを取得した商品IDで置換（例外処理対象）
			pstmt.setInt(1, targetId);
			// 手順-8. SQLの実行と結果セットの取得（例外処理対象：リソース自動解放のしくみで管理される）
			try (ResultSet rs = pstmt.executeQuery();) {
				// 手順-9. 結果セットから商品インスタンスに変換
				if (rs.next()) {
					int id = rs.getInt("id");
					int categoryId = rs.getInt("category_id");
					String name  = rs.getString("name");
					int price = rs.getInt("price");
					int quantity = rs.getInt("quantity");
					product = new Product(id, categoryId, name, price, quantity);
				}
			}
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}
		
		// 手順-10. 商品インスタンスのチェック
		if (product == null) {
			Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
			return;
		}
		// 手順-11. 商品インスタンスを表示
		// 見出し行の表示
		System.out.println(); // 区切り用の空行
		System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
						  "商品ID", "カテゴリID", "商品名", "価格", "数量");
		// 商品インスタンスの表示
		System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
				product.getId(),
				product.getCategoryId(),
				product.getName(),
				product.getPrice(),
				product.getQuantity()
			 );
	}

}
