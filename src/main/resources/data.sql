create table tests.exam_answer (id bigint not null auto_increment, actual bit, answer varchar(255), question_id bigint, primary key (id)) engine=InnoDB;
create table tests.exam_question (id bigint not null auto_increment, examination_ticket_id bigint, position integer, question varchar(255), primary key (id)) engine=InnoDB;
create table tests.exam_result (id bigint not null auto_increment, date datetime(6), ticket_id bigint, max_size_answers integer, position integer, size_answers integer, status_exam varchar(255), user_id bigint, primary key (id)) engine=InnoDB;
create table tests.examination_ticket (id bigint not null auto_increment, name varchar(255), primary key (id)) engine=InnoDB;
create table tests.users (id bigint not null auto_increment, password varchar(255), role varchar(255), user_name varchar(255), full_name varchar(255), is_blocked boolean, primary key (id)) engine=InnoDB;

insert into tests.users (id, password, role, user_name, full_name, is_blocked)
VALUES (1, '$2a$10$C71BTWcYChRmXvmydDE.lOxZ8GhOm3Cchs1xYTP.BqcVAURVdReye', 'STUDENT', 'student', 'Фамилия Имя Отчество', false);
insert into tests.users(id, password, role, user_name, full_name, is_blocked)
VALUES (2, '$2a$10$eQGPxe8LzT2m8aQcmdQ57.5XN0R2vXzzuKLGkK8bp9LaFsgs2XtIO', 'TEACHER', 'teacher', 'Фамилия Имя Отчество', false);
insert into tests.users(id, password, role, user_name, full_name, is_blocked)
VALUES (3, '$2a$10$zGMw4sPwUzZDPAWLXQuTr.OdDasdl7rwbaAAo1rO/b0ioPr16Vd6a', 'ADMIN', 'admin', 'Фамилия Имя Отчество', false);

insert into tests.examination_ticket (id,name) values (1, 'java');
insert into tests.examination_ticket (id,name) values (2, 'kotlin');

insert into tests.exam_question (id, question, position, examination_ticket_id) values (1, 'Что такое java?', 1, 1);
insert into tests.exam_question (id,question, position, examination_ticket_id) values (2, 'От какого класса унаследованы все классы java?', 2, 1);

insert into tests.exam_question (id, question, position, examination_ticket_id) values (3, 'Что такое kotlin?',1, 2);
insert into tests.exam_question (id, question, position, examination_ticket_id) values (4, 'Что лучше, Java или Kotlin?',2, 2);

insert into tests.exam_answer (id, answer, actual, question_id) values (1, 'Язык программирования', true, 1);
insert into tests.exam_answer (id, answer, actual, question_id) values (2, 'Язык ситхов', false, 1);

insert into tests.exam_answer (id, answer, actual, question_id) values (3, 'От класса Object', true, 2);
insert into tests.exam_answer (id, answer, actual, question_id) values (4, 'От Адама и Евы', false, 2);

insert into tests.exam_answer (id, answer, actual, question_id) values (5, 'Язык программирования, работающий поверх Java Virtual Machine', true, 3);
insert into tests.exam_answer (id, answer, actual, question_id) values (6, 'Официальный язык племени Кагуя', false, 3);

insert into tests.exam_answer (id, answer, actual, question_id) values (7, 'Конечно Kotlin', true, 4);
insert into tests.exam_answer (id, answer, actual, question_id) values (8, 'Возможно Java', false, 4);