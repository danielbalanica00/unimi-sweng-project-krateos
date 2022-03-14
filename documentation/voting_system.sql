CREATE DATABASE IF NOT EXISTS voting_system;

CREATE TABLE IF NOT EXISTS user (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    username varchar(64) NOT NULL UNIQUE,
    password varchar(64) NOT NULL,
    role ENUM('ELECTOR', 'MANAGER') NOT NULL DEFAULT 'ELECTOR'
);
CREATE INDEX username_index ON user(username);

CREATE TABLE IF NOT EXISTS elector (
    id int(11) PRIMARY KEY,
    email varchar(320) NOT NULL,
    first_name varchar(64) NOT NULL,
    last_name varchar(64) NOT NULL,
    FOREIGN KEY (id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS voting_group (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    name varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS elector_group (
    elector_id int(11),
    voting_group_id int(11),
    PRIMARY KEY (elector_id, voting_group_id),
    FOREIGN KEY (elector_id) REFERENCES elector(id),
    FOREIGN KEY (voting_group_id) REFERENCES voting_group(id)
);

CREATE TABLE IF NOT EXISTS voting_session (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    name varchar(64) NOT NULL,
    ends_on datetime NOT NULL,
    active tinyint(1) NOT NULL,
    cancelled tinyint(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS vote (
    elector_id int(11),
    voting_group_id int(11),
    voting_session_id int(11),
    has_voted tinyint(1) NOT NULL,
    PRIMARY KEY(elector_id, voting_group_id, voting_session_id),
    FOREIGN KEY (elector_id) REFERENCES elector(id),
    FOREIGN KEY (voting_group_id) REFERENCES voting_group(id),
    FOREIGN KEY (voting_session_id) REFERENCES voting_session(id)
);

CREATE TABLE IF NOT EXISTS referendum (
    voting_session_id int(11) PRIMARY KEY,
    quorum_id int(11) DEFAULT NULL,
    FOREIGN KEY (voting_session_id) REFERENCES voting_session(id)
);

CREATE TABLE IF NOT EXISTS ordinal (
    voting_session_id int(11) PRIMARY KEY,
    FOREIGN KEY (voting_session_id) REFERENCES voting_session(id)
);

CREATE TABLE IF NOT EXISTS categoric (
    voting_session_id int(11) PRIMARY KEY,
    with_preferences tinyint(1) NOT NULL,
    FOREIGN KEY (voting_session_id) REFERENCES voting_session(id)
);

CREATE TABLE IF NOT EXISTS voting_option (
    id int(11) PRIMARY KEY AUTO_INCREMENT,
    voting_session_id int(11) NOT NULL,
    option_value int(11) NOT NULL,
    has_elements tinyint(1) NOT NULL,
    votes int(11) NOT NULL,
    FOREIGN KEY (voting_session_id) REFERENCES voting_session(id)
);

CREATE TABLE IF NOT EXISTS option_element (
    voting_option_id int(11),
    element_id int(11),
    PRIMARY KEY(voting_option_id, element_id),
    FOREIGN KEY (voting_option_id) REFERENCES voting_option(id),
    FOREIGN KEY (element_id) REFERENCES voting_option(id)
);