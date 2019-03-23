#1.Get dump.sql from mySQL db
mysqldump --complete-insert=TRUE --extended-insert=FALSE --protocol=TCP -t -h localhost -P 3306 -u root -p petclinic > dump.sql 

#2.Copy data schema from mySQL to SQLite
sqlite3 database.sqlite3 < schemaLite.sql

#3.write and translate dump.sql to SQLite
cat dump.sql | perl sql2lite.pl | sqlite3 database.sqlite3

echo "SUCCESSFUL"
