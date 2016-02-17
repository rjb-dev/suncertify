# suncertify
All About Improvement Project

This project is a basic interface for saving and booking tradespeople over a network. It was the final project
for the java part of my software development diploma and I am uploading it here so potential employers have
an example of my code to look at. The rest of this readme is the userguide I wrote for the project, followed by
the design choices document I also submitted.

Userguide
All About Improvement Project
17/12/2015

Opening the project

Open Command Prompt in Windows and navigate to the directory the submitted runme.jar is situated in. 
You'll need to type the following into Command Prompt: java -jar runme.jar <mode>
The mode options are: alone, server, or you can leave the mode blank. 
Alone mode runs the standalone client. Server starts the server application and no mode 
flag starts a networked client if there is already an instance of the server running. It is possible to start 
multiple networked clients. There is more information on the possible modes in the paragraphs below.

Running in standalone mode

Once you have entered 'java -jar runme.jar alone' a dialog box will open requesting the database location. 
Either enter the fully qualified location into the box, or use the ... (browse) button to open a file chooser 
and navigate to the file. Once you have selected the database file, choose connect and the All About Improvement 
application will launch the main window. Once you have specified the file location, it will be saved for when you 
next run the program.

Running in server mode with networked clients

First you need to start a server instance with 'java -jar runme.jar server'. This opens the server window of 
the application which requests the database location and the server port number. Enter the database location 
in the text box or use the browse button to navigate to the database file. There is a default port provided, 
which you can change if necessary. Once you have entered the correct details, click the 'Start server' button. 
The input fields and start button will then be greyed out and the label at the bottom of the window should display 
'server running.' Note that after a minute of no further activity, the server window will time-out and close.

To connect clients to the server via a network, type 'java -jar runme.jar' in a new Command Prompt window while 
the server is running. This will open a dialog box similar to the one you see when running in standalone mode. 
In this case the database location should be the IP address of the server or localhost if you are running the client 
on the same machine. As with the server, a default port is provided which you may change. Once you have configured 
the options, click connect to run the client application. You can repeat this process to run as many clients as you like.

Using the All About Improvement application

When you open the application, the main window will display a list of records in the database. 
Following is a list of how to perform the various functions associated with the application:

Search

You can simply browse the listed records or use the combo boxes to filter your search by business name or location. 
Using the combo box multiple times may result in the boxes stopping to filter. If this happens, you can use the 
keyword search box to refine your search, or restart the application. (This is a known bug which will be resolved 
in a future release.) The keyword search is not case-sensitive and will return all results in which your search is 
present, even if the search item is a partial word in the result (E.g. 'model' will return 'remodeling'). It is also 
possible to search for multiple words in the same record by entering several keywords separted by commas, (E.g. model, roof).

Book a Tradesperson (Tradie)

To make a booking, click on the booking in the record list, (once selected it will turn pink), and click the 
'Book Tradie' button. A dialog box will pop up requesting a customer ID. Add the 8 digit customer ID and select OK to book, 
or Cancel to return to the main window. You'll see the customer ID appear in the right-hand column of table, indicating that 
the record is booked. No other clients will then be able to book that record. 
Note: Please ensure that you have entered the correct customer number and that it is no longer than 8 digits. 
Entering a longer number will cause a database error and the following record will be corrupted. 

Release a Booking

Once the work has been completed, select the record to release by clicking the record to highlight and then click the 
'Release booking' button. The customer ID will disappear from the right-hand column, indicating that that tradesperson 
is once again available for work.

Administrative Tasks - Add Record, Update Record and Delete Record

To add a new tradesperson to the database, click the 'Add Record' button in the pink strip just below the table. 
An input box will appear where you can enter the new details for a record. Click OK to save and the new record 
will appear at the bottom of the table. You will also receive a notification of the new record number.

To update a record, simply select the record to update in the table and click the 'Update Record' button. 
You can modify the details as necessary, as long as you keep your field lengths within the required maximum values. These are:
Name: 32 characters
Location: 64 characters
Specialties: 64 characters
Number of Staff: 6 characters
Rate: 8 characters
Failure to adhere to the maximums will result in database corruption, so be careful!

