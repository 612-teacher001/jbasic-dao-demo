\c jbasicdaodb

INSERT INTO categories (name) VALUES
	  ('本')
	, ('DVD')
	, ('ゲーム');

INSERT INTO products (category_id, name, price, quantity) VALUES
	  (1, 'Javaの基本', 2500, 22)
	, (1, 'MLB Fun', 980, 23)
	, (1, '料理 BOOK!', 1200, 14)
	, (2, '懐かしのアニメシリーズ', 2000, 15)
	, (2, 'The Racer', 1000, 19)
	, (2, 'Space Wars 3', 1800, 33)
	, (3, 'パズルゲーム', 780, 6)
	, (3, 'Invader Fighter', 3400, 9)
	, (3, 'Play the BasketBall', 2200, 2);

INSERT INTO departments (name) VALUES
	  ('営業部')
	, ('開発部')
	, ('企画部')
	, ('管理部')
	, ('製造部');

INSERT INTO employees (department_id, name, phone, hired_date) VALUES
	  (1, '羽生 章洋', '00800', '1998-12-17')
	, (1, '釜本 喜美子', '01600', '1991-2-20')
	, (2, '安部 弘江', '01250', '1991-2-22')
	, (2, '松村 秀和', '02975', '1991-4-2')
	, (3, '萩原 恵理子', '01251', '2008-9-28')
	, (3, '岡田 奈緒子', '02850', '2007-5-1')
	, (3, '井上 尚志', '02450', '2000-11-15')
	, (4, '西口 麻衣子', '03000', '2008-12-3')
	, (4, '滝本 順三', '05000', '2004-12-18')
	, (4, '工藤 新一', '01500', '2009-4-1');

