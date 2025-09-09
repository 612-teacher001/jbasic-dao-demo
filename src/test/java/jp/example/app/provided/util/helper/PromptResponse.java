package jp.example.app.provided.util.helper;

public class PromptResponse {
	
	/**
	 * フィールド
	 */
	private String prompt;   // 表示メッセージ
	private String response; // 入力値
	
	/**
	 * デフォルトコンストラクタ
	 */
	public PromptResponse() {}

	/**
	 * コンストラクタ
	 * @param prompt   表示メッセージ
	 * @param response 入力値
	 */
	public PromptResponse(String prompt, String response) {
		this.prompt = prompt;
		this.response = response;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
	
	public String getResponseln() {
		return this.response + "\n";
	}
	
}
