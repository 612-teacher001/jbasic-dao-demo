package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import app.entity.Product;

public class ProductPrimaryKeySearchDemo {

	/**
	 * クラス定数：データベース接続情報文字列定数群
	 */
	private static final String DB_URL      = "jdbc:postgresql://localhost:5432/jbasicdaodb";
	private static final String DB_USER     = "student";
	private static final String DB_PASSWORD = "himitu";
	
	/**
	 * productsテーブルの全件検索結果を表示する
	 * 【データベースへの接続手順】
	 *   1. 実行するSQLの設定
	 *   2. データベース接続関連オブジェクトの初期化
	 *   3. データベースに接続：データベース接続オブジェクトの取得
	 *   4. SQL実行オブジェクトの取得
	 *   5. プレースホルダをパラメータで置換
	 *   6. SQLの実行と結果セットの取得
	 *   7. 結果セットを商品インスタンスを取得
	 *   8. データベース接続関連オブジェクトの解放
	 *   9. 商品リストを表示
	 * @param args
	 */
	public static void main(String[] args) {
		
		// 0. 検索する商品の商品IDを取得
		Scanner scanner = new Scanner(System.in);
		System.out.print("商品ID：");
		int id = scanner.nextInt();
		scanner.close();
		
		// 1. 実行するSQLの設定
		String sql = "SELECT * FROM products WHERE id = ?";
		// 2. データベース接続関連オブジェクトの初期化
		Connection conn = null; // データベース接続オブジェクト：
		PreparedStatement pstmt = null; // SQL実行オブジェクト
		ResultSet rs = null; // 結果セット：SQLの実行結果を格納するオブジェクト
		try {
			// 3. データベースに接続：データベース接続オブジェクトの取得
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			// 4. SQL実行オブジェクトの取得
			pstmt = conn.prepareStatement(sql);
			// 5. プレースホルダをパラメータで置換
			pstmt.setInt(1, id);
			// 6. SQLの実行と結果セットの取得
			rs = pstmt.executeQuery();
			
			// 7. 結果リストから商品インスタンスを取得
			Product product = null;
			if (rs.next()) {
				product = new Product();
				product.setId(rs.getInt("id"));
				product.setName(rs.getString("name"));
				product.setPrice(rs.getInt("price"));
				product.setStock(rs.getInt("stock"));
			}
			
			// 9. 商品リストの表示
			if (product == null) {
				System.out.println("\n指定した商品IDの商品は見つかりませんでした。");
			} else {
				System.out.println("\n指定した商品IDの商品が見つかりました。");
				System.out.printf("%-6s\t%-10s\t%-6s\t%-3s\n", "商品ID", "商品名", "価格", "在庫数");
				System.out.printf("%-6s\t%-10s\t%-6s\t%-3s\n", 
							product.getId(),
							product.getName(),
							product.getPrice(),
							product.getStock());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 8. データベース接続関連オブジェクトの解放
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (pstmt != null) {
					pstmt.close();
					pstmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
