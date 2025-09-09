package jp.example.app.provided.configure;

/**
 * データベース接続情報を読み込むクラス
 */
public class DbConfigure {

	/**
	 * クラス定数：プロパティファイルパス
	 */
	// データベース設定ファイルパス定数
	private static final String DB_PROPERTIES = "/db.properties";
	
	// プロパティキー文字列定数群
	private static final String KEY_DB_URL = "db.url";
	private static final String KEY_DB_USERNAME = "db.username";
	private static final String KEY_DB_PASSWORD = "db.password"; 
	
	/**
	 * フィールド
	 */
	private final PropertyLoader loader;
	
	/**
	 * 引数なしコンストラクタ
	 */
	public DbConfigure() {
		this.loader = PropertyLoader.getInstance(DB_PROPERTIES);
	}

	public String getUrl() {
		return this.loader.getString(KEY_DB_URL);
	}

	public String getUser() {
		return this.loader.getString(KEY_DB_USERNAME);
	}

	public String getPassword() {
		return this.loader.getString(KEY_DB_PASSWORD);
	}

}
