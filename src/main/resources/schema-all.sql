DROP TABLE IF EXISTS google_activity;

CREATE TABLE google_activity (
    id bigint,
    eff_date date,
    post_id varchar(50),
    post_url varchar(100),
    title varchar(250),
    replies integer,
    plusoners integer,
    resharers integer,
    attachment_url varchar(250),
    attachment_type varchar(50),
    published timestamp,
    updated timestamp
);
