# micropress

clj! cms!! microservice architecture!!!

micropress is a Content Management System with Microservice architecture.

## Setup

### Install MySQL and migrate

1. [Download MySQL from here](https://dev.mysql.com/downloads/installer/).
1. Create database `micropress`.
1. start repl(`lein repl`), and migrate(`(migrate config)`).

## Development

To get an interactive development environment run:

    lein repl

and run

    (start)

To clean all compiled files:

    lein clean

## License

Copyright © 2016 blackawa

Distributed under the Eclipse Public License either version 1.0 or  any later version.
