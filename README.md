# OneStore

Because we need One Store to rules them all.

The aim of OneStore is to create a simple end user data storage that let any developer create some app using this unique storage to create apps but also let users keep there data at home. OneStore should provide at least authentication, authorization, data storage (key-value). This will be enought for most end user app.

OneStore is a response to very big centralized services. But it's not the only one solution to doing it.

# Features

## Today

- Create collections and declare there access rights
- Add/Remove items from collections
- Read one item from a collection
- Read all items from a collection
- Create multiple users
- Authentication

## In future

- environment variables to changes salts
- install guide
- Update one data
- Delete user from API (with all user data)
- Refresh token to keep user connected
- Add a simple web UI to manage users
- Add a simple web UI to explore collections and access rights
- GraalVM based Docker image


# FAQ
##  What is the default admin password?

The default admin user is "admin" and its password is "admin". But API user must send password hashed with sha256. So in database we will have double hashed passwords to ensure passwords are not easily computable.

The password is saved in database hash in sha3-256 with a salt. The salt can be configured if you need, but you will need to generate a new password hash for the default user manually.