CREATE TABLE schemawizard.posts (created_date INTERVAL YEAR TO MONTH NOT NULL, rate NUMERIC DEFAULT 0.0 NOT NULL, title VARCHAR(300) NOT NULL, user_email VARCHAR(50) NOT NULL, user_id INTEGER NOT NULL, CONSTRAINT pk_posts_user_id_created_date PRIMARY KEY (user_id, created_date), CONSTRAINT fk_posts_user_id_user_email FOREIGN KEY (user_id,user_email) REFERENCES schemawizard.users (id, email), CONSTRAINT unq_user_id_title UNIQUE (user_id, title))