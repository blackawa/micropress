# micropress

Clojure x CMS!

micropress is the API based Content Management System written by clojure.

## Setup

### Install MySQL

[Download MySQL from here](https://dev.mysql.com/downloads/installer/), and install.

Log in with root user, and create schema.

```
create user 'micropress'@'localhost' identified by 'p@ssw0rd';
create database micropress;
grant all privileges on micropress.* to 'micropress'@'localhost';
create database micropress_test;
grant all privileges on micropress_test.* to 'micropress'@'localhost';
flush privileges;
```

Migrate.

```
lein repl
user=> (migrate)
user=> (quit)
lein with-profile +test repl
user=> (migrate)
user=> (quit)
```

## Development

To get an interactive development environment run:

    lein repl

To Start server:

    user=> (run)

To clean all compiled files:

    lein clean

To run test:

    lein test

or

    lein with-profile +test
    user=> (run-all-tests)

## License

Copyright © 2016 blackawa

Distributed under the Eclipse Public License either version 1.0 or  any later version.
