DROP DATABASE IF EXISTS pourControlDB;

CREATE DATABASE pourControlDB;
USE pourControlDB;

DROP TABLE IF EXISTS cocktail;
CREATE TABLE cocktail (
    cid INT,
    cocktail_name VARCHAR(30) NOT NULL,
    on_menu TINYINT NOT NULL,
    instructions MEDIUMTEXT,
    CONSTRAINT PK_cocktail PRIMARY KEY (cid)
);

DROP TABLE IF EXISTS ingredient;
CREATE TABLE ingredient (
    iid INT,
    ingredient_name VARCHAR(30) NOT NULL,
    ml_inStock INT DEFAULT 0,
    target_ml INT DEFAULT 0,
    CONSTRAINT PK_ingredient PRIMARY KEY (iid)
);

DROP TABLE IF EXISTS ingredientInCocktail;
CREATE TABLE ingredientInCocktail (
    ingredient_id INT,
    cocktail_id INT,
    CONSTRAINT FOREIGN KEY FK_ingredient_for_cocktail (ingredient_id)
        REFERENCES ingredient (iid),
    CONSTRAINT FOREIGN KEY FK_cocktail_for_ingredient (cocktail_id)
        REFERENCES cocktail (cid)
);

DROP TABLE IF EXISTS dailySale;
CREATE TABLE dailySale (
    sid INT AUTO_INCREMENT,
    cocktail_id INT NOT NULL,
    date DATETIME,
    qty_sold INT,
    CONSTRAINT PK_dailySale PRIMARY KEY (sid),
    CONSTRAINT FOREIGN KEY FK_cocktail_sale (cocktail_id)
        REFERENCES cocktail (cid)
);

DROP TABLE IF EXISTS dailyAudit;
CREATE TABLE dailyAudit (
    aid INT AUTO_INCREMENT,
    ingredient_id INT,
    date DATETIME,
    start_ml INT DEFAULT 0,
    end_ml INT DEFAULT 0,
    CONSTRAINT PK_dailyAudit PRIMARY KEY (aid),
    CONSTRAINT FOREIGN KEY FK_ingredient_audit (ingredient_id)
        REFERENCES ingredient (iid)
);




DROP TABLE IF EXISTS user;
CREATE TABLE user (
    uid INT AUTO_INCREMENT,
    username VARCHAR(25) NOT NULL,
    email VARCHAR(35) NOT NULL,
    -- the length of this will depend on the hash we use
    -- this is for SHA-256. change to 32 if we want to use MD5
    -- or whatever output length of the hash we end up using
    password_hash CHAR(64) NOT NULL,
    CONSTRAINT PK_user PRIMARY KEY (uid)
);
