package com.example.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.CvData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object PdfHelper {

  fun printOrSavePdf(context: Context, cvData: CvData) {
    val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
    if (printManager == null) {
      Toast.makeText(context, "Print service unavailable", Toast.LENGTH_SHORT).show()
      return
    }

    val jobName = "CV_${cvData.personal.fullName.replace(" ", "_").ifBlank { "Resume" }}"
    val printAdapter = object : PrintDocumentAdapter() {
      private var pdfDocument: PdfDocument? = null

      override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
      ) {
        if (cancellationSignal?.isCanceled == true) {
          callback?.onLayoutCancelled()
          return
        }

        val info = PrintDocumentInfo.Builder("$jobName.pdf")
          .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
          .setPageCount(1)
          .build()

        callback?.onLayoutFinished(info, true)
      }

      override fun onWrite(
        pages: Array<out PageRange>?,
        destination: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
      ) {
        if (cancellationSignal?.isCanceled == true) {
          callback?.onWriteCancelled()
          return
        }

        pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 at 72dpi
        val page = pdfDocument!!.startPage(pageInfo)

        drawCvOnCanvas(page.canvas, cvData, 595, 842)

        pdfDocument!!.finishPage(page)

        try {
          val outputStream = FileOutputStream(destination?.fileDescriptor)
          pdfDocument!!.writeTo(outputStream)
          outputStream.close()
          callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
        } catch (e: Exception) {
          callback?.onWriteFailed(e.message)
        } finally {
          pdfDocument?.close()
          pdfDocument = null
        }
      }
    }

    printManager.print(jobName, printAdapter, PrintAttributes.Builder().build())
  }

  fun sharePdf(context: Context, cvData: CvData) {
    try {
      val pdfDoc = PdfDocument()
      val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
      val page = pdfDoc.startPage(pageInfo)

      drawCvOnCanvas(page.canvas, cvData, 595, 842)
      pdfDoc.finishPage(page)

      val cacheDir = File(context.cacheDir, "documents")
      if (!cacheDir.exists()) cacheDir.mkdirs()

      val fileName = "CV_${cvData.personal.fullName.replace(" ", "_").ifBlank { "Resume" }}.pdf"
      val file = File(cacheDir, fileName)
      val fos = FileOutputStream(file)
      pdfDoc.writeTo(fos)
      fos.close()
      pdfDoc.close()

      val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
      )

      val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_SUBJECT, "CV - ${cvData.personal.fullName}")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      }
      context.startActivity(Intent.createChooser(shareIntent, if (cvData.lang == "ar") "مشاركة السيرة الذاتية PDF" else "Share CV PDF"))
    } catch (e: Exception) {
      Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
  }

  private fun drawCvOnCanvas(canvas: Canvas, cvData: CvData, width: Int, height: Int) {
    // Fill background
    val isDark = cvData.template == "dark"
    val bgPaint = Paint().apply {
      color = if (isDark) Color.parseColor("#111827") else Color.WHITE
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

    val primaryColor = try {
      Color.parseColor(cvData.primaryColorHex)
    } catch (e: Exception) {
      if (isDark) Color.parseColor("#34D399") else Color.parseColor("#111827")
    }

    val textColor = if (isDark) Color.parseColor("#F9FAFB") else Color.parseColor("#111827")
    val textMuted = if (isDark) Color.parseColor("#9CA3AF") else Color.parseColor("#4B5563")
    val isAr = cvData.lang == "ar"

    val margin = 36f
    var currentY = 50f

    // Header - Name
    val namePaint = Paint().apply {
      color = primaryColor
      textSize = 22f * cvData.fontSizeScale
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }
    val name = cvData.personal.fullName.ifBlank { if (isAr) "الاسم الكامل" else "Full Name" }
    canvas.drawText(name, margin, currentY, namePaint)
    currentY += 20f

    // Title
    val titlePaint = Paint().apply {
      color = textMuted
      textSize = 13f * cvData.fontSizeScale
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }
    val title = cvData.personal.title.ifBlank { if (isAr) "المسمى الوظيفي" else "Professional Title" }
    canvas.drawText(title, margin, currentY, titlePaint)
    currentY += 18f

    // Contact line
    val contactPaint = Paint().apply {
      color = textMuted
      textSize = 10f * cvData.fontSizeScale
      isAntiAlias = true
    }
    val contacts = listOfNotNull(
      cvData.personal.email.takeIf { it.isNotBlank() }?.let { "✉ $it" },
      cvData.personal.phone.takeIf { it.isNotBlank() }?.let { "📱 $it" },
      cvData.personal.address.takeIf { it.isNotBlank() }?.let { "📍 $it" },
      cvData.personal.website.takeIf { it.isNotBlank() }?.let { "🔗 $it" }
    ).joinToString("   •   ")
    if (contacts.isNotBlank()) {
      canvas.drawText(contacts, margin, currentY, contactPaint)
      currentY += 18f
    }

    // Top divider
    val linePaint = Paint().apply {
      color = primaryColor
      strokeWidth = 2f
      isAntiAlias = true
    }
    canvas.drawLine(margin, currentY, width - margin, currentY, linePaint)
    currentY += 22f

    // Summary Section
    if (cvData.personal.summary.isNotBlank()) {
      currentY = drawSectionHeader(canvas, if (isAr) "النبذة الشخصية" else "Professional Summary", margin, currentY, primaryColor, width, cvData.fontSizeScale)
      val bodyPaint = Paint().apply {
        color = textColor
        textSize = 10f * cvData.fontSizeScale
        isAntiAlias = true
      }
      currentY = drawMultilineText(canvas, cvData.personal.summary, margin, currentY, width - (margin * 2), bodyPaint, 14f)
      currentY += 15f
    }

    // Experiences Section
    if (cvData.experiences.isNotEmpty()) {
      currentY = drawSectionHeader(canvas, if (isAr) "الخبرات المهنية" else "Work Experience", margin, currentY, primaryColor, width, cvData.fontSizeScale)
      val itemTitlePaint = Paint().apply {
        color = primaryColor
        textSize = 11f * cvData.fontSizeScale
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
      }
      val subPaint = Paint().apply {
        color = textMuted
        textSize = 10f * cvData.fontSizeScale
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        isAntiAlias = true
      }
      val descPaint = Paint().apply {
        color = textColor
        textSize = 9.5f * cvData.fontSizeScale
        isAntiAlias = true
      }

      for (exp in cvData.experiences) {
        if (currentY > height - 60f) break
        canvas.drawText("${exp.position} (${exp.dates})", margin, currentY, itemTitlePaint)
        currentY += 14f
        canvas.drawText(exp.company, margin, currentY, subPaint)
        currentY += 14f
        if (exp.desc.isNotBlank()) {
          currentY = drawMultilineText(canvas, exp.desc, margin, currentY, width - (margin * 2), descPaint, 13f)
        }
        currentY += 10f
      }
      currentY += 5f
    }

    // Education Section
    if (cvData.education.isNotEmpty()) {
      currentY = drawSectionHeader(canvas, if (isAr) "المؤهلات التعليمية" else "Education", margin, currentY, primaryColor, width, cvData.fontSizeScale)
      val itemTitlePaint = Paint().apply {
        color = primaryColor
        textSize = 11f * cvData.fontSizeScale
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
      }
      val subPaint = Paint().apply {
        color = textMuted
        textSize = 10f * cvData.fontSizeScale
        isAntiAlias = true
      }

      for (edu in cvData.education) {
        if (currentY > height - 50f) break
        canvas.drawText("${edu.degree} (${edu.dates})", margin, currentY, itemTitlePaint)
        currentY += 14f
        canvas.drawText(edu.school, margin, currentY, subPaint)
        currentY += 16f
      }
      currentY += 5f
    }

    // Skills Section
    if (cvData.skills.isNotEmpty()) {
      currentY = drawSectionHeader(canvas, if (isAr) "المهارات والخبرات" else "Skills & Competencies", margin, currentY, primaryColor, width, cvData.fontSizeScale)
      val skillPaint = Paint().apply {
        color = textColor
        textSize = 10f * cvData.fontSizeScale
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        isAntiAlias = true
      }
      val skillsText = cvData.skills.joinToString("   •   ") { it.name }
      currentY = drawMultilineText(canvas, skillsText, margin, currentY, width - (margin * 2), skillPaint, 14f)
      currentY += 15f
    }

    // Projects Section
    if (cvData.projects.isNotEmpty()) {
      currentY = drawSectionHeader(canvas, if (isAr) "المشاريع والشهادات" else "Projects & Certifications", margin, currentY, primaryColor, width, cvData.fontSizeScale)
      val projPaint = Paint().apply {
        color = textColor
        textSize = 10f * cvData.fontSizeScale
        isAntiAlias = true
      }
      for (proj in cvData.projects) {
        if (currentY > height - 40f) break
        canvas.drawText("• ${proj.name}", margin, currentY, projPaint)
        currentY += 14f
        if (proj.desc.isNotBlank()) {
          currentY = drawMultilineText(canvas, "  ${proj.desc}", margin + 10f, currentY, width - (margin * 2) - 10f, projPaint, 13f)
        }
        currentY += 6f
      }
    }
  }

  private fun drawSectionHeader(
    canvas: Canvas,
    title: String,
    x: Float,
    y: Float,
    color: Int,
    width: Int,
    scale: Float
  ): Float {
    val paint = Paint().apply {
      this.color = color
      textSize = 12f * scale
      typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
      isAntiAlias = true
    }
    canvas.drawText(title.uppercase(), x, y, paint)
    val linePaint = Paint().apply {
      this.color = color
      strokeWidth = 1.2f
      isAntiAlias = true
    }
    canvas.drawLine(x, y + 5f, width - x, y + 5f, linePaint)
    return y + 20f
  }

  private fun drawMultilineText(
    canvas: Canvas,
    text: String,
    x: Float,
    startY: Float,
    maxWidth: Float,
    paint: Paint,
    lineHeight: Float
  ): Float {
    var currentY = startY
    val lines = text.split("\n")
    for (rawLine in lines) {
      val words = rawLine.split(" ")
      var currentLine = ""
      for (word in words) {
        val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
        val measured = paint.measureText(testLine)
        if (measured > maxWidth && currentLine.isNotEmpty()) {
          canvas.drawText(currentLine, x, currentY, paint)
          currentY += lineHeight
          currentLine = word
        } else {
          currentLine = testLine
        }
      }
      if (currentLine.isNotEmpty()) {
        canvas.drawText(currentLine, x, currentY, paint)
        currentY += lineHeight
      }
    }
    return currentY
  }
}
