package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.common.Utils;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * テーブルのキーワード検索によってレコードを取得し表示するプログラム
 */
public class DaoKeyword {

	/**
	 * 入力されたキーワードを元にproductsテーブルを商品名フィールドでキーワード検索して商品を取得して標準出力に表示する
	 *
	 * 特徴:
	 *  - キーワード検索は商品名フィールドのあいまい検索とする
	 *  - 取得したレコードは主キー(id)の昇順で並べ替える
	 *  - 取得した商品の件数チェックを実施し、0件の場合はメッセージを表示してプログラムを終了する
	 *  - 結果は、Utils#showProductList(List<Product>) メソッドを呼び出して表示する
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
			// 手順-3. キーボードから入力されたキーワードを取得
			System.out.println();
			String keyword = Keyboard.getInputString("検索キーワードを入力してください：");
			System.out.println();
			// 手順-4. 入力されたキーワードから商品リストを取得
			List<Product> productList = dao.findByNameLike(keyword);
			// 手順-5. 取得した商品リストの件数チェック
			if (productList.isEmpty()) {
				Display.showMessageln("指定されたキーワードを商品名に含む商品は見つかりませんでした。");
				return;
			}
			// 手順-6. 取得した商品リストを表示
			System.out.println();
			Utils.showProductList(productList);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレース（デバッグ用の詳細情報）を表示
			e.printStackTrace();
			return;
		}

	}

}
