package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.ocr.OcrTodoImportPreview
import dev.gemmabcr.ocr.PokedexScreenshotOcrResult
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.ui.HtmlLayout
import dev.gemmabcr.views.ui.MenuItem
import dev.gemmabcr.views.ui.OcrContent
import kotlinx.html.DIV

class OcrPage(
    private val result: PokedexScreenshotOcrResult? = null,
    private val importPreview: OcrTodoImportPreview? = null,
    private val error: String? = null,
    private val session: Session = Session(),
) : HtmlLayout(CommonI18nKey.UPLOAD_PROGRESS, session, activeMenuItem = MenuItem.OCR) {
    override fun DIV.content() {
        OcrContent(result, importPreview, error, session.user != null).create(this)
    }
}
