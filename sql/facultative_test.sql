-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema facultative
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema facultative
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `facultative_test` DEFAULT CHARACTER SET utf8mb3 ;
USE `facultative_test` ;

-- -----------------------------------------------------
-- Table `category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `category` ;

CREATE TABLE IF NOT EXISTS `category` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`title` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `status` ;

CREATE TABLE IF NOT EXISTS `status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `course` ;

CREATE TABLE IF NOT EXISTS `course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `duration` INT UNSIGNED NOT NULL,
  `start_date` DATETIME NOT NULL,
  `amount_students` INT UNSIGNED NULL DEFAULT 0,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `category_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE,
  INDEX `fk_course_category_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_course_course_status1_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `fk_course_category`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`),
  CONSTRAINT `fk_course_course_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `status` (`id`));


-- -----------------------------------------------------
-- Table `role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `role` ;

CREATE TABLE IF NOT EXISTS `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `status` ;

CREATE TABLE IF NOT EXISTS `status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE);


-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user` ;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(16) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `block` TINYINT NOT NULL DEFAULT FALSE,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_user_role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `role` (`id`));


-- -----------------------------------------------------
-- Table `users_course`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `users_course` ;

CREATE TABLE IF NOT EXISTS `users_course` (
  `course_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `grade` INT UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`course_id`, `user_id`),
  INDEX `fk_course_has_user_user1_idx` (`user_id` ASC) VISIBLE,
  INDEX `fk_course_has_user_course1_idx` (`course_id` ASC) VISIBLE,
  CONSTRAINT `fk_course_has_user_course1`
    FOREIGN KEY (`course_id`)
    REFERENCES `course` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_course_has_user_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


    -- -----------------------------------------------------
    -- Inserts
    -- -----------------------------------------------------

    -- roles
    INSERT INTO role VALUES(1, 'ADMIN');
    INSERT INTO role VALUES(2, 'TEACHER');
    INSERT INTO role VALUES(3, 'STUDENT');

    -- course statuses
    INSERT INTO status VALUES(1, "COMING_SOON");
    INSERT INTO status VALUES(2, "IN_PROCESS");
    INSERT INTO status VALUES(3, "COMPLETED");

--    -- categories
--    INSERT INTO category VALUES(DEFAULT, 'PROGRAMMING',null);
--    INSERT INTO category VALUES(DEFAULT, 'FOREIGN_LANGUAGE',null);
--    INSERT INTO category VALUES(DEFAULT, 'MATH',null);
--
--    -- user
--    -- --admin
--    INSERT INTO user VALUES(DEFAULT, 'admin', 'admin', 'admin', 'admin,', 'admin@gmail.com', DEFAULT, (SELECT id FROM role WHERE name='ADMIN'));
--
--    -- --teachers
--    INSERT INTO user VALUES(DEFAULT, 'Eddard', '111111', 'Eddard', 'Stark,', 'Stark@game.com', DEFAULT, (SELECT id FROM role WHERE name='TEACHER'));
--    INSERT INTO user VALUES(DEFAULT, 'Tywin', '222222', 'Tywin', 'Lannister,', 'Lannister@game.com', DEFAULT, (SELECT id FROM role WHERE name='TEACHER'));
--    INSERT INTO user VALUES(DEFAULT, 'Rhaegar', '333333', 'Rhaegar', 'Targaryen,', 'Targaryen@game.com', DEFAULT, (SELECT id FROM role WHERE name='TEACHER'));
--    INSERT INTO user VALUES(DEFAULT, 'Baratheon ', '444444', 'Robert', 'Baratheon ,', 'Baratheon@game.com', DEFAULT,(SELECT id FROM role WHERE name='TEACHER'));
--
--    -- --students
--    INSERT INTO user VALUES(DEFAULT, 'Sansa ', '555555', 'Sansa', 'Stark', 'Sansa@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Arya ', '666666', 'Arya', 'Stark', 'Arya@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Brandon ', '777777', 'Brandon', 'Stark', 'Brandon@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Tyrion ', '888888', 'Tyrion', 'Lannister', 'Tyrion@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Bronn ', '999999', 'Bronn', 'Lannister', 'Bronn@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Joffrie ', '101010', 'Joffrie', 'Lannister', 'Joffrie@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Viserys ', '121212', 'Viserys', 'Targaryen', 'Viserys@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Daenerys ', '131313', 'Daenerys', 'Targaryen', 'Daenerys@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Missandei ', '141414', 'Missandei', 'Targaryen', 'Missandei@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Renly ', '171717', 'Renly', 'Baratheon', 'Renly@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Stannis ', '181818', 'Stannis', 'Baratheon', 'Stannis@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--    INSERT INTO user VALUES(DEFAULT, 'Selyse ', '191919', 'Selyse', 'Baratheon', 'Selyse@game.com', DEFAULT, (SELECT id FROM role WHERE name='STUDENT'));
--
--    -- courses
--    -- --coming soon
--    INSERT INTO course VALUES(DEFAULT, 'Java', 20, '2020-11-22',  DEFAULT, (SELECT id FROM category WHERE title="PROGRAMMING"), (SELECT id FROM status WHERE title="COMING_SOON"));
--    INSERT INTO course VALUES(DEFAULT, 'English', 100, '2020-11-22',  DEFAULT, (SELECT id FROM category WHERE title="FOREIGN_LANGUAGE"), (SELECT id FROM status WHERE title="COMING_SOON"));
--    INSERT INTO course VALUES(DEFAULT, 'Higher mathematics', 100, '2020-11-22', DEFAULT, (SELECT id FROM category WHERE title="MATH"), (SELECT id FROM status WHERE title="COMING_SOON"));
--
--    -- --in progress
--     INSERT INTO course VALUES(DEFAULT, 'Python', 100, '2020-11-08',  DEFAULT, (SELECT id FROM category WHERE title="PROGRAMMING"), (SELECT id FROM status WHERE title="IN_PROCESS"));
--     INSERT INTO course VALUES(DEFAULT, 'Polish', 100, '2020-11-08',  DEFAULT, (SELECT id FROM category WHERE title="FOREIGN_LANGUAGE"), (SELECT id FROM status WHERE title="IN_PROCESS"));
--     INSERT INTO course VALUES(DEFAULT, 'Basics of geometry', 100, '2020-11-08',  DEFAULT, (SELECT id FROM category WHERE title="MATH"), (SELECT id FROM status WHERE title="IN_PROCESS"));
--
--     -- --completed
--     INSERT INTO course VALUES(DEFAULT, 'C++', 50, '2020-05-08',  DEFAULT, (SELECT id FROM category WHERE title="PROGRAMMING"), (SELECT id FROM status WHERE title="COMPLETED"));
--     INSERT INTO course VALUES(DEFAULT, 'Italian ', 100, '2020-05-08',  DEFAULT, (SELECT id FROM category WHERE title="FOREIGN_LANGUAGE"), (SELECT id FROM status WHERE title="COMPLETED"));
--     INSERT INTO course VALUES(DEFAULT, 'Mathematical analysis', 100, '2020-05-08',  DEFAULT, (SELECT id FROM category WHERE title="MATH"), (SELECT id FROM status WHERE title="COMPLETED"));
--
--    -- --users_course
--     insert into users_course VALUES (1,2,DEFAULT);
--     insert into users_course VALUES (1,4,DEFAULT);
--     insert into users_course VALUES (1,5,DEFAULT);
--     insert into users_course VALUES (2,2,DEFAULT);
--     insert into users_course VALUES (2,4,DEFAULT);
--     insert into users_course VALUES (3,2,DEFAULT);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
