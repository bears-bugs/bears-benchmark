--
-- PostgreSQL schema addition for repair on specific nodes
--


ALTER TABLE "repair_unit" 
ADD "blacklisted_tables" TEXT [];
