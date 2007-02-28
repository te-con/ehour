#!/usr/bin/perl

# Fill eHour db with random data for performance testing
# Thies Edeling -> thies@rrm.net / thies@te-con.nl
#
use DBI;
use Date::Calc qw(:all);

$| = 1;

my $DEPARTMENT_COUNT = 20;
my $USER_COUNT = 100;
my $CUSTOMER_COUNT = 80;
my $PROJECT_COUNT = 250;
my $ASSIGNMENT_PER_USER = 5;
my $TIMESHEET_ENTRIES_PER_DAY = 200;

my $word_list = "/usr/share/dict/words";

my $log_db = "ehour_perf";
my $log_dbhostname = "localhost";
my $log_dbport = 3306;
my $log_dbuser = "perf";
my $log_dbpassword = "me";
my $log_dsn = "DBI:mysql:database=$log_db;host=$log_dbhostname;port=$log_dbport";

$dbh = DBI->connect($log_dsn, $log_dbuser, $log_dbpassword, {AutoCommit => 11}) or die "Couldn't connect: " . DBI->errstr;

# Create random user departments
my @dept_id = insert_random_depts();
# Create random users
my @user_id = insert_random_users(@dept_id);
# Create random customers
my @cust_id = insert_random_customers();
# Create random projects
my @prj_id = insert_random_projects(@cust_id);
# Create random assignments
my @asg_id = create_random_assignments(\@user_id, \@prj_id);
# Create random timesheet entries
create_timesheet_entries(@asg_id);

#$dbh->rollback;
#$dbh->commit;
$dbh->disconnect;

# Create timesheet entries
sub create_timesheet_entries()
{
	print "Creating timesheet entries and comments...";

	my @asg_id = @_;
	my $year = 2006, $day = 1, $month = 1;

	my $sth = $dbh->prepare(qq/INSERT INTO TIMESHEET_ENTRY VALUES(?, ?, ?)/) or die DBI->errstr;

	$entries = 0;

	$asg_i = 0;

	while ($year <= 2007)
	{
		for (my $i = 0; $i < $TIMESHEET_ENTRIES_PER_DAY; $i++)
		{
			$date = "$year/$month/$day\n";

			$asgId = $asg_id[$asg_i++];

			$asg_i = 0 if ($asg_i >= $ASSIGNMENT_PER_USER * $USER_COUNT);
	
			$sth->execute($asgId, $date, rand(24));
			$entries++;

			if ($entries % 4000 == 0)
			{
				print ".";
			}

		}
		($year,$month,$day) = Add_Delta_Days($year,$month,$day, 1);
	}

	print "$entries entries created\n";
}

# Create random assignments
sub create_random_assignments()
{
	print "Creating project assignments...";
	my $user_id = shift;
	my $prj_id = shift;
        my @word = get_random_words($ASSIGNMENT_PER_USER * $USER_COUNT);

	my $sth = $dbh->prepare(qq/INSERT INTO PROJECT_ASSIGNMENT(PROJECT_ID, USER_ID, HOURLY_RATE, DATE_START, DATE_END, DESCRIPTION) VALUES(?, ?, ?, ?, ?, ?)/) or die DBI->errstr;

	my $word_i;

	for (my $i = 0; $i < $USER_COUNT; $i++)
	{
		for (my $j = 0; $j < $ASSIGNMENT_PER_USER; $j++)
		{
			my $desc = ucfirst lc $word[$word_i];
			my $startDate = "2005-01-01";
			my $endDate = "2008-01-01";
			my $hourlyRate = rand(120);
			my $prjId = @$prj_id[int(rand($PROJECT_COUNT))];
			my $userId = @$user_id[$i];

			$sth->execute($prjId, $userId, $hourlyRate, $startDate, $endDate, $desc);
			$asg_id[$word_i] = $dbh->last_insert_id(undef, undef, 'PROJECT_ASSIGNMENT', undef);

			$word_i++;
		}
	}

	print "$word_i created\n";

	return @asg_id;
}
	
