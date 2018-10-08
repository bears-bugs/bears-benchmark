alter table user drop constraint if exists fk_user_manager_id;
drop index if exists ix_user_manager_id;

alter table user_user drop constraint if exists fk_user_user_user_1;
drop index if exists ix_user_user_user_1;

alter table user_user drop constraint if exists fk_user_user_user_2;
drop index if exists ix_user_user_user_2;

alter table user_role drop constraint if exists fk_user_role_user;
drop index if exists ix_user_role_user;

alter table user_role drop constraint if exists fk_user_role_role;
drop index if exists ix_user_role_role;

drop table if exists role;

drop table if exists user;

drop table if exists user_user;

drop table if exists user_role;

