# DbConfigure クラス使用ガイド

## 1. 役割

`DbConfigure` クラスは、データベース接続に必要な情報（URL、ユーザ名、パスワード）を提供するユーティリティクラスです。  
学習者はこのクラスを利用するだけで、接続情報をコードに直接書き込む必要はありません。

## 2. 配布と利用方法

- このクラスはあらかじめ配布されています。
- 直接編集する必要はなく、JDBCで接続する際に呼び出して利用します。

## 3. 使用例

```java
// DbConfigure のインスタンスを生成
DbConfigure configure = new DbConfigure();

// 接続情報を取得
String url = configure.getUrl();
String user = configure.getUser();
String password = configure.getPassword();

// JDBCでConnectionを取得
Connection conn = DriverManager.getConnection(url, user, password);

```

## 4. 補足

- 内部的には db.properties などの設定ファイルから値を読み込む構造になっています。  
- この内部構造により接続情報の管理が容易になり、学習者は接続処理の理解に集中できます。
