-- this sql is used for h2, some of the keywords are not common used.

DROP TABLE IF EXISTS `spitter`;
CREATE TABLE `spitter`
(
    `id`        INT         NOT NULL AUTO_INCREMENT,
    `firstName` VARCHAR(30) NOT NULL,
    `lastName`  VARCHAR(30) NOT NULL,
    `username`  VARCHAR(30) NOT NULL,
    `password`  VARCHAR(30) NOT NULL,
    `deleted`   TINYINT(2)  NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `spittle`;
CREATE TABLE `spittle`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `spitter_id` INT          NOT NULL,
    `message`   VARCHAR(255) NOT NULL,
    `time`      TIMESTAMP    NOT NULL,
    `latitude`  DOUBLE DEFAULT 0,
    `longitude` DOUBLE DEFAULT 0,
    `deleted`   TINYINT(2)    NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
);


