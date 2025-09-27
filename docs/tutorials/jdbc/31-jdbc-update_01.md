# *T1.13* JDBC接続によるCRUD操作・更新 ～ 商品を更新してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### 今回のチュートリアル対象

- コミット：[9c0a3c9](https://github.com/612-teacher001/jbasic-dao-demo/commit/9c0a3c9)
- クラス：[`jp.example.app.t1.JdbcUpdate`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcUpdate.java)

👉 チュートリアルの進め方については [チュートリアルガイド](../guidance.md) を参照してください。

## 1. 概要

このチュートリアルで扱うプログラムは、productsテーブルにレコードを更新して、標準出力に全件表示するというものです。

このチュートリアルでは、**商品を更新する方法** について解説します。

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

以下で解説する手順は `jp.example.app.t1.JdbcUpdate#main(String[])` メソッドのコードに記述されている「手順-X」と記述されたコメントに対応しています。  

今回のCRUD操作では大きく以下のような2つの操作を実行します：

1. 更新対象商品を取得する
2. 更新対象商品を更新する

これらはそれぞれデータベースにアクセスする必要があるので、ひとつのデータベース接続オブジェクトを取得してから個々の操作を実行するようにします。

### 手順-1. 全商品を表示する
- ここはこれまでのチュートリアルで扱ったものと同じです。
<!--
- 主キー検索のチュートリアルでの内容を同じです。[商品IDで検索してみた・基本形編](./12-jdbc-id_01.md) を参照してください。
-->

### 手順-2. 商品を新規登録するメッセージを表示する
```java
Display.showMessageln("【商品更新】");
```
- `Display#showMessageln(String)` メソッドを呼び出して、「【商品更新】」というかんたんなタイトルを表示します。
- `Display` クラスについては [Display クラス 利用ガイド](../../manuals/provided/display.md) を参照してください。

### 手順-3. キーボードから更新する商品のIDを取得する
```java
int targetId = Keyboard.getInputNumber("更新する商品のIDを入力してください：");
```
- `Keyboard#getInputNumber(String)` メソッドによって、更新する商品のIDの入力を変数に格納します。
- `Keyboard` クラスについては [Keyboard クラス 利用ガイド](../../manuals/provided/keyboard.md) を参照してください。

### 手順-4. データベース接続情報を取得
- ここはこれまでのチュートリアルで扱ったものと同じです。

### 手順-5. データベース接続オブジェクトを取得する
```java
String sql = "";
try (// 手順-５. データベース接続オブジェクトを取得（例外処理対象：リソース自動解放のしくみで管理される）
    Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());) {

  ...（中略）...      

} catch (SQLException e) {
  // 例外が発生した場合：スタックトレースを表示（必要最低限のエラー情報を表示）
  e.printStackTrace();
  return;
}
```
- 更新対象商品と商品の更新処理をひとつのデータベース接続オブジェクトで行うために、データベース接続オブジェクトをtry-with-resourcesで囲んでいます。

### 手順-6. 実行するSQLを設定する
- 主キー検索のチュートリアルでの内容を同じです。[商品IDで検索してみた・基本形編](./12-jdbc-id_01.md) を参照してください。

### 手順-7. SQL実行オブジェクトを取得する
```java
Product target = null;
try (// 手順-7. SQL実行オブジェクトを取得（例外処理対象）
    PreparedStatement pstmt = conn.prepareStatement(sql);) {

  ...（中略）...      

}
```
- ここでの例外はデータベース接続オブジェクトを取得するところで例外を捕捉するcatch文で捕捉します。
- 指定したIDの商品インスタンスを格納する変数 `target` は `null` で初期化します。

### 手順-8. SQLのプレースホルダを商品IDで置換する
### 手順-9. SQLの実行と結果セットを取得する
```java
// 手順-8. SQLのプレースホルダを商品IDで置換（例外処理対象）
pstmt.setInt(1, targetId);
try (// 手順-9. SQLの実行と結果セットの取得
    ResultSet rs = pstmt.executeQuery();) {

  ...（中略）...      

}
```
- 主キー検索のチュートリアルでの内容を同じです。[商品IDで検索してみた・基本形編](./12-jdbc-id_01.md) を参照してください。
- ここでの例外もデータベース接続オブジェクトを取得するところで例外を捕捉するcatch文で捕捉します。

### 手順-10. 結果セットから商品インスタンスに変換する
```java
// 手順-10. 結果セットから商品インスタンスに変換
List<Product> list = JdbcKeyword.convertToList(rs);
if (!list.isEmpty()) {
  target = list.get(0);
}
```
- 結果セットから商品リストに変換する処理は、すでにキーワード検索のチュートリアルで扱ったメソッドがあるので、そのメソッドを呼出します。
- ここでの検索は主キー検索なので取得した商品リストの要素数は高々1個なので、商品リストが空リストでない場合は要素のインスタンスを取得します。  
このとき、商品リストが空リストである場合は商品インスタンスは取得しないので、`target` は `null` のままになります。

### 手順-11. 商品の存在チェックをする
```java
if (target == null) {
  Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
  return;
}
```
- 指定されたIDの商品が存在しない場合は `target` は `null` のままなので、**`target` が `null` かどうか** で指定されたIDの商品があるかどうかを判定します。
- 商品が存在しなかった場合はメッセージを表示して、return文でプログラムを終了しています。

### 手順-12. 更新情報を取得する
```java
// 手順-12. 更新情報を取得（カテゴリID、商品名、価格、数量）
int categoryId = Keyboard.getInputNumber("カテゴリID（現：" + target.getCategoryId() + "）：");
String name = Keyboard.getInputString("商品名（現：" + target.getName() + "）：");
int price = Keyboard.getInputNumber("価格（現：" + target.getPrice() + "）：");
int quantity = Keyboard.getInputNumber("数量（現：" + target.getQuantity() + "）：");
```
- プロンプトの表示は「カテゴリID（現：3）：」のように、丸括弧内に現在の商品の値が表示されます。
- 商品IDは主キーになるため変更できないようにしています。

### 手順-13. 更新情報で更新対象商品を更新する
```java
target.setCategoryId(categoryId);
target.setName(name);
target.setPrice(price);
target.setQuantity(quantity);
```
- 取得した更新情報で現在の商品のインスタンスを上書きします。
上書きは、カテゴリID、商品名、価格、数量の4つの項目です。

### 手順-14. 実行するSQLを取得する
```java
sql = "UPDATE products SET category_id = ?, name = ?, price = ?, quantity = ? WHERE id = ?";
```
- 更新するのでUPDATE文を設定します。

### 手順-15. SQL実行オブジェクトを取得する
- 実行するSQLが異なるだけで、手順-7と同じ処理になります。

### 手順-16. SQLのプレースホルダを商品インスタンスのフィールド値で置換する
```java
pstmt.setInt(1, target.getCategoryId());
pstmt.setString(2, target.getName());
pstmt.setInt(3, target.getPrice());
pstmt.setInt(4, target.getQuantity());
pstmt.setInt(5, target.getId());
```
- 手順-8と同じ処理になります。

### 手順-17. SQLを実行する
```java
pstmt.executeUpdate();
```
- UPDATE分を実行するので `PreparedStatement#executeUpdate()` メソッドを呼び出して実行しています。  
`PreparedStatement#executeUpdate()` メソッドについては [全件検索の手順-5](./11-jdbc-all_01.md#rs_execute) を参照してください。

### 手順-18. 商品リストを表示する
- 区切りのから行の表示が前後逆になりますが、それ以外は手順-1と同じ処理になります。

## 4. まとめ

ここのコードで学ぶべきポイント：

  - UPDATE文の実行には `executeUpdate` メソッドの利用すること
  - プレースホルダに入力値をセットして安全に SQL を組み立てる方法
  - 共通処理を呼び出して処理を再利用する方法

---

## 実習問題

以下の課題を進めるにあたっては `jp.example.exercise.t1.update` パッケージの中にクラスを作成してください。  
なお、クラス名は `Exercise11` のように「Exercise + 課題番号」の形式にして `main` メソッドに処理を記述してください。  
たとえば、課題-11の場合は「`Exercise11`」というクラスを作成します。

**［課題-11］**  [データベース概要](../00-database.md) を参考にして、キーボードから取得した部署IDを元にdepartmentsテーブルの部署名を変更して全件表示するプログラムを作ってください。
このとき、`convertToList` および `showDepartmentList` メソッドを定義し呼び出してコードを簡略化してください（以下のリンク先を参考にしてください）。  
showProductList の構造を参考に、departments テーブル用に `showDepartmentList` メソッドを定義してください。

参考：convertToList, showProductList メソッドは [JdbcSelectAll](./11-jdbc-all_01.md) で定義しています

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

