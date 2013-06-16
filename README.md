BarcampPG 2013 app for Android
=====================
Feature List
- Homepage + countdown
- Map
- Agenda/Schedule
- Sponsor
- Badges
- Update profile + generate QR code
- Friendlist
- QR Scanner

Dependency:
============
=== Action Bar Sherlock ===

This project uses ActionBarSherlock for a backward compatible actionbar, so we need to add in the actionbar sherlock as a library project to make it work.

So we need to do the following:

1. Download the library from here: https://github.com/JakeWharton/ActionBarSherlock, and import the 'actionbarsherlock' folder into eclipse.
2. Import this project into Eclipse, then, right click on the project folder, then:
Properties--> choose Android tab--> Add --> choose 'actionbarsherlock' that you just imported --> Ok

---------------------------
=== Google Play Services SDK ===

This project also uses Map API v2, visit https://developers.google.com/maps/documentation/android/start#getting_the_google_maps_android_api_v2 for more information.
However, we don't need to obtain the API key again, we just need to download Google Play services SDK, and reference to it.

--------------------------
=== SlidingMenu ===

This project also uses SlidingMenu from here: https://github.com/jfeinstein10/SlidingMenu. Visit the github page for setup instruction.

Have fun!
