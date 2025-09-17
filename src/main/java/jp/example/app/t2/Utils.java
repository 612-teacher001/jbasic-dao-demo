package jp.example.app.t2;

import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.provided.io.Display;

public class Utils {
	
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
	public static void showProducts(List<Product> list) {
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
