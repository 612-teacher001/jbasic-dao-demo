package jp.example.app.bean;

public class ProductView extends Product{

	/**
	 * フィールド
	 */
	private String categoryName;

	/**
	 * 引数なしコンストラクタ
	 */
	public ProductView() {
		super();
	}

	/**
	 * コンストラクタ
	 * @param id           商品ID
	 * @param categoryId   商品カテゴリID
	 * @param categoryName 商品カテゴリ名
	 * @param name         商品名
	 * @param price        価格
	 * @param quantity     数量
	 */
	public ProductView(int id, int categoryId, String categoryName, String name, int price, int quantity) {
		super(id, categoryId, name, price, quantity);
		this.categoryName = categoryName;
	}

	/**
	 * コンストラクタ
	 * @param categoryId   商品カテゴリID
	 * @param categoryName 商品カテゴリ名
	 * @param name         商品名
	 * @param price        価格
	 * @param quantity     数量
	 */
	public ProductView(int categoryId, String categoryName, String name, int price, int quantity) {
		super(categoryId, name, price, quantity);
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	/**
	 * シリアル化メソッド：オブジェクトの文字列化
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [");
		builder.append("id=" + this.getId() + ", ");
		builder.append("categoryId=" + this.getCategoryId() + ", ");
		builder.append("categoryName=" + categoryName + ", ");
		builder.append("name=" + this.getName() + ", ");
		builder.append("price=" + this.getPrice() + ", ");
		builder.append("quantity=" + this.getQuantity());
		builder.append("]");
		return builder.toString();
	}

	/**
	 * 比較用文字列を取得する
	 * @return idフィールドを除いたシリアル化文字列
	 */
	public String toStringCompared() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [");
		builder.append("categoryId=" + this.getCategoryId() + ", ");
		builder.append("categoryName=" + categoryName + ", ");
		builder.append("name=" + this.getName() + ", ");
		builder.append("price=" + this.getPrice() + ", ");
		builder.append("quantity=" + this.getQuantity());
		builder.append("]");
		return builder.toString();
	}
	
}
