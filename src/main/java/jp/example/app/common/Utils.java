package jp.example.app.common;

import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.provided.io.Display;

/**
 * 共通ユーティリティクラス
 */
public class Utils {
	/**
	 * 商品リストを表示する
	 * @param productList 表示する商品リスト
	 */
	public static void showProductList(List<Product> productList) {
		// 件数の表示
		Display.showMessageln("検索結果：" + productList.size() + "件の商品が見つかりました。");
		// 見出し行の表示
		System.out.println(); // 区切り用の空行
		System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
						  "商品ID", "カテゴリID", "商品名", "価格", "数量");
		// 商品リストの表示
		for (Product product : productList) {
			System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
					product.getId(),
					product.getCategoryId(),
					product.getName(),
					product.getPrice(),
					product.getQuantity()
				 );
		}
	}

	/**
	 * 商品インスタンスを表示する
	 * @param product 表示する商品インスタンス
	 */
	public static void showProduct(Product product) {
		// 件数の表示
		Display.showMessageln("検索結果：1件の商品が見つかりました。");
		// 見出し行の表示
		System.out.println(); // 区切り用の空行
		System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
						  "商品ID", "カテゴリID", "商品名", "価格", "数量");
		System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
				product.getId(),
				product.getCategoryId(),
				product.getName(),
				product.getPrice(),
				product.getQuantity()
			 );

	}

}
