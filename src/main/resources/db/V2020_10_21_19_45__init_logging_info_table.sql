CREATE TABLE logging_info (
    data jsonb
);

CREATE UNIQUE INDEX logging_info_tbl_id_idx
    ON logging_info( (data->> 'id') );

