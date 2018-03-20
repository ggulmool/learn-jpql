insert into team(team_id, name) values(10001, 'NMS개발팀');
insert into team(team_id, name) values(10002, '인사팀');

insert into member(member_id, username, age, team_id) values(20001, '김남열', 33, 10001);
insert into member(member_id, username, age, team_id) values(20002, '홍길동', 39, 10001);
insert into member(member_id, username, age, team_id) values(20003, '손예진', 37, 10001);
insert into member(member_id, username, age, team_id) values(20004, '소지섭', 42, 10002);

insert into product(product_id, name, price, stockamount) values(30001, '상품1', 30000, 5);
insert into product(product_id, name, price, stockamount) values(30002, '상품2', 50000, 3);

insert into orders(order_id, city, street, zipcode, orderamount, member_id, product_id) values(40001, 'seoul', 'sangam', '123123', 4, 20001, 30001);
insert into orders(order_id, city, street, zipcode, orderamount, member_id, product_id) values(40002, 'seoul', 'nowon', '456523', 2, 20004, 30002);
