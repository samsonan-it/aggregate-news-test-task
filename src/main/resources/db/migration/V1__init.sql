CREATE TABLE "USER"
(
  USERNAME text NOT NULL,
  PASSWORD text NOT NULL,
  ROLE text NOT NULL,
  CONSTRAINT user_pkey PRIMARY KEY (USERNAME)
);

CREATE SEQUENCE NEWS_FEED_ID_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE NEWS_FEED
(
  ID integer NOT NULL DEFAULT nextval('NEWS_FEED_ID_SEQ'::regclass),
  NAME text NOT NULL,
  URL text NOT NULL,
  OWNER_USERNAME text NOT NULL,
  PARSE_RULE text,
  CONSTRAINT news_feed_pkey PRIMARY KEY (ID),
  CONSTRAINT news_feed_user_fkey FOREIGN KEY (OWNER_USERNAME)
      REFERENCES "USER" (USERNAME) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE NEWS_ITEM_ID_SEQ
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;

CREATE TABLE NEWS_ITEM
(
  ID integer NOT NULL DEFAULT nextval('NEWS_ITEM_ID_SEQ'::regclass),
  TITLE text NOT NULL,
  DESCRIPTION text,
  LINK text,
  NEWS_FEED_ID integer NOT NULL,
  CONSTRAINT news_item_pkey PRIMARY KEY (ID),
  CONSTRAINT news_item_feed_fkey FOREIGN KEY (NEWS_FEED_ID)
      REFERENCES NEWS_FEED (ID) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


INSERT INTO "USER" (USERNAME, PASSWORD, ROLE)
            values ('demo', 'demo', 'ROLE_USER');

INSERT INTO "USER" (USERNAME, PASSWORD, ROLE)
            values ('user', 'user', 'ROLE_USER');

INSERT INTO "USER" (USERNAME, PASSWORD, ROLE)
            values ('admin', 'admin', 'ROLE_ADMIN');
