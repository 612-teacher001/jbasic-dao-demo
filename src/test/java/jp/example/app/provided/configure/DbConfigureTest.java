package jp.example.app.provided.configure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.example.app.provided.configure.DbConfigure;

/**
 * DbConfigure クラスのテスト
 *  - テストコードの書き方やスタイルの違いを示すためのサンプルであり、チュートリアルの解説対象ではない。
 *  - これらのテストは Gradle/Maven の test 実行確認や JUnit の動作サンプルとして利用することを想定している。
 *  - 受講者はこのテストコードを修正・拡張する必要はない。
 */
class DbConfigureTest {

	/** テスト対象クラス：system under test */
	private DbConfigure sut; 
	
	@BeforeEach
	void setUp() throws Exception {
		sut = new DbConfigure();
	}

	@AfterEach
	void tearDown() throws Exception {
		// 現状では特にリソース解放の処理は不要
	    // もし sut がデータベース接続やファイルなどを扱う場合は、ここに close や cleanup 処理を書くことになる
	}

	/**
	 * 個々にプロパティ取得のテストを行うテストクラス
	 *  - メリット：テストに失敗した場合に、失敗したテストケースを特定しやすい
	 *  - デメリット：同じようなテストケースをプロパティの数だけ必要とするので冗長になりやすい
	 */
	@Nested
	@DisplayName("「1メソッド＝1アサーション」というシンプルなテストケース群")
	class PropertiesTest {
		@Test
		@DisplayName("Test01: プロパティキー「db.url」キーの値は「jdbc:postgresql://localhost:5432/jbasicdaodb」である")
		void test01() {
			// setup
			String expected = "jdbc:postgresql://localhost:5432/jbasicdaodb";
			// execute
			String actual = sut.getUrl();
			// verify
			assertEquals(expected, actual);
		}
		
		@Test
		@DisplayName("Test02: プロパティキー「db.username」の値は「student」である")
		void test02() {
			// setup
			String expected = "student";
			// execcute
			String actual = sut.getUser();
			// verify
			assertEquals(expected, actual);
		}
		
		@Test
		@DisplayName("Test03: プロパティキー「db.password」の値は「himitu」である")
		void test03() {
			// setup
			String expected = "himitu";
			// execute
			String actual = sut.getPassword();
			// verify
			assertEquals(expected, actual);
		}
	}
	
	/**
	 * まとめて検証するテストクラス
	 *  - メリット：一括したテストなので複数のテストケースに分かれることなく冗長になりにくい
	 *  - デメリット：テストに失敗した場合に、失敗したテストケースを突き止めにくい
	 */
	@Nested
	@DisplayName("「まとめて検証」テストクラス")
	class AllInOneTest {
		@Test
		@DisplayName("Test00: DbConfigureクラスのすべての設定キーを取得できる")
		void test() {
			// setup
			Map<String, String> expected = new HashMap<String, String>();
			expected.put("db.url", "jdbc:postgresql://localhost:5432/jbasicdaodb");
			expected.put("db.username", "student");
			expected.put("db.password", "himitu");
			
			// verify
			assertEquals(expected.get("db.url"), sut.getUrl());
			assertEquals(expected.get("db.username"), sut.getUser());
			assertEquals(expected.get("db.password"), sut.getPassword());
			
		}
	}
}
