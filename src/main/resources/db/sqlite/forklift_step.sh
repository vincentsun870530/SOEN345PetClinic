#!/bin/bash
#1.Get dump.sql from mySQL db
echo "MySQLDUMP starting....."
#mysql 8.0
#mysqldump --column-statistics=0 --protocol=TCP -t -h localhost -P 3306 -u root --password=root petclinic > dump.sql
#mysql 5.7
mysqldump --protocol=TCP -t -h localhost -P 3306 -u root --password=root petclinic > dump.sql
sleep 3
echo "done!"
#2.Copy data schema from mySQL to SQLite
echo "Adding schema"
sqlite3 petclinic.sqlite3 < schemaLite.sql
echo "Schema Added!"
sleep 3
echo "Populating DB"
#3.write and translate dump.sql to SQLite
./mysql2sqlite dump.sql | sqlite3 petclinic.sqlite3
echo "Populated DB!"
echo "SUCCESSFUL"
