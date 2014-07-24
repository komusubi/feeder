--liquibase formatted sql

--changeset jun.ozeki:1
create table feeds (
    id int,
    name varchar(64),
    primary key(id)
);

create table channels (
    id int,
    name varchar(64),
    primary key(id)
);

create table categories (
    id int,
    name varchar(64),
    primary key(id)
);

create table sites (
    id int auto_increment,
    name varchar(255),
    feed int,
    channel int,
    category int,
    url varchar(255),
    primary key (id),
    foreign key (feed) references feeds(id),
    foreign key (channel) references channels(id),
    foreign key (category) references categories(id)
);

create table messages (
    id int auto_increment,
    text varchar(1024),
    created timestamp,  
    site_id int,
    primary key (id),
    foreign key (site_id) references sites(id)
);

create table tweets (
    url varchar(255) unique,
    message_id int,
    foreign key (message_id) references messages(id)
);
create index tweet_idx on tweets(message_id);

