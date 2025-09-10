# *T1.2* JDBC接続によるCRUD操作・主キー検索 ～ 商品IDを指定して検索してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### チュートリアルの対象

  - 対象コミット：[8939209](https://github.com/612-teacher001/jbasic-dao-demo/commit/8939209)
  - 対象クラス：[`jp.example.app.t1.JdbcId`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcId.java)



## 1. 概要

このチュートリアルでは、条件検索をどう実現するかという方法を説明します。  

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcId#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-1. キーボードからの入力を取得する
- `Keyboard#getInputNumber(String)` メソッドを呼び出して、キーボードから入力された値を変数に取り込みます。  
`Keyboard` クラスの利用方法については [Keyboard クラス 利用ガイド](../../manuals/provided/keyboard.md) を参照してください。

### 手順-2. SQLの設定
```java
String sql = "SELECT * FROM products WHERE id = ?";
```
- ここでは入力された商品IDを条件として検索するので、`WHERE` 句が必要になります。
- このSQLでは「`id = ?`」のような見慣れない `WHERE` 句になっています。  
一般に検索条件は「`id = 3`」のように具体的な商品IDが設定されますが、 実際には検索のたびに商品IDが同じとは限りません。  
そのため、商品IDを決めることは非現実的です。そこで、検索ごとに変化する商品IDを `?` で代表させておきます。  
このような `?` を **プレースホルダ** といい、プレースホルダ `?` を含むSQLを **プレースホルダ付きSQL** といいます。
- プレースホルダ付きSQLは、SQLの実行前にプレースホルダ `?` を具体的な値に置換してから実行されます。

### 手順-3 ～ 手順-4：データベース接続関連オブジェクトを取得する
```java
try (Connection conn = DriverManager.getConnection(configure.getUrl(), configure.getUser(), configure.getPassword());
     PreparedStatement pstmt = conn.prepareStatement(sql);
  ) {
```
- 前回の処理と変更はありません。  
詳細は[前回のチュートリアル](./11-jdbc-all_03.md)を参照してください。
- ただし、SQLを実行する前にSQL実行オブジェクトに格納されているプレースホルダ付きSQLのプレースホルダを具体的な商品IDに置換する処理が必要なので、ここではデータベース接続オブジェクトとSQL実行オブジェクトを取得します。

---

### 手順-5. プレースホルダを商品IDに置換する
```java
pstmt.setInt(1, targetId);
```
- 実行するSQLはSQL実行オブジェクトに格納されているので、`PreparedStatement#setInt(int, int)` メソッドを呼び出してプレースホルダ `?` を具体的な商品IDに置換しています。
- 置換する値が文字列の場合は `setString`メソッド、整数の場合は `setInt` メソッドのようにデータ型別に置換するメソッドが用意されています。
- これらのメソッドの第一引数はプレースホルダ `?` の位置を序数で指定します。最初のプレースホルダであれば「1」になります。  
また第二引数は具体的な値を変数またはリテラルで指定します。
- このサンプルでは「1番目のプレースホルダを変数 `targetId` に代入されている商品IDで置換する」ということになります。

---

### 手順-6：SQLの実行と結果セットの取得する
- 前回の処理と変更はありません。  
詳細は[前回のチュートリアル](./11-jdbc-all_03.md)を参照してください。

---

### 手順-7：結果セットから商品インスタンスを生成する
```java
Product bean  = null;
if (rs.next()) {
  int id = rs.getInt("id");
  int categoryId = rs.getInt("category_id");
  String name = rs.getString("name");
  int price = rs.getInt("price");
  int quantity = rs.getInt("quantity");
  bean = new Product(id, categoryId, name, price, quantity);
}
```
- 今回のSQLは商品IDでの検索なので、主キー検索ということになります。  
- 主キー検索の場合の検索結果は1件または0件です。したがって、取得直後の結果セットに対してレコードポインタを移動すると `true` または `false` に限られます。  
このことを考えると、検索結果としては商品リストとして扱う必要はなく、レコードポインタを移動した結果が `true` の場合のみ結果セットからインスタンスとして取り出せばよいので、条件分岐で十分ということになります。
- レコードポインタを移動する前に、商品インスタンスを `null` で初期化しておきます。

---

### 手順-8：検索結果のチェック
```java
if (bean == null) {
  // 検索結果が0件の場合：メッセージを表示して終了
  Display.showMessageln("指定されたIDの商品は見つかりませんでした。");
  return;
}
```
- 検索結果が1件なら商品インスタンスに値が入り、0件なら null のままとなります。  
商品インスタンスが `null` ということは検索結果が0件であるということです。
- 仕様から、商品インスタンスが `null` であればメッセージを表示してプログラムを終了します。
- メッセージを表示するために、`Display#showMessageln(String)` メソッドを呼出しています。  
このメソッドに関しては、[Display クラス利用ガイド](../../manuals/provided/display.md) を参照してください。

---

### 手順-9：取得した商品インスタンスを表示
```java
System.out.printf("%-4s\t%-4s\t%-16s\t%-4s\t%-4s\n", 
                  "商品ID", "カテゴリID", "商品名", "価格", "数量");
System.out.printf("%-4d\t%-4d\t%-16s\t%-4d\t%-4d\n",
                  bean.getId(),
                  bean.getCategoryId(),
                  bean.getName(),
                  bean.getPrice(),
                  bean.getQuantity()
                );

```
- 商品インスタンスを表示するので、拡張 `for` 文を使う必要はありません。

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - プレースホルダ付きSQLの利用方法
  - 主キー検索の扱い（0件 または 1件）

---

## 5. 関連チュートリアルの参照
- 手順-3〜4は前々回のチュートリアルと同じ処理です。
- 詳細は [JDBC接続によるCRUD操作・全検索 ～ データベースに接続してみた編](./11-jdbc-all_01.md) を参照してください。

---

## 6. 参考メモ：プレースホルダ付きSQLを利用する意義について

- ユーザー入力を安全に扱えるためSQLインジェクション防止になる。
- 同じSQLを繰り返し使う場合、DB側で効率的に処理できる。
- SQL文と値設定が分離されて可読性・保守性が高まる。

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
