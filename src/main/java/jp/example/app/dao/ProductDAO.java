package jp.example.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.example.app.bean.Product;
import jp.example.app.provided.configure.DbConfigure;

/**
 * productsテーブルにアクセスするDAOクラス
 */
public class ProductDAO {

	/**
	 * クラス定数
	 */
	// SQL文字列定数群
	private static final String SQL_FIND_ALL = "SELECT * FROM products ORDER BY id";
	private static final String SQL_FIND_BY_ID = "SELECT * FROM products WHERE id = ?";
	private static final String SQL_FIND_BY_NAME_LIKE_KEYWORD = "SELECT * FROM products WHERE name LIKE ? ORDER BY id";
	private static final String SQL_UPDATE = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";
	
	/**
	 * フィールド：データベース接続オブジェクト
	 */
	private Connection conn;
	
	/**
	 * 引数なしコンストラクタ
	 * @throws SQLException 
	 */
	public ProductDAO() throws SQLException {
		getConnection();
	}

	/**
	 * データベース接続情報をもとにデータベースに接続する
	 * @throws SQLException
	 */
	private void getConnection() throws SQLException {
		// データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		this.conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
	}
	
	/**
	 * データベース接続オブジェクトの破棄
	 * @throws SQLException
	 */
	public  void close() throws SQLException {
		if (this.conn != null) {
			this.conn.close();
		}
	}

	/**
	 * すべての商品を取得する
	 * @return 商品リスト
	 * @throws SQLException
	 */
	public List<Product> findAll() throws SQLException {
		
		try (// SQL実行オブジェクトの取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_FIND_ALL);
			 // SQLの実行と結果セットの取得
			 ResultSet rs = pstmt.executeQuery();
			) {
			// 結果セットを商品リストに変換
			List<Product> list = this.convertToList(rs);
			
			return list;
		}
	}
	
	/**
	 * 指定された商品IDに合致した商品を取得する
	 * @param targetId 指定する商品ID
	 * @return 商品IDに合致した商品がある場合はその商品インスタンス、それ以外はnull
	 * @throws SQLException 
	 */
	public Product findById(int targetId) throws SQLException {
		
		// 戻り値を宣言
		Product bean = null;
		try (// SQL実行オブジェクトの取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_FIND_BY_ID);
			) {
			// プレースホルダをパラメータに置換
			pstmt.setInt(1, targetId);
			try (// SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();
				) {
				// 結果セットを商品リストに変換
				bean = this.convertToBean(rs);
			}
			
		}
		return bean;
	}
	
	/**
	 * キーワードを含む商品名の商品を取得する
	 * @param keyword 検索キーワード
	 * @return 商品リスト
	 * @throws SQLException
	 */
	public List<Product> findByNameLikeKeyword(String keyword) throws SQLException {
		
		// 戻り値を宣言
		List<Product> list = null;
		try (// SQL実行オブジェクトの取得
				 PreparedStatement pstmt = this.conn.prepareStatement(SQL_FIND_BY_NAME_LIKE_KEYWORD);
			) {
				// プレースホルダをパラメータに置換
				pstmt.setString(1, "%" + keyword + "%");
				try (// SQLの実行と結果セットの取得
					 ResultSet rs = pstmt.executeQuery();
					) {
					// 結果セットを商品リストに変換
					list = this.convertToList(rs);
				}
			}
		return list;
	}

	/**
	 * 指定した商品インスタンスで更新する
	 * @param target 更新対象の商品インスタンス
	 * @throws SQLException
	 */
	public void update(Product target) throws SQLException {
		
		try (// SQL実行オブジェクトの取得
				 PreparedStatement pstmt = this.conn.prepareStatement(SQL_UPDATE);
			) {
				// プレースホルダをパラメータに置換
				pstmt.setInt(1, target.getCategoryId());
				pstmt.setString(2, target.getName());
				pstmt.setInt(3, target.getPrice());
				pstmt.setInt(4, target.getQuantity());
				pstmt.setInt(5, target.getId());
				// SQLの実行
				pstmt.executeUpdate();
		}
	}


	/**
	 * 結果セットの行を Product オブジェクトに変換し、インスタンスとして返す。
	 * 
	 * @param rs SQL実行結果の ResultSet（商品情報が格納されていることを前提とする）
	 * @return   商品インスタンス（nullの場合もある）
	 * @throws SQLException 結果セットから値を取得する際にエラーが発生した場合
	 */
	private Product convertToBean(ResultSet rs) throws SQLException {
		Product bean = null;
		if (rs.next()) {
			bean = new Product();
			int id = rs.getInt("id");
			int categoryId = rs.getInt("category_id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			int quantity = rs.getInt("quantity");
			bean = new Product(id, categoryId, name, price, quantity);
		}
		return bean;
	}

	/**
	 * 結果セットの各行を Product オブジェクトに変換し、リストとして返す。
	 * 
	 * @param rs SQL実行結果の ResultSet（商品情報が格納されていることを前提とする）
	 * @return   商品のリスト（空リストの場合もある）
	 * @throws SQLException 結果セットから値を取得する際にエラーが発生した場合
	 */
	private List<Product> convertToList(ResultSet rs) throws SQLException {
		List<Product> list = new ArrayList<Product>();
		while (rs.next()) {
			int id = rs.getInt("id");
			int categoryId = rs.getInt("category_id");
			String name = rs.getString("name");
			int price = rs.getInt("price");
			int quantity = rs.getInt("quantity");
			list.add(new Product(id, categoryId, name, price, quantity));
		}
		return list;
	}

}
