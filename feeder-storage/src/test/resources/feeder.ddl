drop table channel;

create table channel (
	id int,
	name varchar(64),
	primary key(id)
);

drop table messages;

create table messages (
	digest varchar(64),
	channel_id int,
	text varchar(512),
	created timestamp,	
	primary key (digest),
	foreign key (channel_id) references channel(id)
);


