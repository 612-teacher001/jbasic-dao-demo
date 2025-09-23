package jp.example.app.bean;

/**
 * productsテーブルの1レコードを管理するクラス
 */
public class Product {
	
	/**
	 * フィールド
	 */
	private int id;         // 商品ID
	private int categoryId; // 商品カテゴリID
	private String name;    // 商品名
	private int price;      // 価格
	private int quantity;   // 数量
	
	/**
	 * 引数なしコンストラクタ
	 */
	public Product() {}

	/**
	 * コンストラクタ：登録処理の際に呼出される
	 * @param categoryId 商品カテゴリID
	 * @param name       商品名
	 * @param price      価格
	 * @param quantity   数量
	 */
	public Product(int categoryId, String name, int price, int quantity) {
		this.categoryId = categoryId;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	 * コンストラクタ：検索または更新操作の際に呼出される
	 * @param id         商品ID
	 * @param categoryId 商品カテゴリID
	 * @param name       商品名
	 * @param price      価格
	 * @param quantity   数量
	 */
	public Product(int id, int categoryId, String name, int price, int quantity) {
		this(categoryId, name, price, quantity);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * シリアル化メソッド：オブジェクトの文字列化
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [");
		builder.append("id=" + id + ", ");
		builder.append("categoryId=" + categoryId + ", ");
		builder.append("name=" + name + ", ");
		builder.append("price=" + price + ", ");
		builder.append("quantity=" + quantity);
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
		builder.append("categoryId=" + categoryId + ", ");
		builder.append("name=" + name + ", ");
		builder.append("price=" + price + ", ");
		builder.append("quantity=" + quantity);
		builder.append("]");
		return builder.toString();
	}
	
}
