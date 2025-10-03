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
	private static final String SQL_FIND_BY_ID = "SELECT * FROM products WHERE id = ?";
	private static final String SQL_FIND_BY_NAME_LIKE = "SELECT * FROM products WHERE NAME LIKE ?";
	private static final String SQL_INSERT = "INSERT INTO products (category_id, name, price, quantity) VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";
	
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
	 * 指定された商品IDの商品を取得する
	 * @param targetId 検索する商品のID
	 * @return 検索する商品のIDと合致する商品が見つかった場合は商品インスタンス、それ以外はnull
	 * @throws SQLException 
	 */
	public Product findById(int targetId) throws SQLException {
		// 1. 戻り値の初期化
		Product product = null;
		try (// 2. SQL実行オブジェクトを取得
			 PreparedStatement  pstmt = this.conn.prepareStatement(SQL_FIND_BY_ID);) {
			// 3. プレースホルダをパラメータで置換
			pstmt.setInt(1, targetId);
			try (// 4. SQLの実行と結果セットの取得
				 ResultSet rs = pstmt.executeQuery();) {
				// 5. 結果セットを商品インスタンスに変換
				product = this.convertToProduct(rs);
			}
		}
		// 6. 戻り値の返却
		return product;
	}

	/**
	 * 指定されたキーワードが含まれる商品名の商品を取得する
	 * @param keyword 検索キーワード
	 * @return 商品リスト
	 * @throws SQLException
	 */
	public List<Product> findByNameLike(String keyword) throws SQLException {
		// 1. 戻り値の初期化
		List<Product> list = null;
		try (// 2. SQL実行オブジェクトの取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_FIND_BY_NAME_LIKE);) {
			// 3. プレースホルダをパラメータにで置換
			pstmt.setString(1, "%" + keyword + "%");
			// 4. SQLの実行と結果セットの取得
			try (// 5. 結果セットを商品リストに変換
				 ResultSet rs = pstmt.executeQuery();) {
				// 5. 結果セットを商品リストに変換
				list = this.convertToList(rs);
			}
		}
		// 戻り値の返却
		return list;
	}

	/**
	 * 商品を登録する
	 * @param product 登録対象商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	public void insert(Product product) throws SQLException {
		try (// 1. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_INSERT)) {
			// 2. プレースホルダをパラメータに置換
			pstmt.setInt(1, product.getCategoryId());
			pstmt.setString(2, product.getName());
			pstmt.setInt(3, product.getPrice());
			pstmt.setInt(4, product.getQuantity());
			// 3. SQSLの実行
			pstmt.executeUpdate();
		}
	}

	/**
	 * 商品を更新する
	 * @param product 更新対象商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	public void update(Product product) throws SQLException {
		try (// 1. SQL実行オブジェクトを取得
			 PreparedStatement pstmt = this.conn.prepareStatement(SQL_UPDATE);) {
			// 2. プレースホルダをパラメータに置換
			pstmt.setInt(1, product.getCategoryId());
			pstmt.setString(2, product.getName());
			pstmt.setInt(3, product.getPrice());
			pstmt.setInt(4, product.getQuantity());
			pstmt.setInt(5, product.getId());
			// 3. SQLの実行
			pstmt.executeUpdate();
		}
	}

	/**
	 * 結果セットから商品インスタンスに変換する
	 * 処理内容：
	 *   1. 結果セットの1行を読み込む
	 *   2. 商品の各フィールド値を取得
	 *   3. Product オブジェクトを作成
	 *   4. 作成した Product オブジェクトを返却
	 * 
	 * @param rs 結果セット
	 * @return 商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	private Product convertToProduct(ResultSet rs) throws SQLException {
		// 1. 戻り値の初期化
		Product product = null;
		// 2. 結果セットの現在行を商品インスタンスに変換：createProductメソッドの呼び出し
		if (rs.next()) {
			// 3. 現在の行の各フィールド値を取得
			product = this.createProduct(rs);
		}
		// 4. 戻り値の返却
		return product;
	}
	
	/**
	 * 結果セットから商品リストに変換する
	 * 処理内容：
	 *   1. 結果セットの各行を順に読み込む
	 *   2. 商品の各フィールド値を取得
	 *   3. Product オブジェクトを作成
	 *   4. 作成した Product オブジェクトをリストに追加
	 * 
	 * @param rs 結果セット
	 * @return 商品リスト
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	private List<Product> convertToList(ResultSet rs) throws SQLException {
		// 1. 戻り値の初期化
		List<Product> list = new ArrayList<>();
		// 2. 結果セットの１行ごとの読み込み
		while (rs.next()) {
			// 3. 現在の行の各フィールド値を取得
			Product product = this.createProduct(rs);
			list.add(product);
		}
		// 4. 戻り値の返却
		return list;
	}

	/**
	 * 結果リストの現在の行から商品インスタンスを生成する
	 * @param rs 現在の行を指し示す結果セット
	 * @return 商品インスタンス
	 * @throws SQLException 結果セット処理でエラーが発生した場合
	 */
	private Product createProduct(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");                  // 商品ID（id）
		int categoryId = rs.getInt("category_id"); // カテゴリID（categoryId）
		String name = rs.getString("name");        // 商品名（name）
		int price = rs.getInt("price");            // 価格（price）
		int quantity = rs.getInt("quantity");      // 数量（quantity）
		return new Product(id, categoryId, name, price, quantity);
	}

}
