CREATE TABLE request_analytics
(
    id                   BIGSERIAL PRIMARY KEY,
    request_id           UUID         NOT NULL UNIQUE,
    occurred_at          TIMESTAMPTZ  NOT NULL,
    method               VARCHAR(10)  NOT NULL,
    path                 VARCHAR(2048) NOT NULL,
    query_keys           VARCHAR(512),
    status_code          SMALLINT     NOT NULL,
    duration_ms          BIGINT       NOT NULL,
    ip_address           VARCHAR(45)  NOT NULL,
    user_id              INTEGER      REFERENCES users (id) ON DELETE SET NULL,
    scheme               VARCHAR(10)  NOT NULL,
    host                 VARCHAR(255) NOT NULL,
    user_agent           VARCHAR(1024),
    referrer             VARCHAR(2048),
    accept_language      VARCHAR(255),
    request_content_type VARCHAR(100),
    response_content_type VARCHAR(100)
);

CREATE INDEX request_analytics_occurred_at_idx ON request_analytics (occurred_at);
CREATE INDEX request_analytics_path_occurred_at_idx ON request_analytics (path, occurred_at);
CREATE INDEX request_analytics_status_occurred_at_idx ON request_analytics (status_code, occurred_at);
CREATE INDEX request_analytics_user_occurred_at_idx ON request_analytics (user_id, occurred_at);
CREATE INDEX request_analytics_ip_occurred_at_idx ON request_analytics (ip_address, occurred_at);
