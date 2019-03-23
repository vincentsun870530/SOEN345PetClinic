#!/usr/bin/perl
while (<>){
    s/\\'/''/g;                # Use '' instead of \'
    s/\\"/"/g;                 # Use " instead of \"
    s/\\r\\n/\r\n/g;           # Convert escaped \r\n to literal
    s/\\\\/\\/g;               # Convert escaped \ to literal
    s/ auto_increment//g;      # Remove auto_increment
    s/ IGNORE//g;              # Remove ignore
    s/ UNSIGNED//g;            # Remove unsigned
    s/^[UN]*?LOCK TABLES.*//g; # Remove locking statements
    print;
}