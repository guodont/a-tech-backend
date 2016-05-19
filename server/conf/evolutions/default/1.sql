# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table admin (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  email                     varchar(255) not null,
  sha_password              varbinary(64) not null,
  phone                     varchar(11) not null,
  last_ip                   varchar(45) not null,
  auth_token                varchar(255),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint uq_admin_email unique (email),
  constraint uq_admin_phone unique (phone),
  constraint pk_admin primary key (id))
;

create table advertisement (
  id                        bigint auto_increment not null,
  name                      varchar(45) not null,
  description               varchar(255) not null,
  url                       varchar(255) not null,
  image                     varchar(255) not null,
  click_count               bigint,
  position                  varchar(5),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_advertisement_position check (position in ('FLOAT','AMONG','TOP')),
  constraint pk_advertisement primary key (id))
;

create table album (
  id                        bigint auto_increment not null,
  name                      varchar(45) not null,
  path                      varchar(255) not null,
  user_id                   bigint,
  description               varchar(255) not null,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_album primary key (id))
;

create table answer (
  id                        bigint auto_increment not null,
  question_id               bigint,
  content                   TEXT,
  user_id                   bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_answer primary key (id))
;

create table article (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  content                   TEXT,
  tag                       varchar(45),
  category_id               bigint,
  admin_id                  bigint,
  sort                      integer,
  user_id                   bigint,
  click_count               bigint,
  comment_count             bigint,
  image                     varchar(255),
  article_type              varchar(10),
  article_state             varchar(14),
  article_push_state        varchar(7),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_article_article_type check (article_type in ('WEB','ARTICLE','ACCOMPLISH')),
  constraint ck_article_article_state check (article_state in ('NOT_AUDITED','WAIT_AUDITED','AUDITED','FAILED_AUDITED')),
  constraint ck_article_article_push_state check (article_push_state in ('IS_PUSH','NO_PUSH')),
  constraint pk_article primary key (id))
;

create table blog_post (
  id                        bigint auto_increment not null,
  subject                   varchar(255) not null,
  content                   TEXT,
  user_id                   bigint,
  comment_count             bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_blog_post primary key (id))
;

create table category (
  id                        bigint auto_increment not null,
  pid                       bigint,
  name                      varchar(45) not null,
  category_type             varchar(8),
  image                     varchar(255) not null,
  sort                      integer,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_category_category_type check (category_type in ('ARTICLE','EXPERT','VIDEO','TRADE','QUESTION')),
  constraint pk_category primary key (id))
;

create table comment (
  id                        bigint auto_increment not null,
  article_id                bigint,
  user_id                   bigint,
  content                   TEXT,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_comment primary key (id))
;

create table expert (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  category_id               bigint,
  professional              varchar(45) not null,
  duty                      varchar(45) not null,
  introduction              varchar(255) not null,
  service                   TEXT,
  company                   varchar(45) not null,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_expert primary key (id))
;

create table favorite (
  id                        bigint auto_increment not null,
  be_fav_id                 bigint,
  favorite_type             varchar(8),
  user_id                   bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_favorite_favorite_type check (favorite_type in ('QUESTION','TRADE')),
  constraint pk_favorite primary key (id))
;

create table favorite_question (
  id                        bigint auto_increment not null,
  question_id               bigint,
  user_id                   bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_favorite_question primary key (id))
;

create table favorite_trade (
  id                        bigint auto_increment not null,
  trade_id                  bigint,
  user_id                   bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_favorite_trade primary key (id))
;

create table image (
  id                        bigint auto_increment not null,
  name                      varchar(255) not null,
  old_name                  varchar(255) not null,
  src                       varchar(255) not null,
  user_id                   bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_image primary key (id))
;

create table link (
  id                        bigint auto_increment not null,
  name                      varchar(45) not null,
  url                       varchar(45) not null,
  image                     varchar(255) not null,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_link primary key (id))
;

create table post_comment (
  id                        bigint auto_increment not null,
  blog_post_id              bigint,
  user_id                   bigint,
  content                   TEXT,
  constraint pk_post_comment primary key (id))
;

create table question (
  id                        bigint auto_increment not null,
  category_id               bigint,
  title                     varchar(45) not null,
  content                   TEXT,
  click_count               bigint,
  like_count                bigint,
  expert_id                 bigint,
  user_id                   bigint,
  question_audit_state      varchar(12),
  question_resolve_state    varchar(12),
  images                    varchar(255),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_question_question_audit_state check (question_audit_state in ('WAIT_AUDITED','FAILED','AUDITED')),
  constraint ck_question_question_resolve_state check (question_resolve_state in ('RESOLVED','WAIT_RESOLVE')),
  constraint pk_question primary key (id))
;

create table trade (
  id                        bigint auto_increment not null,
  title                     varchar(45) not null,
  description               varchar(255) not null,
  user_id                   bigint,
  click_count               bigint,
  like_count                bigint,
  end_time                  datetime,
  trade_type                varchar(6),
  category_id               bigint,
  trade_state               varchar(12),
  images                    varchar(255),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_trade_trade_type check (trade_type in ('SUPPLY','DEMAND')),
  constraint ck_trade_trade_state check (trade_state in ('WAIT_AUDITED','FAILED','AUDITED')),
  constraint pk_trade primary key (id))
