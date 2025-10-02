package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.common.Utils;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * テーブルの主キー検索によってレコードを取得し表示するプログラム
 */
public class DaoId {

	/**
	 * 入力された商品IDを元にproductsテーブルを主キー検索して商品を取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - 取得したレコードは1件または0件である
	 *  - 結果は、Utils#showProduct(Product) メソッドを呼び出して表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		
		try (// 手順-1. ProductDAOをインスタンス化
			 ProductDAO dao = new ProductDAO();) {
			// 手順-2. 全件表示
			List<Product> list = dao.findAll();
			Utils.showProductList(list);
			// 手順-3. キーボードから入力された検索する商品のIDを取得
			System.out.println();
			int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
			System.out.println();
			// 手順-4. 入力された商品IDから商品を取得
			Product product = dao.findById(targetId);
			// 手順-5. 取得した商品の存在チェック
			if (product == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			// 手順-6. 取得した商品を表示
			Utils.showProduct(product);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレース（デバッグ用の詳細情報）を表示
			e.printStackTrace();
			return;
		}
	}

}
