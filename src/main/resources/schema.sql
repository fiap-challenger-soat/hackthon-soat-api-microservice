CREATE TABLE IF NOT EXISTS tb_category (
   category_id BIGSERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_client (
    client_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS tb_product (
    product_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    image VARCHAR(255),
    category_id BIGINT REFERENCES tb_category (category_id)
);

CREATE TABLE IF NOT EXISTS tb_stock (
    stock_id BIGSERIAL PRIMARY KEY,
    quantity INT NOT NULL,
    product_entity_product_id BIGINT REFERENCES tb_product (product_id)
);
