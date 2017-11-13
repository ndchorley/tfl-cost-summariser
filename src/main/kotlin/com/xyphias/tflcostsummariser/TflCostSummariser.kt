package com.xyphias.tflcostsummariser

import com.natpryce.*
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
    summarise(args, ::println)
}

data class Record(val date: YearMonth, val cost: Double)

fun summarise(fileNames: Array<String>, write: (String) -> Unit) {
    if (fileNames.isEmpty()) {
        write("No files given")
        return
    }

    val (monthRecords, failures) = fileNames.map(::summariseFile).partition()

    val monthTotals = monthRecords
            .groupBy { it.date }
            .map { it.value.reduce { acc: Record, record: Record ->
                        Record(acc.date, acc.cost + record.cost)
                    }
            }
            .sortedBy { it.date }

    monthTotals.forEach { display(it, write) }

    if (failures.isNotEmpty()) {
        write("")
    }

    failures.forEach { write("No such file: $it") }

}

private fun display(totalMonthRecord: Record, write: (String) -> Unit) {
    val formattedDate = totalMonthRecord.date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
    val formattedTotal = "%.2f".format(totalMonthRecord.cost)

    write("$formattedDate: $formattedTotal")
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

    val date = YearMonth.parse(fields[0], DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val cost = fields[1].toDouble() * -1

    return Record(date, cost)
}
