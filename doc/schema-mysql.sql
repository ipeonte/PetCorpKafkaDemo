drop table pet;
drop table client_pet_ref;
drop table client;

create table pet (
	id int not null auto_increment,
	name varchar(255) not null,
	sex varchar(255) not null,
	vaccinated char(1) not null default 'N',
	adopted char(1) not null default 'N',
	primary key(id)
);

create table client (
        id int not null auto_increment,
        first_name varchar(255) not null,
        last_name varchar(255) not null,
        primary key(id)
);

create table client_pet_ref (
	id int not null auto_increment,
	pet_id int not null,
	client_id int not null,
	registered timestamp not null,

	foreign key (client_id) references client(id) on delete cascade,

	primary key(id)
);

insert into pet(id, name, sex, vaccinated) values(1, 'Lucky', 'F', 'Y');
insert into pet(id, name, sex, vaccinated) values(2, 'Bella', 'F', 'Y');
insert into pet(id, name, sex, vaccinated) values(3, 'Dino', 'M', 'N');
insert into pet(id, name, sex, vaccinated) values(4, 'Rex', 'M', 'Y');

insert into client(id, first_name, last_name) values(1, 'Ritchie', 'Blackmore');
insert into client(id, first_name, last_name) values(2, 'David', 'Coverdale');
insert into client(id, first_name, last_name) values(3, 'Jon', 'Lord');
insert into client(id, first_name, last_name) values(4, 'Ian', 'Paice');

