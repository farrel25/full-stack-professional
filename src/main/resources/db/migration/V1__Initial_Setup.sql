-- CREATE SEQUENCE customer_id_sequence;

CREATE TABLE customer (
--     id BIGINT DEFAULT nextval('customer_id_sequence'::regclass) PRIMARY KEY ,
    id BIGSERIAL PRIMARY KEY ,
    name TEXT NOT NULL ,
    email TEXT NOT NULL ,
    age INTEGER NOT NULL
);


-- PostgreSQL provides the following SQL commands to work with sequences:
--
-- CREATE SEQUENCE: This command is used to create a new sequence. The syntax for creating a sequence is as follows:
--
-- CREATE SEQUENCE sequence_name
--   [ INCREMENT increment ]
--   [ MINVALUE min_value ]
--   [ MAXVALUE max_value ]
--   [ START start ]
--   [ CACHE cache ]
--   [ CYCLE | NO CYCLE ];
--
-- sequence_name: The name of the sequence to be created.
-- INCREMENT: The amount by which the sequence is incremented. The default is 1.
-- MINVALUE: The minimum value of the sequence. The default is 1.
-- MAXVALUE: The maximum value of the sequence. The default is the largest value of the data type.
-- START: The starting value of the sequence. The default is the minimum value of the data type.
-- CACHE: The number of sequence values to cache. The default is 1.
-- CYCLE | NO CYCLE: Specifies whether the sequence should start over when the maximum or minimum value is reached.
-- NEXTVAL: This command is used to get the next value from a sequence. The syntax for getting the next value from a sequence is as follows:
--
-- ---------------
--
-- SELECT NEXTVAL('sequence_name');
-- sequence_name: The name of the sequence from which to get the next value.
-- CURRVAL: This command is used to get the current value of a sequence. The syntax for getting the current value of a sequence is as follows:
--
-- ---------------
--
-- SELECT CURRVAL('sequence_name');
-- sequence_name: The name of the sequence from which to get the current value.
-- SETVAL: This command is used to set the current value of a sequence. The syntax for setting the current value of a sequence is as follows:
--
-- ---------------
--
-- SELECT SETVAL('sequence_name', new_value);
-- sequence_name: The name of the sequence to set the current value for.
-- new_value: The new value to set for the sequence.
-- DROP SEQUENCE: This command is used to delete a sequence. The syntax for deleting a sequence is as follows:
--
-- ---------------
--
-- DROP SEQUENCE sequence_name;
-- sequence_name: The name of the sequence to be deleted.
-- These are the basic SQL commands for working with sequences in PostgreSQL.