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
 * データベースの格納されている商品を修正するプログラム
 */
public class JdbcUpdate {

	/**
	 * キーボードから入力された情報をもとに products テーブルの既存商品を更新する
	 *
	 * 特徴:
	 *  - ユーザはキーボードから更新する商品のIDを入力する
	 *  - 該当商品をデータベースから取得する
	 *  - 更新情報としてカテゴリID・商品名・価格・数量をキーボードから入力する
	 *  - 入力値をもとに Product インスタンスを更新する
	 *  - UPDATE 文を実行して products テーブルの該当商品を更新する
	 *  - 更新後に再度商品一覧を表示し、更新結果を確認できるようにする
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
		
		// 手順-1. キーボードから更新する商品の商品IDを取得
		int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
		
		// 手順-2. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		// 手順-3. データベース接続オブジェクトを取得
		try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());) {
			// 手順-4. 更新対象商品を取得
			//   4-1. 実行するSQLを設定
			String sql = "SELECT * FROM products WHERE id = ?";
			//   4-2. SQL実行オブジェクトを取得
			Product target = null;
			try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
				//   4-3. プレースホルダを商品IDで置換
				pstmt.setInt(1, targetId);
				//   4-4. SQLの実行と結果セットの取得
				try (ResultSet rs = pstmt.executeQuery();) {
					//   4-5. 結果セットを商品インスタンスに変換
					if (rs.next()) {
						target = new Product(
									rs.getInt("id"),
									rs.getInt("category_id"),
									rs.getString("name"),
									rs.getInt("price"),
									rs.getInt("quantity")
								);
					}
				}
			}
			
			// 手順-5. 取得した商品インスタンスの存在チェック
			if (target == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			
			// 手順-6. 商品を更新
			//   6-1. 更新情報を取得
			int categoryId = Keyboard.getInputNumber("カテゴリID（現在の値：" + target.getCategoryId() + "）：");
			String name = Keyboard.getInputString("商品名（現在の値：" + target.getName() + "）：");
			int price = Keyboard.getInputNumber("価格（現在の値：" + target.getPrice() + "）：");
			int quantity = Keyboard.getInputNumber("数量（現在の値：" + target.getQuantity() + "）：");
			//   6-2. 商品インスタンスを取得した更新情報で再設定
			target.setCategoryId(categoryId);
			target.setName(name);
			target.setPrice(price);
			target.setQuantity(quantity);
			//   6-3. 実行するSQLの設定
			sql = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";
			//   6-4. SQL実行オブジェクトを取得
			try (PreparedStatement pstmt = conn.prepareStatement(sql);) {
				//   6-5. プレースホルダを更新情報で置換
				pstmt.setInt(1, target.getCategoryId());
				pstmt.setString(2, target.getName());
				pstmt.setInt(3, target.getPrice());
				pstmt.setInt(4, target.getQuantity());
				pstmt.setInt(5, target.getId());
				//   6-6. SQLの実行
				pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		}
		
		// 手順-0. 商品一覧を表示（参考処理：SQLが成功したかどうかを確認するため）
		System.out.println("");
		JdbcAll.main(new String[] {});
	}

}
