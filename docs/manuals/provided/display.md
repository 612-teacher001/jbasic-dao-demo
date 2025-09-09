# Display クラス 利用ガイド

## 1. 役割

`Display` クラスは、Javaアプリケーション内で標準出力に文字列を表示するためのユーティリティクラスです。

- 改行あり・改行なしの表示を簡単に呼び出すことができます。  
- 静的メソッドのみで構成されているため、インスタンス化する必要はありません。

---

## 2. 配布と利用方法

### 配布

- パッケージ：`jp.example.app.provided.io`  
- 配布対象：共有ユーティリティクラスとしてプロジェクトに含めて配布可能

### 利用方法

1. パッケージをインポートする
```java
import jp.example.app.provided.io.Display;
```

2. 静的メソッドを呼び出して標準出力に文字列を表示する
```java
Display.showMessageln("改行ありのメッセージ");
Display.showMessage("改行なしのメッセージ");
```

---

## 3. 使用例
```java
public class Main {
    public static void main(String[] args) {
        // 改行ありの表示
        Display.showMessageln("Hello, World!");
        
        // 改行なしの表示
        Display.showMessage("Processing...");
        Display.showMessageln("Done!");
    }
}
```

#### 出力結果

```
Hello, World!
Processing...Done!
```

---

## 4. まとめ

- `Display` は標準出力に文字列を簡単に表示するユーティリティクラスです。  
- インスタンス化不要で、静的メソッド `showMessageln` / `showMessage` を提供します。  
- 改行の有無に応じたメッセージを出力できます。

小規模なコンソールアプリケーションやデバッグ出力用に最適です。
また、学習用や簡単なログ表示にも活用できます。
