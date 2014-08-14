--liquibase formatted sql

--changeset jun.ozeki:1
create table feeds (
    id int primary key,
    name varchar(64)
);

create table channels (
    id int primary key,
    name varchar(64)
);

create table categories (
    id int primary key,
    name varchar(64)
);

create table sites (
    id int auto_increment primary key,
    name varchar(255),
    feed int not null,
    channel int not null,
    category int not null,
    url varchar(255),
    foreign key (feed) references feeds(id),
    foreign key (channel) references channels(id),
    foreign key (category) references categories(id)
);

create table messages (
    id int auto_increment primary key,
    site_id int not null,
    created timestamp,  
    foreign key (site_id) references sites(id),
);

create table scripts (
	hash varchar(64) primary key,
	text varchar(1024),
	url varchar(255) unique,
    message_id int not null,
	foreign key (message_id) references messages(id)
);



