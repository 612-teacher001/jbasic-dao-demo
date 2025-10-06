package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.ProductView;
import jp.example.app.common.Utils;
import jp.example.app.dao.ProductJoinDAO;
import jp.example.app.provided.io.Display;

/**
 * 複数のテーブルからレコードを取得するプログラム
 * 単なるひとつのテーブルに対するCRUD操作ではなく複数のテーブルを結合した擬似的テーブルからレコードを取得する。
 */
public class DaoJoin {

	/**
	 * productsテーブルのすべての商品を取得して標準出力に表示する
	 * 商品のカテゴリIDの代わりに、categoriesテーブルのカテゴリ名を表示する。
	 * 
	 * 特徴:
	 *  - 取得したレコードは主キー(id)の昇順で並べ替える
	 *  - 取得した結果の件数が0件ならメッセージを表示してプログラムを終了する
	 *  - 結果は「商品ID、カテゴリ名、商品名、価格、数量」の形式で表示する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try (// 手順-1. ProductJoinDAOをインスタンス化
			 ProductJoinDAO dao = new ProductJoinDAO();) {
			// 手順-2. 商品リストを取得
			List<ProductView> productList = dao.findAll();
			// 手順-3. 商品リストの要素数チェック
			if (productList.isEmpty()) {
				Display.showMessageln("商品は登録されていません。");
				return;
			}
			// 手順-4. 商品リストを表示
			System.out.println();
			Utils.showProductViews(productList);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}
	}

}
