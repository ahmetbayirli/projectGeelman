# Project Description

Project Geelman is a personal webpage template. It supports bootstrap 4.1.0 components and fully responsive. There is a content management interface using CKEditor.

There is no need to set up an application server or database server. It has been developed using PlayFramework 1.2.5 and uses H2 file database.

You can see the template on live in these web pages;
* http://www.ardenarastirma.com/

Keywords: Personal web page, java, responsive, content management (CM)

Prerequisites: JDK1.7.x, PlayFramework1.2.5

# Acknowledgements

* Special thanks for background images to http://www.aljanh.net/beach-with-stones-wallpapers
* Special thanks for navigation bar theme to https://work.smarchal.com/twbscolor/
* Special thanks for dropdown and hide on scroll functionality on the navigation bar to https://www.smartmenus.org/ (this option is canceled currently)

# First Deployment

The project has a secret hardcoded superadmin user. In CacheHelper.checkSuperAdminExists() method you need to change username "superadmin" and password "supersecret" to your desired way. This user is not configurable any other way, to protect the freelancer developer from customer related issues.

You can build and run the project at your local computer and create an H2 DB file. Then you can upload it to the production server.

If you don't want to use H2 file database you can configure it to use remote MySQL DB too, using the conf/application.conf file. You just need to change the JDBC URL

Download and open PlayFramework1.2.5 distribution.

Set play run mode either as "dev" or "geelman" for development and for production respectively by using the command "play id"

After you set to the desired mode, start the project by using "play run <projectfolderadress>"

In dev mode HTTP port is 9000 and in geelman mode 80

When the project is fully built and up, superadmin must connect and create the admin.

Then Admin (the customer or webmaster) can add, update, view or delete as much as webpages as he/she wants.

PageLogo and Favicon are under ./<projectfolder>/public/images as logo.png favicon.png you can change them anytime you want. If the project is on production already, you need to restart the app



# Notes

* Administration interfaces can be accessed only if you know the address
* http://server:port/admincontroller/superadminlogin for superadmin
* http://server:port/admincontroller/login for admin
* http://server:port/admincontroller/index for administration
* If an admin tries the wrong password 3 times then he/she is blocked and you(superadmin) need to reset the cache
* Just outside of the project folder, there will be automatically created folders.
  - ./data/ *.db files for db
  - ./data/attachments/uuid files for images.

* You can crontab and backup ./data folder to dropbox periodically. To return from the backup simply copy and past *.db files and attachments to necessary folders.
* To optimize the DB size, you can re-use same images in different pages by noting the id of the image.


# Drawbacks

* The user (admin) is needed to know HTML in basic level. There is CKEditor in CM interface and most of the time you need to use the Source option in order to be able to successfully add bootstrap components.
* Once you save the page and come back for an update, you will see that all of the indentations in HTML source is lost. You need to reserve your page's inner HTML in a different location/notepad for future uses.

# New Features 18.6.2019

Administration screens are refactored for a better UX
Entry and EntryKeyword options has been added. Users with lack of HTML knowledge may use these options for list of repeating entries.
User is needed to determine a keyword and a style for the list.
Then he/she may add entries to that list.
Entry list must be added to the page content with entrylist:keyword:entrylist tag
Every list is shown according to the style; paragraph, accordion, tabbed
Entry lists can be inserted on each other, like list1:accordion in a list2:tabbed list. You need to add the entrylist:keyword:entrylist tag as an entry content of one of the entries of parent list. !!!Recursion is not prevented.


