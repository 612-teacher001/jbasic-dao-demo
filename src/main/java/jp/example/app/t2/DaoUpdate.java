package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.common.Utils;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * テーブルにレコードを新規登録するプログラム
 */
public class DaoUpdate {

	public static void main(String[] args) {
		
		try (// 手順-1. ProductDAOをインスタンス化
			 ProductDAO dao = new ProductDAO();) {
			// 手順-2. 初期一覧の表示
			//   2-1. 全件取得
			List<Product> productList = dao.findAll();
			//   2-2. 商品リストの表示
			Utils.showProductList(productList);
			// 手順-3. 更新対象商品のID取得と更新対象商品の取得
			//   3-1. 商品を更新するメッセージを表示
			Display.showMessageln("【商品更新 ft. ProductDAO】");
			//   3-2. キーボードから更新対象商品のIDを取得
			int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
			//   3-3. 入力された商品IDから商品を取得
			Product target = dao.findById(targetId);
			//   3-4. 取得した商品の存在チェック
			if (target == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			// 手順-4. 更新情報の入力と更新処理
			//   4-1. キーボードから更新する情報を取得（カテゴリID、商品名、価格、数量）
			int categoryId = Keyboard.getInputNumber("カテゴリID（現：" + target.getCategoryId() + "）：");
			String name = Keyboard.getInputString("商品名（現：" + target.getName() + "）：");
			int price = Keyboard.getInputNumber("価格（現：" + target.getPrice() + "）：");
			int quantity = Keyboard.getInputNumber("数量（現：" + target.getQuantity() + "）：");
			//   4-2. 取得した情報で更新対象商品を再設定（カテゴリID、商品名、価格、数量）
			target.setCategoryId(categoryId);
			target.setName(name);
			target.setPrice(price);
			target.setQuantity(quantity);
			//   4-3. 更新対象商品を更新
			dao.update(target);
			// 手順-5. 更新後の一覧表示
			//   5-1. 全件取得
			productList = dao.findAll();
			//   5-2. 商品リストの表示
			System.out.println();
			Utils.showProductList(productList);
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}

	}

}
