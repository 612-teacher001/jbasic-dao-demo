package jp.example.app.t1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jp.example.app.bean.Product;
import jp.example.app.provided.configure.DbConfigure;
import jp.example.app.provided.io.Keyboard;

/**
 * データベースに商品を新規登録するプログラム
 */
public class JdbcInsert {

	/**
     * キーボードから入力された情報をもとに products テーブルへ新しい商品を登録する
     *
     * 特徴:
     *  - ユーザはキーボードからカテゴリID・商品名・価格・数量を入力する
     *  - 入力値をもとに Product インスタンスを生成する
     *  - INSERT 文を実行して products テーブルに商品を登録する
     *  - 登録後に再度商品一覧を表示し、登録結果を確認できるようにする
     *  - SQLException が発生した場合はスタックトレースを出力する
     *  - 入力値チェックは不要
     *
     * @param args 未使用
     */
	public static void main(String[] args) {
		// 手順-0. 商品一覧を表示（前提処理：検索対象を選びやすくするため）
		// ※参考：main メソッドを呼び出して処理を再利用する例
		JdbcAll.main(new String[] {});
		System.out.println();
		// 手順-1. キーボードから入力された商品の情報を取得
		int categoryId = Keyboard.getInputNumber("追加する商品のカテゴリID：");
		String name = Keyboard.getInputString("追加する商品の商品名：");
		int price = Keyboard.getInputNumber("追加する商品の価格：");
		int quantity = Keyboard.getInputNumber("追加する商品の数量：");
		// 手順-2. 入力値をもとに商品をインスタンス化
		Product product = new Product(categoryId, name, price, quantity);
		// 手順-3. 実行するSQLを設定
		String sql = "INSERT INTO products (category_id, name, price, quantity) VALUES (?, ?, ?, ?)";
		// 手順-4. データベース接続情報を取得
		DbConfigure configure = new DbConfigure();
		// 手順-5. データベース接続関連オブジェクトを取得
		try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
			 PreparedStatement pstmt = conn.prepareStatement(sql);
			) {
			// 手順-6. プレースホルダを商品インスタンスで置換
			pstmt.setInt(1, product.getCategoryId());
			pstmt.setString(2, product.getName());
			pstmt.setInt(3, product.getPrice());
			pstmt.setInt(4, product.getQuantity());
			// 手順-7. SQLを実行
			pstmt.executeUpdate();
			// 手順-0. 商品一覧を表示（参考処理：SQLが成功したかどうかを確認するため）
			System.out.println("");
			JdbcAll.main(new String[] {});
		} catch (SQLException e) {
			// 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
			e.printStackTrace();
		}
	}

}
