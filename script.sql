DROP SCHEMA IF EXISTS eupravadb;
CREATE SCHEMA eupravadb DEFAULT CHARACTER SET utf8;
USE eupravadb;

create table infected_news
(
    id            int auto_increment
        primary key,
    infected      int      not null,
    tested        int      not null,
    hospitalized  int      not null,
    on_respirator int      not null,
    date_time     datetime not null,
    constraint infected_news_pk2
        unique (id)
);

create table manufacturers
(
    id      int auto_increment
        primary key,
    name    varchar(100) not null,
    country varchar(100) not null,
    constraint manufacturers_pk2
        unique (id)
);

create table news
(
    id        int auto_increment
        primary key,
    name      varchar(45)  not null,
    content   varchar(500) not null,
    date_time datetime     not null,
    constraint news_pk2
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
    constraint users_phone
        unique (phone_num),
    constraint users_pk1
        unique (email)
);

create table patients_info
(
    id             int auto_increment
        primary key,
    vaxxed         bit      not null,
    received_doses tinyint  not null,
    last_dose_date datetime null,
    user_id        int      not null,
    constraint patient_info_pk2
        unique (id),
    constraint patients_info_ibfk_1
        foreign key (user_id) references users (id)
            on delete cascade
);

create index user_id
    on patients_info (user_id);

create table vax
(
    id              int auto_increment
        primary key,
    name            varchar(100) not null,
    available_num   int          not null,
    manufacturer_id int          not null,
    constraint vax_pk2
        unique (id),
    constraint vax_ibfk_1
        foreign key (manufacturer_id) references manufacturers (id)
            on delete cascade
);

create table applications
(
    id         int auto_increment
        primary key,
    date_time  datetime not null,
    patient_id int      not null,
    vax_id     int      not null,
    constraint applications_pk2
        unique (id),
    constraint applications_ibfk_1
        foreign key (patient_id) references users (id)
            on delete cascade,
    constraint applications_ibfk_2
        foreign key (vax_id) references vax (id)
            on delete cascade
);

create index patient_id
    on applications (patient_id);

create index vax_id
    on applications (vax_id);

create table buy_request
(
    id             int auto_increment
        primary key,
    amount         int          not null,
    reason         varchar(255) null,
    date_time      datetime     not null,
    denial_comment varchar(100) null,
    staff_id       int          not null,
    vax_id         int          not null,
    status         varchar(30)  not null,
    constraint buy_request_pk2
        unique (id),
    constraint buy_request_ibfk_1
        foreign key (staff_id) references users (id)
            on delete cascade,
    constraint buy_request_ibfk_2
        foreign key (vax_id) references vax (id)
            on delete cascade
);

create index staff_id
    on buy_request (staff_id);

create index vax_id
    on buy_request (vax_id);

create index manufacturer_id
    on vax (manufacturer_id);

CREATE FUNCTION get_total_infected(news_id INT) RETURNS INT
READS SQL DATA
DETERMINISTIC
BEGIN
  DECLARE total_infected INT;
  SELECT SUM(infected) INTO total_infected FROM infected_news WHERE date_time <= (select date_time from infected_news
                                                                                                  where id = news_id);
  RETURN total_infected;
END;

