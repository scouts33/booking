package com.sungmin.booking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BookingClientApiApplication

fun main(args: Array<String>) {
    runApplication<BookingClientApiApplication>(*args)
}
