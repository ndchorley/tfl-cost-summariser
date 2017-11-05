package com.xyphias.tflcostsummariser

import com.natpryce.*
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    summarise(args, ::println)
}

data class Record(val date: LocalDate, val cost: Double)

fun summarise(fileNames: Array<String>, write: (String) -> Unit) {
    if (fileNames.isEmpty()) {
        write("No files given")
        return
    }

    val (monthRecords, failures) = fileNames.map(::summariseFile).partition()

    val totalMonthRecord = monthRecords.reduce { acc: Record, record: Record ->
        Record(acc.date, acc.cost + record.cost)
    }

    display(totalMonthRecord, failures, write)
}

private fun display(totalMonthRecord: Record, failures: List<String>, write: (String) -> Unit) {
    val formattedDate = totalMonthRecord.date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val formattedTotal = "%.2f".format(totalMonthRecord.cost)

    write("$formattedDate: $formattedTotal")

    if (failures.isNotEmpty()) {
        write("")
    }

    failures.forEach { write("No such file: $it") }
}

private fun summariseFile(fileName: String): Result<Record, String> {
    return readLines(fileName).map {
        it.drop(1).map(::toRecord).reduce { acc: Record, record: Record ->
            Record(record.date, acc.cost + record.cost)
        }
    }
}

private fun readLines(fileName: String): Result<List<String>, String> {
    return try {
        Ok(File(fileName).readLines())
    } catch (e: FileNotFoundException) {
        Err(fileName)
    }
}

fun toRecord(line: String): Record {
    val fields = line.split(",")

    val date = LocalDate.parse(fields[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val cost = fields[1].toDouble() * -1

    return Record(date, cost)
}
