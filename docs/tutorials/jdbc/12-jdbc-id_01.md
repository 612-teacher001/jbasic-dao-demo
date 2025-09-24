# *T1.12* JDBC接続によるCRUD操作・主キー検索 ～ 商品IDで検索してみた・基本形編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### 今回のチュートリアル対象

- コミット：[900d1aa](https://github.com/612-teacher001/jbasic-dao-demo/commit/900d1aa)
- クラス：[`jp.example.app.t1.JdbcId`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcId.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

## 1. 概要

このチュートリアルで扱うプログラムは、productsテーブルから商品ID検索によりレコードを取得して、標準出力に表示するというものです。

このチュートリアルでは、**JDBCの基本の流れ（入力 → 処理 → 表示）を実現する方法** について解説します。

処理結果の表示仕様は以下のとおりです：

  - **取得順序**：商品インスタンスなので指定なし
  - **出力形式**：商品ID、カテゴリID、商品名、価格、数量
  - **例外処理**：SQL実行中にエラーが発生した場合にはスタックトレースを表示

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

以下で解説する手順は `jp.example.app.t1.Jdbcid#main(String[])` メソッドのコードに記述されている「手順-X」と記述されたコメントに対応しています。  
実際にコードを確認しながら読み進めてください。

### 手順-1. 全商品を表示する
```java
JdbcAll.main(new String[] {});
System.out.println();
```
- 全商品を表示する処理は `JdbcAll` クラスで実行できるので、`JdbcAll.main()` メソッドを呼び出します。  
このチュートリアルですでに説明済みのプログラムが使用できる場合はこのように呼び出して使用します。

- 2行目の `System.out.println();` は以下に続く表示メッセージとの区切りとして空行を出力しています。

### 手順-2. キーボードから入力された商品IDを取得する
```java
int targetId = Keyboard.getInputNumber("検索する商品のIDを入力してください：");
```
- `Keyboard#getInputNumber(String)' メソッドを呼び出して、キーボードからの入力を変数に格納します。

- `Keyboard` クラスについては [Keyboard クラス 利用ガイド](../../manuals/provided/keyboard.md) を参照してください。

### 手順-3. 実行するSQLを設定する
```java
String sql = "SELECT * FROM products WHERE id = ?";
```
- ここでは入力された商品IDを条件として検索するので `WHERE` 句が必要になります。

- このSQLでは「`id = ?`」のような見慣れない `WHERE` 句になっています。  
一般に検索条件は「`id = 3`」のように具体的な商品IDが設定されますが、 実際には検索のたびに商品IDが同じとは限りません。  
そのため、商品IDをあらかじめ決めておくことは非現実的です。そこで、検索ごとに変化する商品IDを `?` で代表させておきます。  
このような `?` を **プレースホルダ** といい、プレースホルダ `?` を含むSQLを **プレースホルダ付きSQL** といいます。

- プレースホルダ付きSQLは、**SQLの実行前にプレースホルダ `?` を具体的な値に置換** してから実行されます。

### 手順-4. データベース接続情報を取得する
```java
DbConfigure configure = new DbConfigure();
```
- ここはこれまでのチュートリアルで扱ったものと同じです。

### 手順-5. データベース接続オブエジェクトを取得する
### 手順-6. SQL実行オブジェクトを取得する
```java
try (// 手順-5. データベース接続オブエジェクトを取得（例外処理と連動）
    Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
    // 手順-6. SQL実行オブジェクトを取得（例外処理と連動）
    PreparedStatement pstmt = conn.prepareStatement(sql);
  ) {

    ...（中略）

} catch (SQLException e) {
  // 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
  e.printStackTrace();
}
```
- `try-with-resources` を利用してデータベース接続オブエジェクトとSQL実行オブジェクトを取得しています。  
このようにすることで、これらのオブジェクトの破棄を省略できます。

- データベース接続関連オブジェクトについては、チェック例外である `SQLException` が発生する可能性があるので `catch` で捕捉します。  
このとき、`try-with-resources` の `try` は `try-catch` 文と同じように扱うことができます。

- このチュートリアルでは例外処理はスタックトレースを表示するだけにしていますが、実務ではログを出力することが多くなります。  
また、try-with-resources を使うことで、データベース接続やSQL実行オブジェクトが自動で閉じられ、データベース接続関連オブジェクトなどのリソース管理が安全かつ簡潔になります。

### 手順-7. SQLのプレースホルダを取得した商品IDで置換する
```java
pstmt.setInt(1, targetId);
```
- 実行するSQLはSQL実行オブジェクトに格納されているので、`PreparedStatement#setInt(int, int)` メソッドを呼び出してプレースホルダ `?` を具体的な商品IDに置換しています。

- 置換する値が文字列の場合は `setString`メソッド、整数の場合は `setInt` メソッドのようにデータ型別に置換するメソッドが用意されています。

- これらのメソッドの第一引数はプレースホルダ `?` の位置を序数で指定します。最初のプレースホルダであれば「1」になります。  
また第二引数は具体的な値を変数またはリテラルで指定します。

- このサンプルでは「1番目のプレースホルダを変数 `targetId` に代入されている商品IDで置換する」ということになります。

###  手順-8. SQLの実行と結果セットを取得する
```java
try (ResultSet rs = pstmt.executeQuery();) {

    ...（中略）

}
```
- `PreparedStatement#executeQuery()` メソッドを呼び出すと `SQLExcception` が発生する可能性があるので捕捉する必要があります。  
しかし、すでにデータベース接続オブエジェクトとSQL実行オブジェクトを取得するコードに対応した `catch` ブロックが設定されているので、そのブロックにで捕捉するようにします。 

### 手順-9. 結果セットから商品インスタンスに変換する
```java
Product product = null;
if (rs.next()) {
	int id = rs.getInt("id");
	int categoryId = rs.getInt("category_id");
	String name  = rs.getString("name");
	int price = rs.getInt("price");
	int quantity = rs.getInt("quantity");
	product = new Product(id, categoryId, name, price, quantity);
}
```
- 結果セットには主キー検索の結果が格納されているので、結果の件数は **必ず1件または0件** です。  
したがって、`while (ra.next())` ではなく `if (rs.next())` を利用しています。

- 結果セットから取得する商品インスタンスは `null` で初期化しておきます。  
この初期化によって、商品インスタンスが見つからなかった場合は `null` のままなので、商品が見つかったかどうかを判断することがでできます。  

### 手順-10. 商品インスタンスをチェックする
```java
if (product == null) {
	Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
	return;
}
```
- 手順-9で説明した `null` での初期化をここで判定しています。  
商品が見つからない場合は `null` なので、メッセージを表示してプログラムを中断しています。

- 戻り値のない `return` はプログラムを終了する場合に使用します。
`System.exit(0)` としても同じことになります。

### 手順-11. 商品インスタンスを表示する
```java
// 見出し行の表示
System.out.println(); // 区切り用の空行
System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
					"商品ID", "カテゴリID", "商品名", "価格", "数量");
// 商品インスタンスの表示
System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
		product.getId(),
		product.getCategoryId(),
		product.getName(),
		product.getPrice(),
		product.getQuantity()
		);
```
- 全件検索のチュートリアルでは商品リストを表示していましたが、拡張for文のブロック内の処理と同じ処理になります。


## 4. まとめ

ここのコードで学ぶべきポイント：

  - SQLの実行手順（接続 → 実行 → 結果取得）
  - `ResultSet` からインスタンスへの変換方法
  - スタックトレース表示による簡易例外処理と、リソース自動解放のしくみ

### 残された問題

このチュートリアルでは以下の問題が残されています：

  - try-with-resourcesブロック内で商品インスタンスの表示を実行しているので、データベース接続関連オブジェクトが表示処理中も保持されたままである

この問題については、次のチュートリアルで取り上げます。

---

## 実習問題

以下の課題を進めるにあたっては `jp.example.exercise.t1.id` パッケージの中にクラスを作成してください。  
なお、クラス名は `Exercise11` のように「Exercise + 課題番号」の形式にして `main` メソッドに処理を記述してください。  
たとえば、課題-11の場合は「`Exercise11`」というクラスを作成します。

**［課題-11］** [データベース概要](../00-database.md) を参考にして、キーボードから入力された担当者IDをもとに主キー検索をした結果を表示するプログラムを作ってください。

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

