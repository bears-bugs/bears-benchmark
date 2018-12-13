# Changelog

## 1.3.6

### Minor Changes

(nothing yet)

## 1.3.5

### Minor Changes

* Restored the KMS client cache with a fix for the memory leak.
* When using a master key provider that can only service a subset of regions
(e.g. using the deprecated constructors), and requesting a master key from a
region not servicable by that MKP, the exception will now be thrown on first
use of the MK, rather than at getMasterKey time.

## 1.3.4

### Minor Changes

* Removed the KMS client cache, which could result in a memory leak when
decrypting certain malformed ciphertexts. This may reduce performance slightly
in some scenarios.

## 1.3.3

### Minor Changes
* Move the `aws-encryption-sdk-java` repository from `awslabs` to `aws`.
* Log a warning when an unsupported asymmetric algorithm is used with `JceMasterKey`
* Make `JceMasterKey` case insensitive
* Fix bare aliases not using default region

## 1.3.2

### Minor Changes
* Frame size restriction removed again
* Support Builders for use with AWS KMS
* Fix estimateCipherText when used with cached data keys
* Do not automatically set a default region in KmsMasterKeyProvider

## 1.3.1

### Minor changes

* Frame sizes are once again required to be aligned to 16 bytes
  This restriction was relaxed in 1.3.0, but due to compatibility concerns
  we'll put this restriction back in for the time being.

## 1.3.0

### Major changes

* Synchronized version numbers with the Python release
* Added cryptographic materials managers
* Added data key caching
* Moved to deterministic IV generation

### Minor changes

* Added changelog
* Made elliptic curve signatures length deterministic
* Various minor improvements
