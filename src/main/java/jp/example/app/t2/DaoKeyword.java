package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Keyboard;

/**
 * DAOによるキーワード検索を実行するプログラム
 */
public class DaoKeyword {

	/**
	 *　DAOを利用して、入力されたキーワードを含む商品名のレコードをproductsテーブルから検索し、標準出力に表示する

	 * 特徴:
	 *  - ユーザはキーボードから検索キーワードを入力する
	 *  - 該当商品をデータベースから取得する
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 *
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-0. 商品一覧を表示（前提処理：検索対象を選びやすくするため）
		// ※参考：main メソッドを呼び出して処理を再利用する例
		DaoAll.main(new String[] {});
		System.out.println();

		// 手順-1. finally で close() できるように、スコープの外で宣言しておく（初期値は null）
		ProductDAO dao = null;
		try {
			/* DAOによるCRUD操作・キーワード検索 */
			// 手順-2. DAOのインスタンス化
			dao = new ProductDAO();
			// 手順-3. キーボードから入力された検索キーワードの取得
			String keyword = Keyboard.getInputString("検索キーワードを入力してください：");
			// 手順-4. 商品名にキーワードを含む商品のリストを取得
			List<Product> list = dao.findByNameLikeKeyword(keyword);
			// 手順-5. 商品リストを表示
			Utils.showProducts(list);
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		} finally {
			// 手順-6. DAOの解放
			try {
				if (dao != null) {
					dao.close();
				}
			} catch (SQLException e) {
				// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
				e.printStackTrace();
			}
		}
	}


}
