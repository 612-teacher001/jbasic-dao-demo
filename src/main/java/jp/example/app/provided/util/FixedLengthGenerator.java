package jp.example.app.provided.util;

/**
 * 指定された文字数の文字列に変換するユーティリティクラス
 */
public class FixedLengthGenerator {
	
	/**
	 * 指定された文字数の文字列に変換する
	 * @param target 変換対象文字列
	 * @param length 変換後の文字数
	 * @return 指定された文字数の文字列
	 * @throws IllegalArgumentException 引数lengthが負数の場合
	 */
	public static String execute(String target, int length) {
		// 引数の妥当性検査
	    if (target == null) {
	    	// 変換対象文字列がnullの場合：空文字に置き換える
	        target = "";
	    }
	    if (length < 0) {
	    	// 変換後の文字数に負数が指定された場合：不正な引数としてスロー
	    	throw new IllegalArgumentException("length must be non-negative");
	    }
	    if (target.length() > length) {
	    	// 文字列の文字数が指定文字数より長い場合：切り詰めて返却
	        return target.substring(0, length); // 長い場合は切り詰め
	    } else {
	    	// 短い場合：左寄せで右側をスペースで埋める
	        return String.format("%-" + length + "s", target); // 短い場合は右側をスペースで埋める
	    }
	}

	/**
	 * 指定された文字数の数字列に変換する
	 * @param target 変換対象整数
	 * @param length 変換後の文字数
	 * @return 指定された文字数の数字列
	 * @throws IllegalArgumentException 引数lengthが負数の場合
	 */
	public static String execute(int target, int length) {
		return execute(Integer.toString(target), length);
	}
	
	

}
