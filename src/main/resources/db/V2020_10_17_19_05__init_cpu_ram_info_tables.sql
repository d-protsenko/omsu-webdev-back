CREATE TABLE cpu_info (
    data jsonb
);

CREATE UNIQUE INDEX cpu_info_tbl_id_idx
    ON cpu_info( (data->> 'id') );

CREATE TABLE ram_info (
    data jsonb
);

CREATE UNIQUE INDEX ram_info_tbl_id_idx
    ON ram_info( (data->> 'id') );
