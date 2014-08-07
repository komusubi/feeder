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
    feed int,
    channel int,
    category int,
    url varchar(255),
    foreign key (feed) references feeds(id),
    foreign key (channel) references channels(id),
    foreign key (category) references categories(id)
);

create table messages (
    id int auto_increment primary key,
    text varchar(1024),
    hash varchar(64) unique,
    created timestamp,  
    site_id int,
    foreign key (site_id) references sites(id)
);

create table tweets (
    url varchar(255) primary key,
    message_id int,
    unique (url, message_id),
    foreign key (message_id) references messages(id)
);

