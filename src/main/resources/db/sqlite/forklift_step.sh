#1.Get dump.sql from mySQL db
echo "MySQLDUMP starting....."
mysqldump --complete-insert=TRUE --extended-insert=FALSE --protocol=TCP -t -h localhost -P 3306 -u root --password=root petclinic > dump.sql 
sleep 3
echo "done!"
#2.Copy data schema from mySQL to SQLite
echo "Adding schema"
sqlite3 petclinic.sqlite3 < schemaLite.sql
echo "Schema Added!"
sleep 3
echo "Populating DB"
#3.write and translate dump.sql to SQLite
cat dump.sql | perl sql2lite.pl | sqlite3 petclinic.sqlite3
echo "Populated DB!"
echo "SUCCESSFUL"
