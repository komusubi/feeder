drop table channels;
create table channels (
	id int auto_increment,
	name varchar(64),
	primary key(id)
);

drop table messages;
create table messages (
	id int auto_increment,
	text varchar(512),
	created timestamp,	
	primary key (id)
);

drop table aggregations;
create table aggregations(
	id int auto_increment,
	name varchar(64),
	url varchar(256),
	primary key(id)
);

drop table message_refs;
create table message_refs (
	id int auto_increment,
	tweet_id bigint not null,
	message_id int not null,
	channel_id int not null,
	aggregation_id int not null,
	foreign key (message_id) references messages(id),
	foreign key (channel_id) references channels(id),
	foreign key (aggregation_id) references aggregations(id),
	primary key(id)
);

insert into aggregations(name, url) values ('5931', 'http://google.co.jp');

select * from aggregations;
delete from aggregations;