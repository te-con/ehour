eHour
-----

eHour is a webbased time registration tool for consultancy companies and other project based businesses.
The primary objective is to keep time registration as simple as needed while still being very effective at measuring and reporting the amount of time your team spends on a project.

Install instructions
--------------------
Make sure you have a Java 5 runtime environment installed. Download an install the latest version
from http://java.sun.com/javase/downloads/index.jsp

To start eHour go to the bin/<your OS> directory.
For windows execute the eHour.bat or run eHour-installService if you want to install eHour as a service.
Make sure you the path you extract eHour to doesn't contain any spaces.
c:\program files\ehour will not work, try using c:\ehour instead.

Linux users execute "ehour.sh start" to start eHour.

Log files can be found in the /logs directory.

When eHour is started browse to http://localhost:8000/ to access the web interface.

First time usage documentation can be found at http://www.ehour.nl/first_time_usage.phtml and http://www.ehour.nl/doc.phtml


Upgrade instructions from v0.7.x to v0.8.3
------------------------------------------
Unpack the v0.8.3 archive to a new directory. Copy the ehourDb directory from the root of your previous eHour installation to the
root of the directory where you unpacked 0.8.3. That's it! Start eHour as explained in the installation instructions above.

www.ehour.nl
