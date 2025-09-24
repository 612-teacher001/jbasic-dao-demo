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
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * データベースからキーワードが含まれているレコードを取得するプログラム
 */
public class JdbcKeyword {
	
	/**
	 * キーボードから入力されたキーワードを商品名に含んだ商品をproductsテーブルから取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 全商品の一覧を表示する
	 *  - 検索キーワードをキーボードから入力する
	 *  - 未入力または空文字が入力された場合はプログラムを終了する
	 *  - 入力値のエラーチェックはしない
	 *  - 実施する検索は部分一致検索（あいまい検索）とする
	 *  - 商品が見つからない場合は「指定されたキーワードを含む商品は見つかりませんでした。」と表示して終了する
	 *  - 商品が見つかった場合には見つかった商品の件数を「xx件の商品が見つかりました。」と表示する
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で商品IDの昇順で表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-1. 全商品を表示
		JdbcAll.main(new String[] {});
		System.out.println(); // 区切り用空行
		// 手順-2. キーボードから入力された検索キーワードを取得
		String keyword = Keyboard.getInputString("検索キーワードを入力してください：");
		
		// 手順-3. 実行するSQLを設定
		String sql = "SELECT * FROM products WHERE name LIKE ? ORDER BY id";
		// 手順-4. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		List<Product> productList = new ArrayList<>();
		try (// 手順-5. データベース接続オブエジェクトを取得（例外処理対象：リソース自動解放のしくみで管理される）
			 Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 // 手順-6. SQL実行オブジェクトを取得（例外処理対象）
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-7. SQLのプレースホルダを取得した検索キーワードで置換（例外処理対象）
			pstmt.setString(1, "%" + keyword + "%");
			try (// 手順-8. SQLの実行と結果セットの取得（例外処理対象：リソース自動解放のしくみで管理される）
				 ResultSet rs = pstmt.executeQuery();
				) {
				// 手順-9. 結果セットから商品リストに変換
				while (rs.next()) {
					int id = rs.getInt("id");
					int categoryId = rs.getInt("category_id");
					String name  = rs.getString("name");
					int price = rs.getInt("price");
					int quantity = rs.getInt("quantity");
					productList.add(new Product(id, categoryId, name, price, quantity));
				}
			}
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}
		
		// 手順-10. 商品リストのチェック
		if (productList.size() == 0) {
			Display.showMessageln("検索結果：検索キーワードを含む商品名の商品は見つかりませんでした。");
			return;
		}
		// 手順-11. 商品リストを表示
		// 件数の表示
		Display.showMessageln("検索結果：" + productList.size() + "件の商品が見つかりました。");
		// 見出し行の表示
		System.out.println(); // 区切り用の空行
		System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
						  "商品ID", "カテゴリID", "商品名", "価格", "数量");
		// 商品リストの表示
		for (Product product : productList) {
			System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
					product.getId(),
					product.getCategoryId(),
					product.getName(),
					product.getPrice(),
					product.getQuantity()
				 );
		}
	}

}
