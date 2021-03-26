INSERT INTO ARTICLE (id, name, title) values (1, 'someone', 'first');
INSERT INTO ARTICLE (id, name, title) values (2, 'someone', 'second');
INSERT INTO ARTICLE (id, name, title) values (3, 'someone', 'third');
INSERT INTO ARTICLE (id, name, title) values (4, 'someone', 'fourth');
INSERT INTO ARTICLE (id, name, title) values (5, 'someone', 'fifth');

INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (1, 1, 'somone', 'aaa');
INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (2, 1, 'somone', 'bbb');
INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (3, 1, 'somone', 'ccc');
INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (4, 1, 'somone', 'ddd');
INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (5, 2, 'somone', 'ee');
INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (6, 2, 'somone', 'ff');
INSERT INTO ARTICLE_COMMENT(id, article_id, name, body) values (7, 2, 'somone', 'ggg');


INSERT INTO product (id, name, total_amount, started_at, finished_at) values (1, 'aaa', 10000, DATEADD('DAY', -20, now()), DATEADD('DAY', -2, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (2, 'bbb', 20000, DATEADD('DAY', -20, now()), DATEADD('DAY', -2, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (3, 'ccc', 1000, DATEADD('DAY', -2, now()), DATEADD('DAY', 2, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (4, 'ddd', 2000, DATEADD('DAY', -2, now()), DATEADD('DAY', 2, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (5, 'eee', 2000, DATEADD('DAY', -2, now()), DATEADD('DAY', 2, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (6, 'fff', 60000, DATEADD('DAY', 2, now()), DATEADD('DAY', 12, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (7, 'ggg', 40000, DATEADD('DAY', 2, now()), DATEADD('DAY', 12, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (8, 'hhh', 40000, DATEADD('DAY', 2, now()), DATEADD('DAY', 12, now()));
INSERT INTO product (id, name, total_amount, started_at, finished_at) values (9, 'iii', 40000, DATEADD('DAY', 2, now()), DATEADD('DAY', 12, now()));

