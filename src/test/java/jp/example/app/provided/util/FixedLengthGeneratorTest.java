package jp.example.app.provided.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jp.example.app.provided.util.helper.StringLengthHelper;

/**
 * FixedLengthGenerator#execute(String, int) の動作確認用テストクラス
 *  - 指定した文字数に収まるように入力文字列を加工する処理を確認する
 *  - 短い場合はスペースで埋める／長い場合は切り詰める／nullや空文字も対象
 *
 *  ※ 研修課題の解答コードの正しさを裏で検証する目的で使用する。
 */
class FixedLengthGeneratorTest {

	@Nested
	@DisplayName("FixedLengthGenerator#execute(String, int)メソッドのテストクラス")
	class ExecuteStringTest {
		@ParameterizedTest
		@MethodSource("dataProvider")
		@DisplayName("Test: 指定された文字数の文字列に変換できる")
		void test(StringLengthHelper target, String expected) {
			// execute
			String actual = FixedLengthGenerator.execute(target.getValue(), target.getLength());
			// verify
			assertEquals(expected, actual);
		}
		
		/**
		 * テストパラメータを提供する
		 * @return テストパラメータ群
		 */
		static Stream<Arguments> dataProvider() {
			// テストパラメータを返却
			return Stream.of(
						  // Test01 短い文字列: "abc" → "abc" + " ".repeat(7)
						  Arguments.of(new StringLengthHelper("abc", 10), "abc" + " ".repeat(7))
						  // Test02 同じ長さ: "watermelon" → "watermelon"
						, Arguments.of(new StringLengthHelper("watermelon", 10), "watermelon")
						  // Test03 長すぎ: "watermelonman" → "watermelon"
						, Arguments.of(new StringLengthHelper("watermelonman", 10), "watermelon")
						  // Test04 null → " ".repeat(10)
						, Arguments.of(new StringLengthHelper(null, 10), " ".repeat(10))
						  // Test05 空文字 → " ".repeat(10)
						, Arguments.of(new StringLengthHelper("", 10), " ".repeat(10))
					);
			
		}
		
	}

}
