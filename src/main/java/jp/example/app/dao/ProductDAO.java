package jp.example.app.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jp.example.app.bean.Product;

/**
 * productsテーブルにアクセスするDAO
 */
public class ProductDAO extends BaseDAO {

	/**
	 * クラス定数
	 */
	// SQL文字列群
	private static final String SQL_FIND_ALL = "SELECT * FROM products ORDER BY id";
	
	/**
	 * コンストラクタ：データベース接続オブジェクトを取得する
	 * @throws SQLException 
	 */
	public ProductDAO() throws SQLException {
		super();
	}

	/**
	 * すべての商品の商品リストを取得する
	 * 
	 * @return 商品リスト
	 * @throws SQLException 
	 */
	public List<Product> findAll() throws SQLException {
		// 1. 戻り値の初期化
		List<Product> list = null;
		try (// 2. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_FIND_ALL);
			 // 3. SQLの実行と結果セットの取得
			 ResultSet rs = pstmt.executeQuery();) {
			// 4. 結果セットから商品リストに変換
			list = this.convertToList(rs);
		}
		// 5. 戻り値の返却
		return list;
	}

	/**
	 * 結果セットから商品リストに変換する
	 * 処理内容：
	 *   1. 結果セットの各行を順に読み込む
	 *   2. 商品の各フィールド値を取得
	 *   3. Product オブジェクトを作成
	 *   4. 作成した Product をリストに追加
	 * 
	 * @param rs 結果セット
	 * @return 商品リスト
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	protected List<Product> convertToList(ResultSet rs) throws SQLException {
		// 1. 戻り値の初期化
		List<Product> list = new ArrayList<>();
		// 2. 結果セットの１行ごとの読み込み
		while (rs.next()) {
			// 3. 現在の行の各フィールド値を取得
			int id = rs.getInt("id");                 // 商品ID（id）
			int categoryId =rs.getInt("category_id"); // カテゴリID（categoryId）
			String name = rs.getString("name");       // 商品名（name）
			int price = rs.getInt("price");           // 価格（price）
			int quantity = rs.getInt("quantity");     // 数量（quantity）
			// 4. 取得したフィールド値での商品クラスのインスタンス化と商品リストへの追加を同時に実施
			list.add(new Product(id, categoryId, name, price, quantity));
		}
		// 5. 戻り値の返却
		return list;
	}

}
