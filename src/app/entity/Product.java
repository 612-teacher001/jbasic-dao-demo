package app.entity;

public class Product {
	
	/**
	 * フィールド
	 */
	private int id;      // 商品ID
	private String name; // 商品名
	private int price;   // 価格
	private int stock;   // 在庫数
	
	/**
	 * デフォルトコンストラクタ
	 */
	public Product() {}

	/**
	 * コンストラクタ
	 * @param name  商品名
	 * @param price 価格
	 * @param stock 最個数
	 */
	public Product(String name, int price, int stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	/**
	 * コンストラクタ
	 * @param id    商品ID
	 * @param name  商品名
	 * @param price 価格
	 * @param stock 最個数
	 */
	public Product(int id, String name, int price, int stock) {
		this(name, price, stock);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	/**
	 * シリアル化：テストケースで利用する場合にはidフィールドをコメントアウトしておくほうが無難
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Product [");
		builder.append("id=" + id + ", ");
		builder.append("name=" + name + ", ");
		builder.append("price=" + price + ", ");
		builder.append("stock=" + stock);
		builder.append("]");
		return builder.toString();
	}
	
}