Deletion of a record involves selecting a record and clicking 'Delete Record'. You will receive a warning message 
giving you the opportunity to cancel the deletion before the record is removed from the database.


Design Choices
All About Improvement Project
16/12/2015

My initial choices about design were based on the choices made in the sample project presented in the SCJP textbook 
which is the basis for the SJD module at Computer Power Plus. While following the design of the sample project meant 
that the code in my project was initially and perhaps still is more complicated than necessary, I felt that following 
a working model had huge advantages for a beginning programmer like myself, and that those advantages outweighed any 
concerns I had about over-complicating the solution.

I chose sockets for a network solution because RMI is a java only solution whereas sockets are a standard networking 
solution. I felt that by choosing a standard solution I would be learning something that would be adaptable to many 
programming situations, not just those using the Java language.

My program is split into four packages: db, for the code related to the the server, logical locking of records and 
database access; network, for the code relating to the sockets network; direct, for direct access when the database is on 
the same machine as the client; and gui, containing all the code relating to the parts of the program the user interacts with.
I will talk in the following paragraphs about design decisions related to each of the packages except direct, which contains
only one class providing direct access to the database and as such is fairly self-explanatory.

The db package contains the supplied interface, DB, which is implemented by the Data class, as required. The Data class 
is a facade for two other classes, DatabaseAccess and LockerRoom. DatabaseAccess provides all of the physical access to the 
database file. The advantage of containing this in one class is that if All About Improvement decides to change to a 
commercial database in future, this is the class that would need to be re-written to connect to new database. 
While there could be minor revisions other classes, the bulk of the change would happen in this class only. The LockerRoom 
class provides logical locking of records for modifications and deletions to the database. In addition to these main classes,
the db contains the DuplicateKey and RecordNotFound exceptions required by the interface and a class called DBRecord which 
is used to contain database records as objects.

The network package contains the DBConnector class which is used to make a connection to the server. The two classes 
DBCommand and DBResult are serialisable classes used for sending (DBCommand) and receiving (DBResult) objects across the 
sockets network. The DBCommand object is an example of the Command pattern as it encapsulates a request in an object. To do 
this it uses the enum SocketCommand. The commands are interpreted by the switch statement in the DBSocketRequest class, while 
the work of sending and receiving required information happens in the getResultFor method of the DBSocketClient class. 
DBSocketClient also implements the DB interface, ensuring that all required functionality is provided by the sockets network 
solution. I chose to adapt all the classes used in the sockets solution of the example project in the textbook to my project, 
as I considered it a very clever solution which I couldn't hope to better and felt that by re-implementing that solution 
I would learn a great deal (which has proved to be the case!).

The gui package is the most complicated as it includes all the configuration for the connection to the database (whether 
networked or standalone) as well as the gui windows and the code that makes a connection to the database. Given that all of 
these functions relate to the user, I felt that despite being complicated, it made sense to keep all these classes in the 
same package. Application Runner is the main method that launches the All About Improvement application. It takes in the 
command line arguments and launches the application in the correct mode. From there the classes ApplicationMode, ConfigOptions,
ConnectionType, SavedConfiguration and OptionUpdate all relate to configuring the necessary parameters for making a 
connection to the database in the specified mode. This might seem like a lot of classes to have, but it seemed that keeping 
things as separated as possible was a good idea. If the user chooses to start in server mode, the gui ServerWindow JFrame 
is opened and the user has the opportunity to enter relevant options. NetworkStarterSockets is there to start the server if 
that information is valid.

While I thought it made sense to validate the allowed input for the update and add methods, as well as book, I decided that 
at this stage it would add unnessesary complication to the code. Although All About Improvement is planning to creat a web 
app in future, at the moment only trained staff who know what the valid input should be are using the software. Once a web 
client is developed to extend the application to the public, validation will be required. But as that will also require a 
new GUI class, which may contain different form fields it makes sense to delay writing the appropriate validation code until 
the exact specifications for the web-based GUI have been worked out.

I had a lot of problems implementing the functionality required by this project, and while everything works, I expect there
to still be bugs in the code. I decided to submit my project regardless at this stage as I feel that while the design and 
implementation could still be improved on, I have met the requirements of the All About Improvement project and that anything
extra would be out of scope.









