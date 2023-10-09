# payment-tcp

# Introduction

This microservice is responsible for create a connection using TCP protocol that will receive encoded transmission with transactions messages, process TLV messages and return the payment transactions information 

## Technologies

Java 17

Spring Framework

Maven

Jacoco

Pitest

## External Library

BER-TLV - BerTlv is a java library for parsing and building BER TLV encoded data.
Documentation: https://github.com/evsinev/ber-tlv/blob/master/README.md

Java Money - Java library for handling with Money & Currency models
Documentation: https://javamoney.github.io/


## How to run

Utilise the SpringBoot run to run the project, to execute this task first we need access the main folder (../payment-tcp) and run the command below

```mvn spring-boot:run```

or you can Run the Application.class via IDE

## Unit Tests

To run and collect the tests reports run the command below

``mvn clean test``

To check the unit tests coverage access the Jacoco report at path: target -> jacoco-report -> index.html

To check the mutation tests report access the Pitest report at path: target -> pit-reports -> index.html
