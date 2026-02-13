package com.project.cineversemobile.Util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.project.cineversemobile.Data.CartItem
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.scale

fun generateTicketsPdf(
    context: Context,
    items: List<CartItem>,
    userFullName: String
): Uri {

    val pdfDocument = PdfDocument()

    val titlePaint = Paint().apply {
        textSize = 20f
        isFakeBoldText = true
    }

    val sectionPaint = Paint().apply {
        textSize = 14f
        isFakeBoldText = true
    }

    val normalPaint = Paint().apply {
        textSize = 12f
    }

    val smallPaint = Paint().apply {
        textSize = 10f
        color = Color.DKGRAY
    }

    items.forEachIndexed { index, item ->

        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, index + 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas

        // Fondo tarjeta
        val bgPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        val left = 30f
        val top = 40f
        val right = 565f
        val bottom = 800f

        canvas.drawRect(left, top, right, bottom, bgPaint)

        var y = 80f

        // Header
        canvas.drawText("CineVerse", 60f, y, titlePaint)
        y += 30
        canvas.drawText("Entrada válida para una sesión", 60f, y, normalPaint)
        y += 30

        canvas.drawLine(50f, y, 545f, y, normalPaint)
        y += 30

        // Datos principales
        canvas.drawText("PELÍCULA", 60f, y, sectionPaint)
        y += 18
        canvas.drawText(item.movieTitle, 60f, y, normalPaint)
        y += 30

        canvas.drawText("FECHA Y HORA", 60f, y, sectionPaint)
        y += 18
        canvas.drawText(item.dateTime.replace("T", " "), 60f, y, normalPaint)
        y += 30

        canvas.drawText("SALA", 60f, y, sectionPaint)
        y += 18
        canvas.drawText(item.roomName ?: "-", 60f, y, normalPaint)
        y += 30

        canvas.drawText("ASIENTO", 60f, y, sectionPaint)
        y += 18
        canvas.drawText(item.seatNumber.toString(), 60f, y, normalPaint)
        y += 30

        canvas.drawText("PRECIO", 60f, y, sectionPaint)
        y += 18
        canvas.drawText("%.2f €".format(item.price), 60f, y, normalPaint)
        y += 40

        canvas.drawLine(50f, y, 545f, y, normalPaint)
        y += 30

        // Bloque cliente
        canvas.drawText("CLIENTE", 60f, y, sectionPaint)
        y += 18
        canvas.drawText(userFullName, 60f, y, normalPaint)
        y += 40

        val qrContent = "CineVerse|session=${item.sessionId}|seat=${item.seatNumber}|user=$userFullName"
        val qrBitmap = generateQrBitmap(qrContent, 250)

        val qrLeft = 380f
        val qrTop = 170f
        val qrSize = 120f

        canvas.drawBitmap(
            qrBitmap.scale(qrSize.toInt(), qrSize.toInt(), false),
            qrLeft,
            qrTop,
            null
        )

        // Código de referencia
        val ref = "REF-${System.currentTimeMillis()}-${index + 1}"
        canvas.drawText("Referencia:", 380f, qrTop + qrSize + 30f, smallPaint)
        canvas.drawText(ref, 380f, qrTop + qrSize + 45f, normalPaint)

        // Pie
        canvas.drawLine(50f, 740f, 545f, 740f, normalPaint)
        canvas.drawText(
            "Entrada válida solo para la sesión indicada · No reembolsable",
            60f,
            770f,
            smallPaint
        )

        pdfDocument.finishPage(page)
    }

    val file = File(context.cacheDir, "tickets_${System.currentTimeMillis()}.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}

