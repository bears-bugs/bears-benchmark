# modelmapper-module-vavr
[![License](http://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Build Status](https://travis-ci.org/julianps/modelmapper-module-vavr.svg)](https://travis-ci.org/julianps/modelmapper-module-vavr)

This is a module for [modelmapper](https://github.com/modelmapper/modelmapper) to support [vavr.io](https://github.com/vavr-io/vavr) features.
Modelmapper is great for converting objects but cannot handle vavr.io types ootb.
This project gives you an easy to use module to be able to convert your vavr types.

## Register the module: 

```modelMapper.registerModule(new VavrModule());```

## Supported mappings:

The base-type `Value` and all its implementations.
