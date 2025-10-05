package jp.example.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.example.app.bean.ProductView;

public class ProductJoinDAO extends BaseDAO {

	/**
	 * クラス定数
	 */
	// SQL文字列定数群
	private static final String SQL_JOIN = """
		SELECT 
			  product.id AS id
			, product.category_id AS category_id
			, category.name AS category_name
			, product.name AS product_name
			, product.price AS price
			, product.quantity AS quantity
		FROM
			products product
			JOIN categories category
			ON product.category_id = category.id
		ORDER BY product.id;
		""";
	
	/**
	 * コンストラクタ：データベース接続オブジェクトを取得する
	 * @throws SQLException 
	 */
	public ProductJoinDAO() throws SQLException {
		super();
	}

	/**
	 * すべての商品を商品カテゴリ名を追加して表示する
	 * @return 商品カテゴリを追加した商品を要素とする商品リスト
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	public List<ProductView> findAll() throws SQLException {
		
		try (// 1. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_JOIN);
			 // 2. SQLの実行と結果セットの取得
			 ResultSet rs = pstmt.executeQuery();) {
			// 3. 結果セットから商品リストに変換
			List<ProductView> productList = new ArrayList<>();
			while (rs.next()) {
				// 現在行の結果セットから商品をインスタンス化
				ProductView product = new ProductView(
											  rs.getInt("id"),
											  rs.getInt("category_id"),
											  rs.getString("category_name"),
											  rs.getString("product_name"),
											  rs.getInt("price"),
											  rs.getInt("quantity")
											 );
				productList.add(product);
			}
			// 4. 戻り値を返却
			return productList;
		}
	}

}
