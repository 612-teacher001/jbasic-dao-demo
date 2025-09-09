-- テーブルを削除
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;

/**********************************/
/* テーブル名: カテゴリテーブル */
/**********************************/
CREATE TABLE categories(
		id SERIAL NOT NULL,
		name VARCHAR(10) NOT NULL
);

/**********************************/
/* テーブル名: 商品テーブル */
/**********************************/
CREATE TABLE products(
		id SERIAL,
		category_id INTEGER NOT NULL,
		name VARCHAR(50) NOT NULL,
		price INTEGER NOT NULL,
		quantity INTEGER NOT NULL
);

/**********************************/
/* テーブル名: 部署テーブル */
/**********************************/
CREATE TABLE departments(
		id SERIAL NOT NULL,
		name VARCHAR(10) NOT NULL
);

/**********************************/
/* テーブル名: 担当者テーブル */
/**********************************/
CREATE TABLE employees(
		id SERIAL,
		department_id INTEGER NOT NULL,
		name VARCHAR(50) NOT NULL,
		phone VARCHAR(5) NOT NULL,
		hired_date DATE NOT NULL
);

-- カテゴリテーブルの主キー制約
ALTER TABLE categories ADD CONSTRAINT IDX_categories_PK PRIMARY KEY (id);

-- 商品テーブルの主キー制約と外部参照キー制約
ALTER TABLE products ADD CONSTRAINT IDX_products_PK PRIMARY KEY (id);
ALTER TABLE products ADD CONSTRAINT IDX_products_FK0 FOREIGN KEY (category_id) REFERENCES categories (id);

-- 部署テーブルの主キー制約
ALTER TABLE departments ADD CONSTRAINT IDX_departments_PK PRIMARY KEY (id);

-- 担当者テーブルの主キー制約とがイッブ参照キー制約
ALTER TABLE employees ADD CONSTRAINT IDX_employees_PK PRIMARY KEY (id);
ALTER TABLE employees ADD CONSTRAINT IDX_employees_FK0 FOREIGN KEY (department_id) REFERENCES departments (id);

