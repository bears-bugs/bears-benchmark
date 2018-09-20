# Changelog - see https://keepachangelog.com for conventions

## [Unreleased]

### Added

### Changed

### Deprecated

### Removed

### Fixed
- issue #13 - in previous version, we had forgotten to send OAuth token when creating PR

## [1.0.4] - 2018-08-17

### Added
- upgraded to ci-droid-extensions 1.0.4 : new actions available

### Changed
- **BREAKING CHANGE IN CONFIG** : renamed property key from gitHub.url to gitHub.api.url 
- issue #8 - now also working with github.com - need to receive an OAuth token instead of password
- not logging full stacktrace anymore when branch already exists


## [1.0.3] - 2018-08-03

### Changed
- issue #4 - if PR is made from a fork don't try to rebase
- upgraded to internal-api and extensions 1.0.2 

### Fixed
- issue #2 - providing a PullRequestEventHandler shouldn't be mandatory
- issue #5 - when credentials are incorrect, send a KO email

## [1.0.2] - 2018-07-12

### Changed

- upgraded to internal-api and extensions 1.0.1
- releasing with Travis


## [1.0.1] - 2018-06-29 

### Changed

- refactoring to follow recommended conventions

## [1.0.0] - 2018-06-21 

first version !