;

create table trend (
  id                        bigint auto_increment not null,
  user_id                   bigint,
  content                   varchar(255) not null,
  images                    varchar(255),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_trend primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  email                     varchar(255),
  sha_password              varbinary(64) not null,
  user_type                 varchar(20) not null,
  address                   varchar(255),
  real_name                 varchar(45),
  phone                     varchar(11) not null,
  name                      varchar(45) not null,
  avatar                    varchar(45),
  industry                  varchar(45),
  scale                     varchar(45),
  last_ip                   varchar(45) not null,
  auth_token                varchar(255),
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint ck_user_user_type check (user_type in ('PUBLIC','EXPERT')),
  constraint uq_user_email unique (email),
  constraint uq_user_phone unique (phone),
  constraint uq_user_name unique (name),
  constraint pk_user primary key (id))
;

create table video (
  id                        bigint auto_increment not null,
  name                      varchar(45) not null,
  description               TEXT,
  path                      varchar(255) not null,
  admin_id                  bigint,
  category_id               bigint,
  click_count               bigint,
  version                   bigint not null,
  when_created              datetime not null,
  when_updated              datetime not null,
  constraint pk_video primary key (id))
;

alter table album add constraint fk_album_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_album_user_1 on album (user_id);
alter table answer add constraint fk_answer_question_2 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_answer_question_2 on answer (question_id);
alter table answer add constraint fk_answer_user_3 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_answer_user_3 on answer (user_id);
alter table article add constraint fk_article_category_4 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_article_category_4 on article (category_id);
alter table article add constraint fk_article_admin_5 foreign key (admin_id) references admin (id) on delete restrict on update restrict;
create index ix_article_admin_5 on article (admin_id);
alter table article add constraint fk_article_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_article_user_6 on article (user_id);
alter table blog_post add constraint fk_blog_post_user_7 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_blog_post_user_7 on blog_post (user_id);
alter table comment add constraint fk_comment_article_8 foreign key (article_id) references article (id) on delete restrict on update restrict;
create index ix_comment_article_8 on comment (article_id);
alter table comment add constraint fk_comment_user_9 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_comment_user_9 on comment (user_id);
alter table expert add constraint fk_expert_user_10 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_expert_user_10 on expert (user_id);
alter table expert add constraint fk_expert_category_11 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_expert_category_11 on expert (category_id);
alter table favorite add constraint fk_favorite_user_12 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_favorite_user_12 on favorite (user_id);
alter table favorite_question add constraint fk_favorite_question_question_13 foreign key (question_id) references question (id) on delete restrict on update restrict;
create index ix_favorite_question_question_13 on favorite_question (question_id);
alter table favorite_question add constraint fk_favorite_question_user_14 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_favorite_question_user_14 on favorite_question (user_id);
alter table favorite_trade add constraint fk_favorite_trade_trade_15 foreign key (trade_id) references trade (id) on delete restrict on update restrict;
create index ix_favorite_trade_trade_15 on favorite_trade (trade_id);
alter table favorite_trade add constraint fk_favorite_trade_user_16 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_favorite_trade_user_16 on favorite_trade (user_id);
alter table image add constraint fk_image_user_17 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_image_user_17 on image (user_id);
alter table post_comment add constraint fk_post_comment_blogPost_18 foreign key (blog_post_id) references blog_post (id) on delete restrict on update restrict;
create index ix_post_comment_blogPost_18 on post_comment (blog_post_id);
alter table post_comment add constraint fk_post_comment_user_19 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_post_comment_user_19 on post_comment (user_id);
alter table question add constraint fk_question_category_20 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_question_category_20 on question (category_id);
alter table question add constraint fk_question_expert_21 foreign key (expert_id) references user (id) on delete restrict on update restrict;
create index ix_question_expert_21 on question (expert_id);
alter table question add constraint fk_question_user_22 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_question_user_22 on question (user_id);
alter table trade add constraint fk_trade_user_23 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_trade_user_23 on trade (user_id);
alter table trade add constraint fk_trade_category_24 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_trade_category_24 on trade (category_id);
alter table trend add constraint fk_trend_user_25 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_trend_user_25 on trend (user_id);
alter table video add constraint fk_video_admin_26 foreign key (admin_id) references admin (id) on delete restrict on update restrict;
create index ix_video_admin_26 on video (admin_id);
alter table video add constraint fk_video_category_27 foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_video_category_27 on video (category_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table admin;

drop table advertisement;

drop table album;

drop table answer;

drop table article;

drop table blog_post;

drop table category;

drop table comment;

drop table expert;

drop table favorite;

drop table favorite_question;

drop table favorite_trade;

drop table image;

drop table link;

drop table post_comment;

drop table question;

drop table trade;

drop table trend;

drop table user;

drop table video;

SET FOREIGN_KEY_CHECKS=1;

