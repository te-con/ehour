This Perl script fills your database with random customers, employees,
timesheet entries for the sake of performance testing.

What it requires is a worldlist for creating names which make a bit of sense.
If you don't have a worldlist already on your system (linux users probably
have: locate words), download one from http://wordlist.sourceforge.net/ and
change the word_list variable in the script. 

Preferably start with a clean database so import the structure from
/sql/ehour.<db>.sql. In the perl script change the values of log_db,
log_dbuser and logdbpassword to match your context.

By default 20 departments are created, 100 employees, 80 customers and 250
projects. Every employee is assigned to 5 projects and for each day between
1/1/2006 and 31/12/2007, 200 timesheet entries are created. You can change the
values in the script, variable names should be self-explanatory.

 - Thies Edeling (thies@rrm.net)