# Create random projects
sub insert_random_projects()
{
	print "Creating $PROJECT_COUNT projects...";
        my @cust_ids = @_;
        my @word = get_random_words($PROJECT_COUNT);
        my @cust_id, $name, $code, $default, $r, $defaultCount = 0, @prj_ids;
        my $sth = $dbh->prepare(qq/INSERT INTO PROJECT (CUSTOMER_ID, NAME, PROJECT_CODE, DEFAULT_PROJECT)
                                         VALUES(?, ?, ?, ?)/) or die DBI->errstr;
        my $wordIndex = 0;

        for (my $i = 0; $i < $PROJECT_COUNT; $i++)
        {
                $name = ucfirst lc $word[$wordIndex++];
                $code = uc substr($name, 0, 3);
		
                $r = int(rand($CUSTOMER_COUNT));
                $cust_id = $cust_ids[$r];

		$default = (rand() < 0.08) ? "Y" : "N";
		if ($default eq "Y")
		{
			$defaultCount++;
		}
		
                $sth->execute($cust_id, $name, $code, $default) or die DBI->errstr;
                $prj_id[$i] = $dbh->last_insert_id(undef, undef, 'PROJECT', undef);
        }

	print "done with $defaultCount default projects\r\n";

        return @prj_id;
}

# create some random customers
sub insert_random_customers()
{
	print "Creating $CUSTOMER_COUNT customers...";
        my @word = get_random_words($CUSTOMER_COUNT);
        my @cust_id, $name, $code;
        my $sth = $dbh->prepare("INSERT INTO CUSTOMER (NAME, CODE) VALUES(?, ?)") or die DBI->errstr;

        for (my $i = 0; $i < $CUSTOMER_COUNT; $i++)
        {
                $name = ucfirst lc $word[$i];
                $code = uc substr($name, 0, 3);

                $sth->execute($name, $code) or die DBI->errstr;
                $cust_id[$i] = $dbh->last_insert_id(undef, undef, 'CUSTOMER', undef);
        }
	print "done!\r\n";

        return @cust_id;
}

# Create random users
sub insert_random_users()
{
	print "Creating $USER_COUNT employees...";
	my @dept_ids = @_;
	my @word = get_random_words($USER_COUNT * 4);
	my @user_id, $username, $firstName, $lastName, $deptId;
	my $sth = $dbh->prepare(qq/INSERT INTO USER (USERNAME, PASSWORD, FIRST_NAME, LAST_NAME, DEPARTMENT_ID)
					 VALUES(?, 'test', ?, ?, ?)/) or die DBI->errstr;
	my $sthRole = $dbh->prepare(qq/INSERT INTO USER_TO_USERROLE (ROLE, USER_ID)
					 VALUES('ROLE_CONSULTANT', ?)/) or die DBI->errstr;
	my $wordIndex = 0;
	for (my $i = 0; $i < $USER_COUNT; $i++)
	{
		$username = lc $word[$wordIndex++];
		$firstName = ucfirst lc $word[$wordIndex++];
		$lastName = ucfirst lc $word[$wordIndex++];
		$r = int(rand($DEPARTMENT_COUNT));
		$deptId = $dept_ids[$r];
	#	print "creating $username ($firstName, $lastName) with dept id $deptId\r\n";
		$sth->execute($username, $firstName, $lastName, $deptId) or die DBI->errstr;
		$user_id[$i] = $dbh->last_insert_id(undef, undef, 'USER', undef);

		$sthRole->execute($user_id[$i]);
	}

	print "done!\r\n";
	return @user_id;
}

# create some random departments
sub insert_random_depts()
{
	print "Creating $DEPARTMENT_COUNT departments...";
	my @word = get_random_words($DEPARTMENT_COUNT);
	my @dept_id, $name, $code;
	my $sth = $dbh->prepare("INSERT INTO USER_DEPARTMENT (NAME, CODE) VALUES(?, ?)") or die DBI->errstr;

	for (my $i = 0; $i < $DEPARTMENT_COUNT; $i++)
	{
		$name = ucfirst lc $word[$i];
		$code = uc substr($name, 0, 3);

		$sth->execute($name, $code) or die DBI->errstr;
		$dept_id[$i] = $dbh->last_insert_id(undef, undef, 'USER_DEPARTMENT', undef);
	}

	print "done!\r\n";

	return @dept_id;
}


# get random word from file
sub get_random_words()
{
	open (FH, $word_list) or die;

	my @words;
	my $needed = shift;

	for ($i = 0; $i < $needed; $i++)
	{
		while (<FH>)
		{
			if ($. <= $needed)
			{
				chomp();
				push @words, $_;
			}
			elsif (rand($.) < $needed)
			{
				chomp();
				$words[rand $needed] = $_;
			}
		}
	}

	close FH;
	return @words;
}
