# micropress

Content management system with Clojure / Script.

## Use micropress

### Environment variables

micropress expects some environment variables to be set.

|Variable|Role|
|:--|:--|
|`DATABASE_URL`|JDBC connection string. e.g. `jdbc:postgresql://db:5432/micropress`|
|`USERNAME`|Database connection username|
|`PASSWORD`|Database connection password|
|`ACCESS_CONTROL_ALLOW_ORIGIN`|[CORS(Cross Origin Resource Sharing)](https://www.w3.org/TR/cors/) configuration. You can send GET request from browser of your frontend service|
|`AWS_ACCESS_KEY_ID`|You must set if you want to upload images to micropress. See [Managing Access Keys for your AWS Account - Amazon Web Services](http://docs.aws.amazon.com/general/latest/gr/managing-aws-access-keys.html)|
|`AWS_SECRET_ACCESS_KEY`|You must set if you want to upload images to micropress. See [Managing Access Keys for your AWS Account - Amazon Web Services](http://docs.aws.amazon.com/general/latest/gr/managing-aws-access-keys.html)|

## Developing

### Prerequisite

You need

* [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)
* [Leiningen](http://leiningen.org/)
* [Docker](https://www.docker.com/)
* [Docker Compose](https://docs.docker.com/compose/)
* [AWS Confidential file which can upload file to Amazon s3](https://aws.amazon.com/jp/s3/)

on your machine.

### Setup

When you first clone this repository, run:

```sh
lein setup
```

This will create files for local configuration, and prep your system
for the project.

### Environment

To begin developing, start with a REPL.

```sh
lein repl
```

Then load the development environment.

```clojure
user=> (dev)
:loaded
```

Before you start application, run docker and boot the database.

```sh
docker-compose up -d --build
```

Run `go` to initiate and start the system.

```clojure
dev=> (go)
:started
```

This creates a web server at <http://localhost:3001>.

When you make changes to your source files, use `reset` to reload any
modified files and reset the server. Changes to CSS or ClojureScript
files will be hot-loaded into the browser.

```clojure
dev=> (reset)
:reloading (...)
:resumed
```

If you want to access a ClojureScript REPL, make sure that the site is loaded
in a browser and run:

```clojure
dev=> (cljs-repl)
Waiting for browser connection... Connected.
To quit, type: :cljs/quit
nil
cljs.user=>
```

### Testing

Testing is fastest through the REPL, as you avoid environment startup
time.

```clojure
dev=> (test)
...
```

But you can also run tests through Leiningen.

```sh
lein test
```

### Build

Run the command to build the docker image:

```sh
lein do clean, uberjar && docker build -t micropress:0.1 .
```

### Migrations

Migrations are handled by [ragtime][]. Migration files are stored in
the `resources/migrations` directory, and are applied in alphanumeric
order.

To update the database to the latest migration, open the REPL and run:

```clojure
dev=> (migrate)
Applying 20150815144312-create-users
Applying 20150815145033-create-posts
```

To rollback the last migration, run:

```clojure
dev=> (rollback)
Rolling back 20150815145033-create-posts
```

Note that the system needs to be setup with `(init)` or `(go)` before
migrations can be applied.

[ragtime]: https://github.com/weavejester/ragtime

### Generators

This project has several generator functions to help you create files.

To create a new endpoint:

```clojure
dev=> (gen/endpoint "bar")
Creating file src/foo/endpoint/bar.clj
Creating file test/foo/endpoint/bar_test.clj
Creating directory resources/foo/endpoint/bar
nil
```

To create a new component:

```clojure
dev=> (gen/component "baz")
Creating file src/foo/component/baz.clj
Creating file test/foo/component/baz_test.clj
nil
```

To create a new boundary:

```clojure
dev=> (gen/boundary "quz" foo.component.baz.Baz)
Creating file src/foo/boundary/quz.clj
Creating file test/foo/boundary/quz_test.clj
nil
```

To create a new SQL migration:

```clojure
dev=> (gen/sql-migration "create-users")
Creating file resources/foo/migrations/20160519143643-create-users.up.sql
Creating file resources/foo/migrations/20160519143643-create-users.down.sql
nil
```

## Legal

Copyright © 2017 blackawa
