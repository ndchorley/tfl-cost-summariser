package com.xyphias.tflcostsummariser

import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    summarise(args[0], ::println)
}

data class Record(val date: LocalDate, val cost: Double)

fun summarise(fileName: String, write: (String) -> Unit) {
    val monthRecord = File(fileName)
            .readLines()
            .drop(1)
            .map(::toRecord)
            .fold(Record(LocalDate.MIN, 0.0)) { acc: Record, record: Record ->
                Record(record.date, acc.cost + record.cost)
            }

    val formattedDate = monthRecord.date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val formattedTotal = "%.2f".format(monthRecord.cost)

    write("$formattedDate: $formattedTotal")
}

fun toRecord(line: String): Record {
    val fields = line.split(",")

    val date = LocalDate.parse(fields[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val cost = fields[1].toDouble() * -1

    return Record(date, cost)
}
