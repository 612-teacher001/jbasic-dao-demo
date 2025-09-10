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

/**
 * データベースから主キーに対応する商品を取得するプログラム
 */
public class JdbcId {

	/**
	 * 指定されたidに合致したproductsテーブルのレコードを取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 手順-0で別クラスの main メソッドを呼び出し、前提処理を再利用している例を示す
	 *  - 指定IDの商品を検索して表示する
	 *  - ユーザはキーボードから検索対象商品の商品IDを入力する
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - 検索結果が0件の場合は「指定されたIDの商品は見つかりませんでした。」と表示して終了する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 *  - 入力値チェックは不要
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-0. 商品一覧を表示（前提処理：検索対象を選びやすくするため）
		// ※参考：main メソッドを呼び出して処理を再利用する例
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-1. キーボードから入力された検索対象商品の商品IDの取得
		int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
		// 手順-2. 実行するSQLの設定
		String sql = "SELECT * FROM products WHERE id = ?";
		// 手順-3. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		// 手順-4. データベース接続関連オブジェクトを取得
		try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-5. プレースホルダを商品IDに置換
			pstmt.setInt(1, targetId);
			// 手順-6. SQLの実行と結果セットの取得
			Product bean = null;
			try (ResultSet rs = pstmt.executeQuery();) {
				// 手順-7. 結果セットから商品インスタンスを生成
				if (rs.next()) {
					int id = rs.getInt("id");
					int categoryId = rs.getInt("category_id");
					String name = rs.getString("name");
					int price = rs.getInt("price");
					int quantity = rs.getInt("quantity");
					bean = new Product(id, categoryId, name, price, quantity);
				}
			}
			
			// 手順-8. 検索結果のチェック
			if (bean == null) {
				// 検索結果が0件の場合：メッセージを表示して終了
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			
			// 手順-9. 取得した商品インスタンスを表示
			// 9.1 見出し行の表示
			System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
							  "商品ID", "カテゴリID", "商品名", "価格", "数量");
			// 9.2 要素の表示
			System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
								bean.getId(),
								bean.getCategoryId(),
								bean.getName(),
								bean.getPrice(),
								bean.getQuantity()
							 );
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
