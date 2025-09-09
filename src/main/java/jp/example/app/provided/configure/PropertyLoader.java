package jp.example.app.provided.configure;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * プロパティファイルを読み込むクラス
 */
public class PropertyLoader {
	
	/**
	 * クラス定数
	 */
	private static final Map<String, PropertyLoader> CACHE = new ConcurrentHashMap<String, PropertyLoader>();
	
	/**
	 * フィールド
	 */
	private final Properties properties = new Properties();
	
	/**
	 * コンストラクタ
	 * @param propertyFileName 読み込み対象のプロパティファイルのファイルパス
	 */
	private PropertyLoader(String propertyFileName) {
		try (InputStream in = getClass().getResourceAsStream(propertyFileName)) {
			if (in == null) {
				// プロパティファイルが見つからない場合：IllegalArgumentException（不正な引数例外）をスロー
				throw new IllegalArgumentException("プロパティファイルが見つかりませんでした：" + propertyFileName);
			}
			// プロパティファイルの読み込み
			properties.load(in);
		} catch (IOException e) {
			// 入出力エラーの場合：実行時エラーとしてスロー
			throw new RuntimeException("プロパティファイルの読み込みに失敗しました：" + e.getMessage());
		}
	}
	
	/**
	 * インスタンス取得（キャッシュ利用）
	 * @param fileName プロパティファイル名
	 * @return PropertyLoaderのインスタンス
	 */
	public static PropertyLoader getInstance(String fileName) {
		return CACHE.computeIfAbsent(fileName, PropertyLoader::new);
	}

	/**
	 * 指定されたキーに対応する値を取得する
	 * @param key 取得する値のキー
	 * @return キーに対応した値
	 */
	public String getString(String key) {
		String value = this.properties.getProperty(key);
		if (value == null) {
			throw new IllegalStateException("キーが存在しません：" + key);
		}
		return value;
	}
	
}
