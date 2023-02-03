USE G3W18;
delimiter | 
CREATE TRIGGER increase_total_sale 
BEFORE INSERT ON Detail_Invoice 
FOR EACH ROW 
BEGIN 
    UPDATE Book SET total_sale = total_sale+1 WHERE book_id = NEW.book_id; 
END; 
|
delimiter;

delimiter | 
CREATE TRIGGER decrease_total_sale 
BEFORE DELETE ON Detail_Invoice 
FOR EACH ROW 
BEGIN 
    UPDATE Book SET total_sale = total_sale-1 WHERE book_id = OLD.book_id; 
END; 
|

delimiter;

delimiter | 
CREATE TRIGGER total_sale_update 
BEFORE UPDATE ON Detail_Invoice 
FOR EACH ROW 
BEGIN 
    
    IF NEW.status = TRUE THEN   
        UPDATE Book SET total_sale = total_sale+1 WHERE book_id = OLD.book_id; 
    ELSEIF NEW.status = FALSE THEN
        UPDATE Book SET total_sale = total_sale-1 WHERE book_id = OLD.book_id; 
    END IF;
 
END; 
|
delimiter;