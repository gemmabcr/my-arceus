package dev.gemmabcr.views.ui

import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h3
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.ul

class PrivacyContent : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        div {
            classes = setOf("ui-card", "privacy-content")
            privacyHero()
            privacyNavigation()
            dataProcessingSections()
            serviceSections()
        }
    }

    private fun DIV.privacyHero() {
        div {
            classes = setOf("privacy-hero")
            div {
                classes = setOf("privacy-hero-mark")
                attributes["aria-hidden"] = "true"
                +"✓"
            }
            div {
                classes = setOf("privacy-hero-copy")
                p {
                    classes = setOf("privacy-updated")
                    +translate(CommonI18nKey.PRIVACY_LAST_UPDATED)
                }
                p {
                    classes = setOf("privacy-hero-controller")
                    +"Gemma Becerra"
                }
                a(href = "mailto:$PRIVACY_EMAIL") {
                    classes = setOf("privacy-contact-link")
                    +PRIVACY_EMAIL
                }
            }
        }
    }

    private fun DIV.dataProcessingSections() {
            privacySection(
                "privacy-controller",
                "01",
                CommonI18nKey.PRIVACY_CONTROLLER_TITLE,
                CommonI18nKey.PRIVACY_CONTROLLER_TEXT,
            )
            privacyListSection(
                "privacy-data",
                "02",
                CommonI18nKey.PRIVACY_DATA_TITLE,
                CommonI18nKey.PRIVACY_DATA_ACCOUNT,
                CommonI18nKey.PRIVACY_DATA_PROGRESS,
                CommonI18nKey.PRIVACY_DATA_OCR,
                CommonI18nKey.PRIVACY_DATA_ANALYTICS,
            )
            privacyListSection(
                "privacy-purpose",
                "03",
                CommonI18nKey.PRIVACY_PURPOSE_TITLE,
                CommonI18nKey.PRIVACY_PURPOSE_ACCOUNT,
                CommonI18nKey.PRIVACY_PURPOSE_ANALYTICS,
            )
            privacyListSection(
                "privacy-legal-basis",
                "04",
                CommonI18nKey.PRIVACY_LEGAL_BASIS_TITLE,
                CommonI18nKey.PRIVACY_LEGAL_BASIS_CONTRACT,
                CommonI18nKey.PRIVACY_LEGAL_BASIS_INTEREST,
            )
    }

    private fun DIV.serviceSections() {
            privacySection(
                "privacy-retention",
                "05",
                CommonI18nKey.PRIVACY_RETENTION_TITLE,
                CommonI18nKey.PRIVACY_RETENTION_TEXT,
            )
            privacySection(
                "privacy-recipients",
                "06",
                CommonI18nKey.PRIVACY_RECIPIENTS_TITLE,
                CommonI18nKey.PRIVACY_RECIPIENTS_TEXT,
                CommonI18nKey.PRIVACY_TRANSFERS_TEXT,
                CommonI18nKey.PRIVACY_NO_AUTOMATED_DECISIONS,
            )
            privacySection(
                "privacy-cookies",
                "07",
                CommonI18nKey.PRIVACY_COOKIES_TITLE,
                CommonI18nKey.PRIVACY_COOKIES_TEXT,
            )
            rightsSection()
            privacySection(
                "privacy-security",
                "09",
                CommonI18nKey.PRIVACY_SECURITY_TITLE,
                CommonI18nKey.PRIVACY_SECURITY_TEXT,
            )
            privacySection(
                "privacy-changes",
                "10",
                CommonI18nKey.PRIVACY_CHANGES_TITLE,
                CommonI18nKey.PRIVACY_CHANGES_TEXT,
            )
    }

    private fun DIV.privacyNavigation() {
        val links =
            listOf(
                "privacy-data" to CommonI18nKey.PRIVACY_DATA_TITLE,
                "privacy-purpose" to CommonI18nKey.PRIVACY_PURPOSE_TITLE,
                "privacy-legal-basis" to CommonI18nKey.PRIVACY_LEGAL_BASIS_TITLE,
                "privacy-retention" to CommonI18nKey.PRIVACY_RETENTION_TITLE,
                "privacy-recipients" to CommonI18nKey.PRIVACY_RECIPIENTS_TITLE,
                "privacy-cookies" to CommonI18nKey.PRIVACY_COOKIES_TITLE,
                "privacy-rights" to CommonI18nKey.PRIVACY_RIGHTS_TITLE,
                "privacy-security" to CommonI18nKey.PRIVACY_SECURITY_TITLE,
            )
        nav {
            classes = setOf("privacy-navigation")
            attributes["aria-label"] = translate(CommonI18nKey.PRIVACY_POLICY)
            links.forEach { (id, key) -> a(href = "#$id") { +translate(key) } }
        }
    }

    private fun DIV.rightsSection() {
        div {
            classes = setOf("privacy-section", "privacy-section-featured")
            id = "privacy-rights"
            privacySectionHeading("08", CommonI18nKey.PRIVACY_RIGHTS_TITLE)
            div {
                classes = setOf("privacy-section-body")
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
    }

    private fun DIV.privacySection(idValue: String, number: String, title: I18nKey, vararg paragraphs: I18nKey) {
        div {
            classes = setOf("privacy-section")
            id = idValue
            privacySectionHeading(number, title)
            div {
                classes = setOf("privacy-section-body")
                paragraphs.forEach { key -> p { +translate(key) } }
            }
        }
    }

    private fun DIV.privacyListSection(idValue: String, number: String, title: I18nKey, vararg items: I18nKey) {
        div {
            classes = setOf("privacy-section")
            id = idValue
            privacySectionHeading(number, title)
            div {
                classes = setOf("privacy-section-body")
                ul { items.forEach { key -> li { +translate(key) } } }
            }
        }
    }

    private fun DIV.privacySectionHeading(number: String, title: I18nKey) {
        div {
            classes = setOf("privacy-section-heading")
            span {
                classes = setOf("privacy-section-number")
                attributes["aria-hidden"] = "true"
                +number
            }
            h3 { +translate(title) }
        }
    }
}

private const val AEPD_URL = "https://www.aepd.es/"
const val PRIVACY_EMAIL = "ei@my-arceus.com"
