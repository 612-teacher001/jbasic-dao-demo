package jp.example.app.provided.io;

/**
 * 引数に指定された文字列を標準出力に表示する出力クラス
 */
public class Display {

	/**
	 * 改行付きでメッセージを表示する
	 * @param message 表示するメッセージ
	 */
	public static void showMessageln(String message) {
		System.out.println(message);
	}
	
	/**
	 * 改行なしでメッセージを表示する
	 * @param message 表示するメッセージ
	 */
	public static void showMessage(String message) {
		System.out.print(message);
	}

}
