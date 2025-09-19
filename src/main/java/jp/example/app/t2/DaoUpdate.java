package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * DAOによる更新を実行するプログラム
 */
public class DaoUpdate {

	/**
	 *　DAOを利用して、入力された商品IDのレコードをproductsテーブルから検索し、商品情報を更新する

	 * 特徴:
	 *  - キーボードから入力された更新対象の商品IDを取得する
	 *  - 該当商品をデータベースから取得する
	 *  - 取得した商品の存在チェック（存在しない場合はプログラム終了）
	 *  - 現在のフィールド値を表示しながらフィールドの更新情報を取得する
	 *    更新情報入力のプロンプトは「商品ID（現在値：7）：」の形式で表示する
	 *  - 更新情報を元に更新する
	 *  - 入力値チェックは省略する
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
			// 手順-2. DAOのインスタンス化
			dao = new ProductDAO();

			/* DAOによるCRUD操作・更新対象の取得 */
			// 手順-3. キーボードから入力された検索キーワードの取得
			int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
			// 手順-4. 商品IDに合致する商品インスタンスを取得
			Product target = dao.findById(targetId);
			
			// 手順-5. 商品インスタンスの存在チェック
			if (target == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			
			// 手順-6. 更新情報を取得
			//   6-1. キーボードから更新情報を取得
			int categoryId = Keyboard.getInputNumber("カテゴリID（現在の値：" + target.getCategoryId() + "）：");
			String name = Keyboard.getInputString("商品名（現在の値：" + target.getName() + "）：");
			int price = Keyboard.getInputNumber("価格（現在の値：" + target.getPrice() + "）：");
			int quantity = Keyboard.getInputNumber("数量（現在の値：" + target.getQuantity() + "）：");
			//   6-2. 商品インスタンスを取得した更新情報で再設定
			target.setCategoryId(categoryId);
			target.setName(name);
			target.setPrice(price);
			target.setQuantity(quantity);
			//   6-3. 取得した情報で更新
			dao.update(target);
			
			// 手順-7. 商品リストのすべての要素を表示
			List<Product> list = dao.findAll();
			System.out.println();
			Utils.showProducts(list);
			
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		} finally {
			// 手順-8. DAOの解放
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
