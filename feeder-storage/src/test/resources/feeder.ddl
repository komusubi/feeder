drop table messages;

create table messages (
	digest varchar(64),
	text varchar(512),
	created timestamp,	
	primary key (digest)
);

