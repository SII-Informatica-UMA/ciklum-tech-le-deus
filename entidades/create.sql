create sequence dieta_seq start with 1 increment by 50;
create table dieta (duracion_dias integer, id bigint not null, id_entrenador bigint not null, descripcion varchar(255), nombre varchar(255), objetivo varchar(255), observaciones varchar(255), recomendaciones varchar(255), primary key (id));
create table dieta_alimentos (dieta_id bigint not null, alimentos varchar(255));
create table dieta_cliente_id (dieta_id bigint not null, id_cliente bigint not null, primary key (dieta_id, id_cliente));
alter table if exists dieta_alimentos add constraint FKgp9v0i9bk4etmieq7od891n94 foreign key (dieta_id) references dieta;
alter table if exists dieta_cliente_id add constraint FKrupfl0sbpxy8i3g6231vuup34 foreign key (dieta_id) references dieta;
create sequence dieta_seq start with 1 increment by 50;
create table dieta (duracion_dias integer, id bigint not null, id_entrenador bigint not null, descripcion varchar(255), nombre varchar(255), objetivo varchar(255), observaciones varchar(255), recomendaciones varchar(255), primary key (id));
create table dieta_alimentos (dieta_id bigint not null, alimentos varchar(255));
create table dieta_cliente_id (dieta_id bigint not null, id_cliente bigint not null, primary key (dieta_id, id_cliente));
alter table if exists dieta_alimentos add constraint FKgp9v0i9bk4etmieq7od891n94 foreign key (dieta_id) references dieta;
alter table if exists dieta_cliente_id add constraint FKrupfl0sbpxy8i3g6231vuup34 foreign key (dieta_id) references dieta;
create sequence dieta_seq start with 1 increment by 50;
create table dieta (duracion_dias integer, id bigint not null, id_entrenador bigint not null, descripcion varchar(255), nombre varchar(255), objetivo varchar(255), observaciones varchar(255), recomendaciones varchar(255), primary key (id));
create table dieta_alimentos (dieta_id bigint not null, alimentos varchar(255));
create table dieta_cliente_id (dieta_id bigint not null, id_cliente bigint not null, primary key (dieta_id, id_cliente));
alter table if exists dieta_alimentos add constraint FKgp9v0i9bk4etmieq7od891n94 foreign key (dieta_id) references dieta;
alter table if exists dieta_cliente_id add constraint FKrupfl0sbpxy8i3g6231vuup34 foreign key (dieta_id) references dieta;
