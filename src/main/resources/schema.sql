create TABLE IF NOT EXISTS CITY (
    id UUID NOT NULL,
    city_name varchar(255) NOT NULL,
    CONSTRAINT city_pkey PRIMARY KEY (id)
   );
