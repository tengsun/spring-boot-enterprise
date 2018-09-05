/* for security testing */

insert into sys_user (id, username, password) values (1, 'Jack', 'passw0rd');
insert into sys_user (id, username, password) values (2, 'Rose', 'passw0rd');

insert into sys_role (id, name) values (1, 'ROLE_ADMIN');
insert into sys_role (id, name) values (2, 'ROLE_USER');

insert into sys_user_roles (sys_user_id, roles_id) values (1, 1);
insert into sys_user_roles (sys_user_id, roles_id) values (2, 2);

/* for batch testing */

drop table if exists student;
create table student (id int not null primary key, name varchar(20), age int, address varchar(50));
