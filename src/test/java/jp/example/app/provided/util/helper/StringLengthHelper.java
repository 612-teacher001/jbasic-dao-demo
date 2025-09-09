package jp.example.app.provided.util.helper;

public class StringLengthHelper {
	
	/**
	 * フィールド
	 */
	private String value; // 文字列リテラル
	private int length;   // 文字数
	
	/**
	 * コンストラクタ
	 * @param value  文字列リテラル
	 * @param length 文字数
	 */
	public StringLengthHelper(String value, int length) {
		this.value = value;
		this.length = length;
	}

	public String getValue() {
		return value;
	}

	public int getLength() {
		return length;
	}
	
}
