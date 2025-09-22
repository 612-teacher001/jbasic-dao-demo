# Keyboard クラス 利用ガイド

## 1. 役割
`Keyboard` クラスは、**コンソール（キーボード）からの入力を簡単に取得するためのユーティリティクラス**です。

- 文字列入力：`getInputString(String msg)`  
- 整数入力：`getInputNumber(String msg)`  

入力時の例外は現状 `System.exit(0)` によりプログラムを終了させますが、実務向けには独自例外を投げる方式が推奨されます。

---

## 2. 配布と利用方法
- **配布形態**  
  - `jp.example.app.provided.io` パッケージに含まれるクラスとして提供。  

- **利用方法**  
  1. プロジェクトに `Keyboard` クラスを含める。
  2. 他のクラスから静的メソッドとして呼び出す：

```java
String value = Keyboard.getInputString("メッセージ");
int number = Keyboard.getInputNumber("メッセージ");
```

## 3. 使用例

### 3.1 文字列入力

```java
String name = Keyboard.getInputString("名前を入力してください: ");
System.out.println("入力された名前: " + name);
```

### 3.2 整数入力

```java
int age = Keyboard.getInputNumber("年齢を入力してください: ");
System.out.println("入力された年齢: " + age);
```

### 3.3 注意点

- 整数入力時に数字以外を入力すると例外が発生し、プログラムが終了します（実務では独自例外による例外処理が推奨されます）。 

---

## 4. まとめ

- `Keyboard` クラスは簡単にコンソール入力を取得できるユーティリティクラスです。  
- 文字列入力と整数入力の二種類のメソッドを提供します。  
- 現在の仕様では **例外発生時に強制終了** するため、ユニットテストや実務での利用時には注意してください。  
- 将来的には独自例外を導入すると、**より安全でテスト可能な設計** になります。