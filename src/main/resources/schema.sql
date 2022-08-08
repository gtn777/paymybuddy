drop TABLE if exists friendship;
drop TABLE if exists buddy_payment;
drop TABLE if exists bank_transfert;
drop TABLE if exists user;
drop TABLE if exists bank_account;
CREATE TABLE bank_account
(
   bank_account_id int (11) NOT NULL AUTO_INCREMENT,
   bank_name varchar (45) NOT NULL,
   account_number BIGINT NOT NULL,
   PRIMARY KEY (`bank_account_id`),
   CONSTRAINT UNIQUE_account_number UNIQUE (account_number)
);
CREATE TABLE user
(
   user_id int (11) NOT NULL AUTO_INCREMENT,
   username varchar (45) NOT NULL,
   password varchar (255),
   balance float NOT NULL,
   role varchar (45) NOT NULL default 'ROLE_USER',
   enabled boolean NOT NULL default 1,
   bank_account_id int (11),
   PRIMARY KEY (`user_id`),
   FOREIGN KEY (bank_account_id) REFERENCES bank_account (bank_account_id),
   CONSTRAINT UNIQUE_username UNIQUE (username)
);
CREATE TABLE friendship
(
   user_id int (11) NOT NULL,
   friend_id int (11) NOT NULL,
   lesser_user int (11) GENERATED ALWAYS AS
   (
      CASE
         WHEN user_id < friend_id    THEN user_id ELSE
      friend_id END
   ),
   greater_user int (11) GENERATED ALWAYS AS
   (
      CASE
         WHEN user_id > friend_id    THEN user_id ELSE
      friend_id END
   ),
   FOREIGN KEY (user_id) REFERENCES user (user_id),
   FOREIGN KEY (friend_id) REFERENCES user (user_id),
   CONSTRAINT CK_friendNotWithSelf CHECK
   (
      user_id <> friend_id
   ),
   CONSTRAINT UQ_friendship_UsersOrdered UNIQUE
   (
      lesser_user,
      greater_user
   ),
   CONSTRAINT UQ_friendship_users UNIQUE
   (
      user_id,
      friend_id
   )
);
CREATE TABLE buddy_payment
(
   buddy_payment_id int (11) NOT NULL AUTO_INCREMENT,
   sender_user_id int (11) NOT NULL,
   recipient_user_id int (11) NOT NULL,
   payment_date DATE NOT NULL,
   amount float (11) NOT NULL,
   PRIMARY KEY (`buddy_payment_id`),
   FOREIGN KEY (sender_user_id) REFERENCES user (user_id),
   FOREIGN KEY (recipient_user_id) REFERENCES user (user_id),
   CONSTRAINT CK_buddyPaymentNotWithSelf CHECK
   (
      sender_user_id <> recipient_user_id
   )
);
CREATE TABLE bank_transfert
(
   bank_transfert_id int (11) NOT NULL AUTO_INCREMENT,
   user_id int (11) NOT NULL,
   amount float (11) NOT NULL,
   user_is_recipient boolean NOT NULL,
   PRIMARY KEY (`bank_transfert_id`),
   FOREIGN KEY (user_id) REFERENCES user (user_id)
);