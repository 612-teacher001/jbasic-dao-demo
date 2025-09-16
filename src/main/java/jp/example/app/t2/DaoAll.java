package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;

/**
 * DAOによる全件検索を実行するプログラム
 */
public class DaoAll {

	/**
	 * DAOを利用して products テーブルのすべてのレコードを取得し、標準出力に表示する
	 *
	 * 特徴:
	 *  - ProductDAO の findAll メソッドを用いて商品リストを取得する
	 *  - 取得したレコードは主キー (id) の昇順で並べ替えられている
	 *  - 結果は「商品ID、カテゴリID、商品名、価格、数量」の形式で表示する
	 *  - 取得件数を先頭に表示し、その後に見出し行と全レコードを出力する
	 *  - SQLException が発生した場合はスタックトレースを出力する
	 *
	 * @param args 未使用
	 */
	public static void main(String[] args) {
		// 手順-1. finally で close() できるように、スコープの外で宣言しておく（初期値は null）
		ProductDAO dao = null;
		try {
			/* DAOによるCRUD操作・全件検索 */
			// 手順-2. DAOのインスタンス化
			dao = new ProductDAO();
			// 手順-3. すべての商品を取得して商品リストを生成
			List<Product> list = dao.findAll();
			// 手順-4. 商品リストのすべての要素を表示
			showProducts(list);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		} finally {
			// 手順-5. DAOの解放
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

	/**
	 * 商品リストを標準出力に表示する
	 *
	 * 処理の内容:
	 *  1. 商品リストの件数を表示
	 *  2. 見出し行を表示
	 *  3. 商品リストの各要素を順に表示
	 *
	 * @param list 商品リスト
	 */
	private static void showProducts(List<Product> list) {
		// 商品リストの要素数を表示
		Display.showMessageln(list.size() + "件の商品が見つかりました。\n");
		// 見出し行の表示
		System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
				  "商品ID", "カテゴリID", "商品名", "価格", "数量");
		// すべての要素の表示
		for (Product bean : list) {
		// 要素の表示
			System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
					bean.getId(),
					bean.getCategoryId(),
					bean.getName(),
					bean.getPrice(),
					bean.getQuantity()
				 );
		}
	}
	
}
