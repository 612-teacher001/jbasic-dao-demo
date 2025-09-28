package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.common.Utils;
import jp.example.app.dao.ProductDAO;

/**
 * テーブルの全レコードを取得して表示するプログラム
 */
public class DaoAll {

	/**
	 * productsテーブルのすべての商品を取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 取得したレコードは主キー(id)の昇順で並べ替える
	 *  - 結果は、Utils#showProductList(List<Product>) メソッドを呼び出して表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		ProductDAO dao = null;
		try {
			// 手順-1. ProductDAOをインスタンス化
			dao = new ProductDAO();
			// 手順-2. すべての商品の商品リストを取得
			List<Product> productList = dao.findAll();
			// 手順-3. 取得した商品を表示
			Utils.showProductList(productList);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレース（デバッグ用の詳細情報）を表示
			e.printStackTrace();
			return;
		} finally {
			// 手順-4. ProductDAOを破棄
			try {
				if (dao != null) {
					dao.close();
				}
			} catch (SQLException e) {
				// 例外が発生した場合：スタックトレース（デバッグ用の詳細情報）を表示
				e.printStackTrace();
			}
		}
	}

}
