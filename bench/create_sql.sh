#!/bin/bash


function create_projects {
	customer_id=$1

	for (( prj_i = 1; prj_i <= $PROJECTS_PER_CUSTOMER ; prj_i++ ))
	do
		name=${names[$prj_i]}
		(( id = $customer_id * 100 + $prj_i ))
		printf "INSERT INTO project VALUES(%s, %s, '%s', '', '', '%s', 'N', 'Y', 'Y', null);\n" "$id" "$customer_id" "$name" "$name"

		(( rate = $RANDOM % 150 ))
		(( user_id = ($RANDOM % $USERS) + 1000 + 1))

		printf "INSERT INTO project_assignment VALUES(%s, %s, %s, '2011-1-1', '2014-1-1', 'Dev', %s, 'Y',  0, 0, 0, 'F');\n" \
			"$assignment_id" "$id" "$rate" "$user_id"

		(( assignment_id += 1))

	done
}


function create_user_dept {
	for (( dept_i = 1; dept_i <= $DEPTS ; dept_i++ ))
	do
		name=${names[$dept_i]}
		(( id = $dept_i + 1000 ))

		printf "INSERT INTO user_department VALUES(%s, '%s', '%s');\n" "$id" "$name" "$name"
	done
}


function create_users {

	dept=1

	for (( user_i = 1; user_i <= $USERS ; user_i++ ))
	do
		name=${names[$user_i]}
		(( id = $user_i + 1000 ))
		(( dept_id = $dept + 1000 ))

		printf "INSERT INTO users VALUES(%s, '%s', 'pass', '%s', '%s', %s, '', 1, 'Y');\n" "$id" "$name" "$name" "$name" "$dept_id"
		printf "INSERT INTO user_to_userrole VALUES('ROLE_CONSULTANT', %s);\n" "$id"

		(( dept += 1 ))

		if [ "$dept" -gt "$DEPTS" ]; then
			dept=1
		fi
	done
}

function book_hours {
	for (( i = 1; i <= $BOOKED_HOURS ; i++ ))
	do
		entry_date=$(random_date)

		(( hours = $RANDOM % 23 ))	
		(( asg_id = $RANDOM % $assignment_id ))

		comment=${names[$i]}	

		printf "INSERT INTO timesheet_entry VALUES(%s, '%s', '%s', %s, '%s');\n" "$asg_id" "$entry_date" "$entry_date" "$hours" "$comment"
	done
}

function random_date {
	(( year = $RANDOM % 2 + 2011 ))
	(( month= $RANDOM % 12 ))
	(( day=$RANDOM % 30 ))

	if [ "$month" -eq "0" ]; then
		month=1
	fi

	if [ "$day" -eq "0" ]; then
		day=1
	fi

	echo "$year-$month-$day" 
}


IFS=$'\n'
names=( $(cat names.txt | sort -R) )

USERS=400
DEPTS=10
PROJECTS_PER_CUSTOMER=30
CUSTOMERS=1000
BOOKED_HOURS=10000

assignment_id=0
create_user_dept
create_users

for (( x = 1; x <= $CUSTOMERS ; x++ ))
do
	name=${names[$x]}
	(( id = x + 1000 ))
	printf "INSERT INTO customer VALUES(%s, '%s', '', '%s', 'Y');\n" "$id" "$name" "$name"

	create_projects $id
done

book_hours
