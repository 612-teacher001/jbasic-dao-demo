# *T1.5* JDBC接続によるCRUD操作・登録 ～ 商品を新規登録してみた編

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)

### チュートリアルの対象

  - 対象コミット：[22528a8](https://github.com/612-teacher001/jbasic-dao-demo/commit/22528a8)
  - 対象クラス：[`jp.example.app.t1.JdbcInsert`](https://github.com/612-teacher001/jbasic-dao-demo/blob/main/src/main/java/jp/example/app/t1/JdbcInsert.java)



## 1. 概要

このチュートリアルでは、レコードの新規登録をどう実現するかという方法を説明します。  

## 2. 事前準備

データベースの詳細については、データベース接続情報を含めて [データベース概要](../00-database.md) を参照してください。

## 3. 手順ごとの解説

ここで解説する手順は `jp.example.app.t1.JdbcInsert#main(String[])` メソッドのコードを前提としています。  
実際にコードを確認しながら読み進めてください。

### 手順-0. 商品一覧を表示する
```java
JdbcAll.main(new String[] {});
System.out.println();
```
- 商品一覧を表示するメソッドはすでに `JdbcAll#main` メソッドとして作成したので、そのまま呼び出しています。
- この前後には `System.out.println("")` として区切りの空行を表示しています。
- INSERT/UPDATE/DELETE 文は実行結果が目に見えないため、前後に商品一覧を表示して確認します。


### 手順-1. キーボードから入力された商品の情報を取得する
```java
int categoryId = Keyboard.getInputNumber("追加する商品のカテゴリID：");
String name = Keyboard.getInputString("追加する商品の商品名：");
int price = Keyboard.getInputNumber("追加する商品の価格：");
int quantity = Keyboard.getInputNumber("追加する商品の数量：");
```
- `Keyboard#getInputString(String)` メソッドと `Keyboard#getInputNumber(String)` メソッドを呼び出して登録する商品の情報を取得しています。  
取得する情報は、商品カテゴリID、商品名、価格、数量の4項目です。  
商品登録の処理なので、この時点での商品IDは決まっていないので商品IDは取得しません。
- これらのメソッドについては [Keyboard クラスの利用ガイド](../../manuals/provided/keyboard.md) を参照してください。

### 手順-2. 入力値をもとに商品をインスタンス化する
```java
Product product = new Product(categoryId, name, price, quantity);
```
- 手順-1で取得した商品情報をもとに登録する商品のインスタンスを生成します。

### 手順-3. 実行するSQLを設定する
```java
String sql = "INSERT INTO products (category_id, name, price, quantity) VALUES (?, ?, ?, ?)";
```
- INSERT文を設定します。  
すべてのフィールドを指定した登録ではないので、設定するフィールドリストが指定されます。  
それに対応して設定するフィールド値リストをプレースホルダで指定します。  
これによってどのような商品でも登録できる自由度ができます。
- 商品IDは追加時にデータベース側で自動採番するので指定する必要はありません。

### 手順-4. データベース接続情報を取得する
- `DbConfigure` オブジェクトを利用してデータベース接続情報としてURL・ユーザー名・パスワードを取得します。  
処理内容は前回と同じです。

### 手順-5. データベース接続関連オブジェクトを取得する
- DriverManagerからConnectionを取得します。  
処理内容は前回と同じです。

### 手順-6. プレースホルダを商品インスタンスで置換する
```java
pstmt.setInt(1, product.getCategoryId());
pstmt.setString(2, product.getName());
pstmt.setInt(3, product.getPrice());
pstmt.setInt(4, product.getQuantity());
```
- プレースホルダを商品インスタンスのフィールド値で置換しています。  
Product オブジェクトを経由することで処理の見通しがよくなります。  
ただし、手順-1で入力値を取得した変数で置換することもできます。  

### 手順-7. SQLを実行する
```java
pstmt.executeUpdate();
```
- 実行するSQLはINSERT文なので、`executeUpdate()` メソッドを呼び出しています。  
- `executeQuery` と `executeUpdate` の違いについては、データベースに接続してみた編の [SQLを実行して結果セットを取得する](./11-jdbc-all_01.md#execute) を参照してください。
- INSERT/UPDATE/DELETE文を実行する `executeUpdate()` メソッドはSQLが成功した行数が戻り値になっています。  
戻り値を利用するかどうかは呼び出し元の状況によって変わりますが、ここでは戻り値を利用する必要がないので `executeUpdate()` メソッドを呼び出すにとどめています。

---

## 4. まとめ

ここのコードで学ぶべきポイント：

  - INSERT/UPDATE/DELETE文を実行するには `PreparedStatement#executeUpdate()` メソッドを利用する
  - `PreparedStatement#executeUpdate()` メソッドの戻り値はSQLが成功した行数である  
  ただし、呼び出し側で戻り値が必要ない場合は再利用する必要はない
  - INSERT文では商品IDは指定せず、データベース側の自動採番に任せる

---

## 5. 関連チュートリアルの参照
- 手順-4〜6は前々回のチュートリアルと同じ処理です。
- 詳細は [JDBC接続によるCRUD操作・全検索 ～ try-with-resourcesの導入編](./11-jdbc-all_03.md) を参照してください。

---

[Javaによるデータベース接続とCRUD操作のチュートリアル](../tutorials.md) > [JDBC接続によるCRUD操作](./10-jdbc.md)
