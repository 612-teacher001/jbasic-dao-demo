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
 * データベースからキーワードが商品名に含まれている商品を取得するプログラム
 */
public class JdbcKeyword {

	/**
	 * キーボードから入力されたキーワードを商品名に含んだ商品をproductsテーブルから取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 手順-0で別クラスの main メソッドを呼び出し、前提処理を再利用している例を示す
	 *  - ユーザはキーボードから検索キーワードを入力する
	 *  - 指定されたキーワードが商品名に含まれる商品を検索して表示する
	 *  - 取得したレコードは主キー(id)の昇順で並べ替える
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - 検索結果が0件の場合は「指定されたキーワードを商品名に含む商品は見つかりませんでした。」と表示して終了する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 *  - 入力値チェックは不要
	 *  - 未入力または空文字列が入力された場合は全件検索とする
	 *  - 実施する検索はあいまい検索（部分一致検索）とする
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-0. 商品一覧を表示（前提処理：検索対象を選びやすくするため）
		// ※参考：main メソッドを呼び出して処理を再利用する例
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-1. キーボードから入力された検索キーワードの取得
		String keyword = Keyboard.getInputString("検索キーワードを入力してください：");
		// 手順-2. 実行するSQLの設定
		String sql = "SELECT * FROM products WHERE name LIKE ? ORDER BY id";
		// 手順-3. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		// 手順-4. データベース接続関連オブジェクトを取得
		try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-5. プレースホルダを検索キーワードに置換
			pstmt.setString(1, "%" + keyword + "%");
			// 手順-6. SQLの実行と結果セットの取得
			List<Product> list = new ArrayList<Product>();
			try (ResultSet rs = pstmt.executeQuery();) {
				// 手順-7. 結果セットから商品リストに変換：convertToList(ResultSet)メソッドの呼び出し
				list = convertToList(rs);
			}
			// 手順-8. 検索結果のチェック
			if (list.size() == 0) {
				Display.showMessageln("指定されたキーワードを商品名に含む商品は見つかりませんでした。");
				return;
			}
			// 手順-9. 取得した商品リストを表示
			// 9.1 見出し行の表示
			System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
					  "商品ID", "カテゴリID", "商品名", "価格", "数量");
			// 9.2 すべての要素の表示
			for (Product bean : list) {
				// 9.3 要素の表示
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
		}

	}

	/**
	 * 結果セットの各行を Product オブジェクトに変換し、リストとして返す。
	 * 
	 * @param rs SQL実行結果の ResultSet（商品情報が格納されていることを前提とする）
	 * @return   商品のリスト（空リストの場合もある）
	 * @throws SQLException 結果セットから値を取得する際にエラーが発生した場合
	 */
	public static List<Product> convertToList(ResultSet rs) throws SQLException {
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			int id = rs.getInt("id");
			int categoryId = rs.getInt("category_id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			int quantity = rs.getInt("quantity");
			list.add(new Product(id, categoryId, name, price, quantity));
		}
		return list;
	}

}
