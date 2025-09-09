# README

## プロジェクト概要

本プロジェクトはJavaからデータベースにアクセスするしくみをチュートリアル形式で解説するものです。

題材として、顧客側および管理者側の業務処理を行う Java コマンドラインアプリケーション（CUIベース）の開発を扱います。

JDBC によるデータベースへの単純な接続から始まり、最終的には DAO を導入するところまでを、アプリケーションを進化させながら説明していきます。

このチュートリアルではビルドツールを用いず、従来のJavaプロジェクト形式で進めます。  
一方、配布版ではGradleプロジェクト形式となっており、初期設定の簡略化や参考資料として利用可能です。

---

## コンテンツ

  - 1. [Getting Started](./docs/manuals/getting-started.md)
  - 2. [チュートリアル](./docs/tutorials/tutorials.md)
    - T1. JDBCによるデータベース接続とCRUD操作の実装（`jp.example.app.t1` パッケージ）
    - T2. DAOの導入（`jp.example.app.t2` パッケージ）

---

## 前提とする開発環境（IDE）

- **推奨IDE:** Eclipse（動作確認済み）  
- **Javaバージョン:** Java 21以降（本プロジェクトはJava 21で動作確認済み）  
- **設定ポイント:**  
- JDBCドライバのJARをプロジェクトのビルドパスに追加してください。  
- 実行構成でクラスパスが正しく設定されていることを確認してください。  

---

## 環境構築

### 環境のディレクトリ構成<a id="env-directory"></a>

Gitによる管理対象となっているディレクトリについては以下の通り：

```
├─src/
│  ├─main
│  │  ├─java/
│  │  │   └─jp/example/
│  │  │       └─app/
│  │  │           ├─provided/ ← 配布済みユーティリティクラス群
│  │  │           │  ├─ configure ← 設定ファイル読み込みユーティリティ群
│  │  │           │  |    ├─ PropertyLoader.java ← プロパティファイル読み込みユーティリティクラス
│  │  │           │  |    └─ DbConfig.java       ← データベース接続情報読み込みクラス
│  │  │           |  |
│  │  │           │  ├─ io ← 入出力用ユーティリティ
│  │  │           │  |    ├─ Display.java  ← 標準入力に文字列を表示するユーティリティクラス
│  │  │           │  |    └─ Keyboard.java ← キーボードから入力された値を取得するユーティリティクラス
│  │  │           │  |
│  │  │           │  └─ util ← 共通ユーティリティ
│  │  │           │       └─ FixedLengthGenerator.java ← 固定長文字列を生成するユーティリティクラス
│  │  │           │
│  │  │           ├─t1/ ← T1.X系列チュートリアル群
│  │  │           │　├─ JdbcAll.java        ← 全商品の検索
│  │  │           │　├─ JdbcPrimaryKey.java ← 主キー検索
│  │  │           │　├─ JdbcKeyword.java    ← キーワード検索
│  │  │           │　├─ JdbcInsert.java     ← 商品の新規追加
│  │  │           │　├─ JdbcUpdate.java     ← 商品の更新
│  │  │           │　└─ JdbcDelete.java     ← 主キーによる商品の削除
│  │  │           │
│  │  │           ├─t2/ ← T2.X系列チュートリアル群
│  │  │           │　├─ DaoAll.java        ← 全商品の検索
│  │  │           │　├─ DaoPrimaryKey.java ← 主キー検索
│  │  │           │　├─ DaoKeyword.java    ← キーワード検索
│  │  │           │　├─ DaoInsert.java     ← 商品の新規追加
│  │  │           │　├─ DaoUpdate.java     ← 商品の更新
│  │  │           │　└─ DaoDelete.java     ← 主キーによる商品の削除
│  │  │           │
│  │  │           ├─dao/      ← 受講者が実装する層（DAOクラス群）
│  │  │           ├─service/  ← 受講者が実装する層（Serviceクラス群）
│  │  │           └─Main.java ← セットアップ確認用コマンドラインプログラム
│  │  │
│  │  ├─webapp
│  │  │    └─WEB-INF
│  │  │       └─view/ ← 受講者が実装する層（JSPを配置予定）
│  │  │
│  │  └─resources/   ← 各種設定ファイル群を配置
│  │       └─db.properties ← データベース接続情報設定ファイル
│  │    
│  └─test
│      ├─java/
│      │   └─jp/example/
│      │       └─app/
│      │       　├─ provided/ ← 配布済みユーティリティクラスのテストケース群
│      │       　│   ├─ configure ← 設定ファイル読み込みユーティリティテストケース群
│      │       　│   │    └─ DbConfigureTest.java ← データベース接続情報読み込みクラステストケース
│      │       　│   │
│      │       　│   ├─ io ← 入出力用ユーティリティクラステストケース群
│      │       　│   │    └─ KeyboardTest.java ← ーボードから入力された値を取得するユーティリティクラステストケース
│      │       　│   │
│      │       　│   └─ util ← 入出力用ユーティリティクラステストケース群
│      │       　│        └─ DbConfig.java ← 固定長文字列を生成するユーティリティクラステストケース
│      │       　│
│      │       　├─dao/      ← DAO用テストケース群
│      │       　└─service/  ← Serviceクラステストケース群
│      │
│      └─resources/   ← テスト用各種設定ファイル群を配置
│      
├─sql/ ← DB初期化用スクリプト（研修で手動 or Gradleタスクから実行）
│      
├─docs/
│   ├─manuals/   ← 本チュートリアルで利用するシステムについての各種マニュアル
│   └─tutorials/ ← チュートリアル本体
│      
├─backup/        ← ローカルのバックアップ用
│      
└─dev-memo/      ← 開発用メモ
    └─db/        ← データベース関連ダイアグラムとSQL（DDLおよびDML）
```

### JDK

- OpenJDK 21 または Oracle JDK 21以降を推奨

### JDBCドライバ（PostgreSQLの場合）

- 本プロジェクトで使用するデータベースはPostgreSQLを前提としています。  
- JDBCドライバのダウンロードとビルドパス設定については [JDBCドライバの導入とビルドパス設定手順](./docs/manuals/jdbc-driver-setup.md) を参照してください。

### コーディング規約・命名規約

- 大まかな規約は慣習に準拠しますが、とくにクラスなどでのメソッドコメントなどの記述については、[Javadoc フォーマット案：主文と補足文の句点ルール](./docs/manuals/javadoc-format-and-examples.md) を参照してください。


---

## 関連ドキュメント
<!-- リリースブランチで追加予定 -->
- [変更履歴](./CHANGELOG.md)


