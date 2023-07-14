create table priority
(
    id bigint identity not null,
        name varchar unique not null,
        weight int unique,
        primary key (id)
);

create table todo (
    id bigint identity not null,
    name varchar unique not null,
    description varchar,
    order_number int not null,
    date datetime,
    priority_id bigint,
    foreign key (priority_id)  references priority (id),
    primary key (id)
);