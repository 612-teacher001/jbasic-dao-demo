package jp.example.app.provided.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * キーボードから入力された値を取り込む入力クラス
 * TODO: 仕様上、例外発出時は「System.exit(0)」で強制終了としているが、
 *       keyboardInputExcceptionのような独自例外を定義して、呼び出し元に戻すほうが実践的である。
 *       ちなみに、例外処理の解説としてちょうどいい話題。
 */
public class Keyboard {

	/**
	 * キーボードから入力された値を文字列として取得する
	 * @param msg 入力を促すメッセージ
	 * @return キーボードから入力された文字列
	 */
	public static String getInputString(String msg) {
		String input = null;
		try {
			System.out.print(msg);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			input = reader.readLine();
		} catch (IOException e) {
			// 入力時に発生する入出力例外
			// e.printStackTrace();
			System.err.println("キー入力時にエラーが発生しました。");
			System.exit(0);
		}
		return input;
	}

	/**
	 * キーボードから入力された値を整数として取得する
	 * @param msg 入力を促すメッセージ
	 * @return キーボードから入力された整数
	 */
	public static int getInputNumber(String msg) {
		Integer input = null;
		try {
			System.out.print(msg);
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			input = Integer.parseInt(reader.readLine());
		} catch (IOException e) {
			// 入力時に発生する入出力例外
			// e.printStackTrace();
			System.err.println("キー入力時にエラーが発生しました。");
			System.exit(0);
		} catch (NumberFormatException e) {
			// 入力された文字列の整数への変換時に発生する数値変換例外
			// e.printStackTrace();
			System.err.println("整数にキー入力時にエラーが発生しました。");
			System.exit(0);
		}
		return input;
	}

}
