package dev.gemmabcr.views.ui

import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.ul

class PrivacyContent : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        div {
            classes = setOf("ui-card", "privacy-content")
            p {
                classes = setOf("privacy-updated")
                +translate(CommonI18nKey.PRIVACY_LAST_UPDATED)
            }
            privacySection(
                CommonI18nKey.PRIVACY_CONTROLLER_TITLE,
                CommonI18nKey.PRIVACY_CONTROLLER_TEXT,
            )
            privacyListSection(
                CommonI18nKey.PRIVACY_DATA_TITLE,
                CommonI18nKey.PRIVACY_DATA_ACCOUNT,
                CommonI18nKey.PRIVACY_DATA_PROGRESS,
                CommonI18nKey.PRIVACY_DATA_OCR,
                CommonI18nKey.PRIVACY_DATA_ANALYTICS,
            )
            privacyListSection(
                CommonI18nKey.PRIVACY_PURPOSE_TITLE,
                CommonI18nKey.PRIVACY_PURPOSE_ACCOUNT,
                CommonI18nKey.PRIVACY_PURPOSE_ANALYTICS,
            )
            privacyListSection(
                CommonI18nKey.PRIVACY_LEGAL_BASIS_TITLE,
                CommonI18nKey.PRIVACY_LEGAL_BASIS_CONTRACT,
                CommonI18nKey.PRIVACY_LEGAL_BASIS_INTEREST,
            )
            privacySection(CommonI18nKey.PRIVACY_RETENTION_TITLE, CommonI18nKey.PRIVACY_RETENTION_TEXT)
            privacySection(
                CommonI18nKey.PRIVACY_RECIPIENTS_TITLE,
                CommonI18nKey.PRIVACY_RECIPIENTS_TEXT,
                CommonI18nKey.PRIVACY_TRANSFERS_TEXT,
                CommonI18nKey.PRIVACY_NO_AUTOMATED_DECISIONS,
            )
            privacySection(CommonI18nKey.PRIVACY_COOKIES_TITLE, CommonI18nKey.PRIVACY_COOKIES_TEXT)
            rightsSection()
            privacySection(CommonI18nKey.PRIVACY_SECURITY_TITLE, CommonI18nKey.PRIVACY_SECURITY_TEXT)
            privacySection(CommonI18nKey.PRIVACY_CHANGES_TITLE, CommonI18nKey.PRIVACY_CHANGES_TEXT)
        }
    }

    private fun DIV.rightsSection() {
        div {
            classes = setOf("privacy-section")
            h3 { +translate(CommonI18nKey.PRIVACY_RIGHTS_TITLE) }
            p {
                +translate(CommonI18nKey.PRIVACY_RIGHTS_TEXT)
                +" "
                a(href = "mailto:$PRIVACY_EMAIL") { +PRIVACY_EMAIL }
            }
            p {
                +translate(CommonI18nKey.PRIVACY_COMPLAINT_TEXT)
                +" "
                a(href = AEPD_URL) {
                    attributes["target"] = "_blank"
                    attributes["rel"] = "noopener noreferrer"
                    +"aepd.es"
                }
            }
        }
    }

    private fun DIV.privacySection(title: I18nKey, vararg paragraphs: I18nKey) {
        div {
            classes = setOf("privacy-section")
            h3 { +translate(title) }
            paragraphs.forEach { key -> p { +translate(key) } }
        }
    }

    private fun DIV.privacyListSection(title: I18nKey, vararg items: I18nKey) {
        div {
            classes = setOf("privacy-section")
            h3 { +translate(title) }
            ul { items.forEach { key -> li { +translate(key) } } }
        }
    }
}

private const val AEPD_URL = "https://www.aepd.es/"
const val PRIVACY_EMAIL = "ei@my-arceus.com"
