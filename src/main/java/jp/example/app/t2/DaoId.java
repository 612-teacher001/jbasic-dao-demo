package jp.example.app.t2;

import java.sql.SQLException;

import jp.example.app.bean.Product;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * DAOによる主キー検索を実行するプログラム
 */
public class DaoId {

	/**
	 *  DAOを利用して productsテーブルから指定した商品IDのレコードを取得し、標準出力に表示する
	 *
	 * 特徴:
	 *  - ユーザはキーボードから検索する商品のIDを入力する
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
			/* DAOによるCRUD操作・主キー検索 */
			// 手順-2. DAOのインスタンス化
			dao = new ProductDAO();
			// 手順-3. キーボードから入力された検索対象商品の商品IDの取得
			int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
			// 手順-4. 取得した商品IDの商品インスタンスを取得
			Product target = dao.findById(targetId);
			// 手順-5. 商品インスタンスを表示
			showProduct(target);
			
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

	/**
	 * 商品インスタンスを標準出力に表示する
	 *
	 * 処理の内容:
	 *  1. 商品インスタンスの存在をチェック：存在しない場合は処理終了
	 *  2. 見出し行を表示
	 *  3. 商品インスタンスを表示
	 *
	 * @param bean 商品インスタンス
	 */
	public static void showProduct(Product bean) {
		// 該当する商品の存在チェック
		if (bean == null) {
			Display.showMessageln("該当する商品が見つかりませんでした。\n");
			return;
		}
		// 見出し行の表示
		System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
				  "商品ID", "カテゴリID", "商品名", "価格", "数量");
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
