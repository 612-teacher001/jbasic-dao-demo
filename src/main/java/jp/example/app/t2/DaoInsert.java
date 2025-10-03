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
public class DaoInsert {

	public static void main(String[] args) {
		
		try (// 手順-1. ProductDAOをインスタンス化
			 ProductDAO dao = new ProductDAO();) {
			// 手順-2. 全件表示
			List<Product> list = dao.findAll();
			Utils.showProductList(list);
			// 手順-3. 商品を新規登録するメッセージを表示
			Display.showMessageln("【商品登録 ft. ProductDAO】");
			// 手順-4. キーボードから登録する情報を取得（カテゴリID、商品名、価格、数量）
			int categoryId = Keyboard.getInputNumber("カテゴリID：");
			String name = Keyboard.getInputString("商品名：");
			int price = Keyboard.getInputNumber("価格：");
			int quantity = Keyboard.getInputNumber("数量：");
			// 手順-5. 入力値をもとに登録する商品をインスタンス化
			Product product = new Product(categoryId, name, price, quantity);
			// 手順-6. 商品インスタンスを登録
			dao.insert(product);
			// 手順-7. 商品リスト再取得
			list = dao.findAll();
			// 手順-8. 商品リストを表示
			System.out.println();
			Utils.showProductList(list);
			
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
			return;
		}

	}

}
