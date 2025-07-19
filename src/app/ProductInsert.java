package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import app.entity.Product;

public class ProductInsert {

	/**
	 * クラス定数：データベース接続情報文字列定数群
	 */
	private static final String DB_URL      = "jdbc:postgresql://localhost:5432/jbasicdaodb";
	private static final String DB_USER     = "student";
	private static final String DB_PASSWORD = "himitu";
	
	/**
	 * productsテーブルに商品を新規追加する
	 * 【データベースへの接続手順】try-with-resourcesを利用した例
	 *   0. 新規商品の情報（商品名・価格・在庫数）を取得
	 *   1. 実行するSQLの設定
	 *   2. データベースに接続：データベース接続オブジェクトの取得
	 *   3. SQL実行オブジェクトの取得
	 *   4. プレースホルダをパラメータで置換
	 *   5. SQLの実行
	 *   6. すべての商品リストを表示（追加の確認）
	 */
	public static void main(String[] args) {
		// 0. 新規商品の情報（商品名・価格・在庫数）を取得
		Product target = null;
		try (Scanner scanner = new Scanner(System.in);) {
			System.out.println("新規追加する商品を入力してください。");
			System.out.print("商品名：");
			String name = scanner.next();
			System.out.print("価格：");
			int price = scanner.nextInt();
			System.out.print("在庫数：");
			int stock = scanner.nextInt();
			// 新規登録する商品のインスタンス化
			target = new Product(name, price, stock);
		}
		
		// 1. 実行するSQLの設定
		String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
		
		try (// 2. データベースに接続：データベース接続オブジェクトの取得
			 Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
			 // 3. SQL実行オブジェクトの取得
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			
			// 4. プレースホルダをパラメータで置換
			pstmt.setString(1, target.getName());
			pstmt.setInt(2, target.getPrice());
			pstmt.setInt(3, target.getStock());
			
			// 5. SQLの実行
			pstmt.executeUpdate();
			
			// 6. すべての商品リストを表示
			System.out.println("");
			ProductListDemo.main(null);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
