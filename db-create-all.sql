create table role (
  id                            integer auto_increment not null,
  name                          varchar(255),
  constraint pk_role primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  first_name                    varchar(255),
  last_name                     varchar(255),
  age                           integer not null,
  active                        boolean default false not null,
  email_address                 varchar(255) not null,
  manager_id                    bigint,
  country                       varchar(255),
  city                          varchar(255),
  street_name                   varchar(255),
  street_no                     varchar(255),
  binary_data                   blob,
  date_of_birth                 date,
  created_by                    varchar(255) not null,
  created_date                  timestamp not null,
  last_modified_by              varchar(255) not null,
  last_modified_date            timestamp not null,
  constraint uq_user_email_address unique (email_address),
  constraint pk_user primary key (id)
);

create table user_user (
  user_id                       bigint auto_increment not null,
  constraint pk_user_user primary key (user_id)
);

create table user_role (
  user_id                       bigint not null,
  role_id                       integer not null,
  constraint pk_user_role primary key (user_id,role_id)
);

create index ix_user_manager_id on user (manager_id);
alter table user add constraint fk_user_manager_id foreign key (manager_id) references user (id) on delete restrict on update restrict;

create index ix_user_user_user_1 on user_user (user_id);
alter table user_user add constraint fk_user_user_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_user_user_2 on user_user (user_id);
alter table user_user add constraint fk_user_user_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_role_user on user_role (user_id);
alter table user_role add constraint fk_user_role_user foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_role_role on user_role (role_id);
alter table user_role add constraint fk_user_role_role foreign key (role_id) references role (id) on delete restrict on update restrict;

