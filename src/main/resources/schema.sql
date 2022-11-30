drop table if exists phone CASCADE;

drop table if exists user CASCADE;

create table phone (
    id varchar(255) not null,
    citycode varchar(25),
    contrycode varchar(10),
    number varchar(25),
    user_id varchar(255),
    primary key (id));

create table user (
    id varchar(255) not null,
    created timestamp,
    email varchar(120) not null,
    is_active boolean,
    last_login timestamp,
    login_failed integer,
    modified timestamp,
    name varchar(60) not null,
    password varchar(16) not null,
    token varchar(513),
    primary key (id));

alter table user add constraint UK_mail unique (email);
alter table phone add constraint FK_user foreign key (user_id) references user;