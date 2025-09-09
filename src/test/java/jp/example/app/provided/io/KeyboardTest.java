package jp.example.app.provided.io;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import jp.example.app.provided.util.helper.PromptResponse;

/**
 * Keyboard クラスの入力メソッドの動作確認用テストクラス
 * 
 * 対象メソッド:
 *   - Keyboard#getInputNumber(String prompt)
 *   - Keyboard#getInputString(String prompt)
 * 
 * テスト方針:
 *   - 標準入力を疑似的に設定して、各入力メソッドが正しい値を返すことを確認する。
 *   - 標準出力に表示されるプロンプト文字列が意図通りかを検証する。
 *   - パラメータ化テストを使用し、複数の入力ケースを網羅する。
 * 
 * 補助:
 *   - provideInput(String data) メソッドで標準入力をセット。
 *   - outContent により標準出力を捕捉して検証。
 * 
 * 注意:
 *   - 現在は正常系テストのみ。異常系や境界値は別途追加可能。
 *   - 標準入出力の操作はテストごとにリセットしている。
 */
class KeyboardTest {

    private InputStream originalIn;
    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        originalIn = System.in;
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /**
     * 標準入力を疑似的にセットする。
     * Keyboardの入力メソッドをテストする際に使用する。
     */
    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
    }

    @Nested
    @DisplayName("Keyboard#getInputNumber() のテスト")
    class GetInputNumberTest {

        @ParameterizedTest
        @MethodSource("dataProvider")
        @DisplayName("表示メッセージと入力値が正しい")
        void testNumberInput(PromptResponse expected) {
            provideInput(expected.getResponseln());

            int expectedValue = Integer.parseInt(expected.getResponse());
            int actualValue = Keyboard.getInputNumber(expected.getPrompt());
            String actualMessage = outContent.toString(StandardCharsets.UTF_8);

            assertThat("出力メッセージが正しいこと", actualMessage, startsWith(expected.getPrompt()));
            assertThat("入力値が正しいこと", actualValue, is(expectedValue));
        }

        /**
         * テストパラメータを提供する
         * ここではKeyboard#getInputNumber()用の正常系入力を準備する。
         * - 「メニュー選択」用: 1
         * - 「購入金額入力」用: 1000
         * @return テストパラメータ群
         */        static Stream<Arguments> dataProvider() {
            List<PromptResponse> testCases = new ArrayList<>();
            testCases.add(new PromptResponse("メニューを選択してください。-> ", "1"));
            testCases.add(new PromptResponse("商品を購入するための金額を入力してください。-> ", "1000"));
            return testCases.stream().map(Arguments::of);
        }
    }

    @Nested
    @DisplayName("Keyboard#getInputString() のテスト")
    class GetInputStringTest {

        @ParameterizedTest
        @MethodSource("dataProvider")
        @DisplayName("表示メッセージと入力値が正しい")
        void testStringInput(PromptResponse expected) {
            provideInput(expected.getResponseln());

            String actualValue = Keyboard.getInputString(expected.getPrompt());
            String actualMessage = outContent.toString(StandardCharsets.UTF_8);

            assertThat("出力メッセージが正しいこと", actualMessage, startsWith(expected.getPrompt()));
            assertThat("入力値が正しいこと", actualValue, is(expected.getResponse()));
        }

        /**
         * テストパラメータを提供する
         * ここではKeyboard#getInputNumber()用の正常系入力を準備する。
         * - 「選択した商品名入力」用: ゴカコーラ
         * - 「手続きの可否入力」用: y
         * @return テストパラメータ群
         */        static Stream<Arguments> dataProvider() {
            List<PromptResponse> testCases = new ArrayList<>();
            testCases.add(new PromptResponse("商品：", "ゴカコーラ"));
            testCases.add(new PromptResponse("商品購入手続きを行いますか？", "y"));
            return testCases.stream().map(Arguments::of);
        }
    }
}
