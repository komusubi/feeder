drop table tweets;
drop table messages;
drop table sites;
drop table feeds;
drop table categories;
drop table channels;

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

create table categories(
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

insert into feeds values (1, 'SCRAPE');
insert into feeds values (2, 'RSS');
insert into channels values (0, 'jal');
insert into channels values (1, '5971');
insert into channels values (2, '5931');
insert into channels values (3, 'jmb');
insert into categories values (1, 'WEATHER');
insert into categories values (2, 'INFORMATION');
insert into categories values (3, 'TOUR');
insert into categories values (4, 'INVESTER RELATIONS');
insert into categories values (5, 'PRESS RELEASE');
insert into categories values (6, 'FLIGHT STATUS');
insert into sites values (null, '運行の見通し(国内線)', 1, 1, 1, 'http://www.jal.co.jp/cms/other/ja/weather_info_dom.html');
insert into sites values (null, '運行の見通し(国際線)', 1, 2, 1, 'http://www.jal.co.jp/cms/other/ja/weather_info_int.html');
insert into sites values (null, 'JALからのお知らせ', 2, 0, 2, 'http://rss.jal.co.jp/f4728/index.rdf');
insert into sites values (null, '国内線のお知らせ', 2, 1, 2, 'http://rss.jal.co.jp/f4746/index.rdf');
insert into sites values (null, '国際線のお知らせ', 2, 2, 2, 'http://rss.jal.co.jp/f4747/index.rdf');
insert into sites values (null, 'JALマイレージバンクのお知らせ', 2, 3, 2, 'http://rss.jal.co.jp/f4749/index.rdf');