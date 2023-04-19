DROP SCHEMA IF EXISTS eupravadb;
CREATE SCHEMA eupravadb DEFAULT CHARACTER SET utf8;
USE eupravadb;

create table manufacturers
(
    id      int auto_increment
        primary key,
    name    varchar(100) not null,
    country varchar(100) not null,
    constraint manufacturers_pk2
        unique (id)
);

create table users
(
    id                int auto_increment
        primary key,
    email             varchar(45)  not null,
    first_name        varchar(45)  not null,
    last_name         varchar(45)  not null,
    password          varchar(15)  not null,
    jmbg              varchar(15)  not null,
    address           varchar(100) not null,
    phone_num         varchar(45)  not null,
    user_role         varchar(30)  not null,
    registration_date timestamp    not null,
    birth_date        date         not null,
    constraint users_pk1
        unique (email),
    constraint users_phone
        unique (phone_num)
);

create table vax
(
    id              int auto_increment
        primary key,
    name            varchar(100) not null,
    available_num   int          not null,
    manufacturer_id int          not null,
    constraint vax_pk2
        unique (id),
    foreign key (manufacturer_id) references manufacturers (id) on delete cascade
);

create table  news(
    id int auto_increment primary key,
    name varchar(45) not null,
    content varchar(500) not null,
    date_ime DATETIME not null,
    constraint news_pk2
        unique(id)
);

create table infected_news(
    id int auto_increment primary key,
    infected int not null,
    tested int not null,
    hospitalized int not null,
    on_respirator int not null,
    date_time DATETIME not null,
    constraint infected_news_pk2
        unique(id)
);

create table patients_info(
    id int auto_increment primary key,
    vaxxed bit not null,
    recieved_doses tinyint not null,
    last_dose_date datetime not null,
    user_id int not null,
    foreign key (user_id) references users(id) on delete cascade,
    constraint patient_info_pk2
        unique(id)
);

create table applications(
    id int auto_increment primary key,
    date_time DATETIME not null,
    patient_id int not null,
    vax_id int not null,
    foreign key (patient_id) references users(id) on delete cascade,
    foreign key (vax_id) references vax(id) on delete cascade,
    constraint applications_pk2
        unique(id)
);

create table buy_request(
    id int auto_increment primary key,
    amount int not null,
    reason varchar(255),
    date date not null,
    status varchar(100),
    denial_comment varchar(100),
    staff_id int not null,
    vax_id int not null,
    foreign key (staff_id) references users(id) on delete cascade,
    foreign key (vax_id) references vax(id) on delete cascade,
    constraint buy_request_pk2
        unique(id)
)


