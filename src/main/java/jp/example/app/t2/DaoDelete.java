package jp.example.app.t2;

import java.sql.SQLException;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.common.Utils;
import jp.example.app.dao.ProductDAO;
import jp.example.app.provided.io.Display;
import jp.example.app.provided.io.Keyboard;

/**
 * テーブルからレコードを削除するプログラム
 */
public class DaoDelete {

	public static void main(String[] args) {
		try (// 手順-1. ProductDAOをインスタンス化
			 ProductDAO dao = new ProductDAO();) {
			// 手順-2. 初期一覧の表示
			//   2-1. 全件取得
			List<Product> productList = dao.findAll();
			//   2-2. 商品リストの表示
			Utils.showProductList(productList);
			System.out.println();
			// 手順-3. 削除対象商品のID取得と削除対象商品の取得
			//   3-1. 商品を削除するメッセージを表示
			Display.showMessageln("【商品削除 ft. ProductDAO】");
			//   3-2. キーボードから削除対象商品のIDを取得
			int targetId = Keyboard.getInputNumber("削除する商品のIDを入力してください：");
			//   3-3. 入力された商品IDから商品を取得
			Product target = dao.findById(targetId);
			//   3-4. 取得した商品の存在チェック
			if (target == null) {
				Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
				return;
			}
			// 手順-4. 削除処理
			dao.deleteById(targetId);
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
