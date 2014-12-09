Server saves data in MySQL, which should be installed. It is required to create potlatch database
manually, and/or change settings in application.properties file. You also need to register users,
who set in a hard-code, in User table:
    User.create("admin", "pass", "ADMIN", "USER"),
    User.create("user0", "pass", "USER"),
    User.create("user1", "pass", "USER"),
    User.create("user2", "pass", "USER"),
    User.create("user3", "pass", "USER"),
    User.create("user4", "pass", "USER"),
    User.create("user5", "pass", "USER")));