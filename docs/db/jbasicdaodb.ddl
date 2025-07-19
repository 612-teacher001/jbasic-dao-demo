DROP TABLE IF EXISTS products;

/**********************************/
/* テーブル名: 商品 */
/**********************************/
CREATE TABLE products(
		id INTEGER NOT NULL,
		name VARCHAR(10) NOT NULL,
		price INTEGER NOT NULL,
		stock INTEGER NOT NULL DEFAULT 0
);


ALTER TABLE products ADD CONSTRAINT IDX_products_PK PRIMARY KEY (id);

