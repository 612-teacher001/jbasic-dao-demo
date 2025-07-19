package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import app.entity.Product;

public class ProductKeywordSearch {

	/**
	 * クラス定数：データベース接続情報文字列定数群
	 */
	private static final String DB_URL      = "jdbc:postgresql://localhost:5432/jbasicdaodb";
	private static final String DB_USER     = "student";
	private static final String DB_PASSWORD = "himitu";
	
	/**
	 * productsテーブルのキーワード検索結果を表示する
	 * 【データベースへの接続手順】try-with-resourcesを利用した例
	 *   0. キーワードを取得
	 *   1. 実行するSQLの設定
	 *   2. ［削除］データベース接続関連オブジェクトの初期化
	 *   3. データベースに接続：データベース接続オブジェクトの取得
	 *   4. SQL実行オブジェクトの取得
	 *   5. プレースホルダをパラメータで置換
	 *   6. SQLの実行と結果セットの取得
	 *   7. 結果セットを商品リストに変換
	 *   8. ［削除］データベース接続関連オブジェクトの解放
	 *   9. 商品リストを表示
	 * @param args
	 */
	public static void main(String[] args) {
		// 0. 検索する商品の商品IDを取得
		Scanner scanner = new Scanner(System.in);
		System.out.print("検索キーワード：");
		String keyword = scanner.next();
		scanner.close();
		
		// 1. 実行するSQLの設定
		String sql = "SELECT * FROM products WHERE name LIKE ?";
		
		try (// 3. データベースに接続：データベース接続オブジェクトの取得
			 Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 // 4. SQL実行オブジェクトの取得
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			
			// 5. プレースホルダをパラメータで置換
			pstmt.setString(1, "%" + keyword + "%");
			try (// 6. SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();
				) {
				// 7. 結果セットを商品リストに変換
				List<Product> list = new ArrayList<Product>();
				while (rs.next()) {
					Product entity = new Product();
					entity.setId(rs.getInt("id"));
					entity.setName(rs.getString("name"));
					entity.setPrice(rs.getInt("price"));
					entity.setStock(rs.getInt("stock"));
					list.add(entity);
				}
				
				// 9. 商品リストを表示
				if (list.size() == 0) {
					System.out.println("\n該当する商品は見つかりませんでした。");
				} else {
					System.out.println("\n該当する商品は" + list.size() + "件見つかりました。");
					System.out.printf("%-6s\t%-10s\t%-6s\t%-3s\n", "商品ID", "商品名", "価格", "在庫数");
					for (Product product : list) {
						System.out.printf("%-6s\t%-10s\t%-6s\t%-3s\n", 
								product.getId(),
								product.getName(),
								product.getPrice(),
								product.getStock());
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
