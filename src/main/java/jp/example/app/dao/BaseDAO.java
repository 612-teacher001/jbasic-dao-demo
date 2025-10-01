package jp.example.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jp.example.app.provided.configure.DbConfigure;

/**
 * productsテーブルにアクセスするDAO
 */
public class BaseDAO implements AutoCloseable {

	/**
	 * クラス定数
	 */
	// データベース接続情報
	private static final DbConfigure DB_CONFIG = new DbConfigure();
	
	/**
	 * フィールド：データベース接続オブジェクト
	 */
	protected Connection conn;
	
	/**
	 * コンストラクタ：データベース接続オブジェクトを取得する
	 * @throws SQLException 
	 */
	public BaseDAO() throws SQLException {
		getConnection();
	}

	/**
	 * データベース接続オブジェクトを取得する
	 * @throws SQLException
	 */
	private void getConnection() throws SQLException {
		this.conn = DriverManager.getConnection(
									DB_CONFIG.getUrl(),       // 接続URL
									DB_CONFIG.getUser(),      // アクセスユーザ名
									DB_CONFIG.getPassword()); // アクセスパスワード
	}
	
	/**
	 * データベース接続オブジェクトを破棄する
	 * @throws SQLException
	 */
	@Override
	public void close() throws SQLException {
		if (this.conn != null) {
			this.conn.close();
		}
	}
	
}
