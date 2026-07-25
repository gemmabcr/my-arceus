package dev.gemmabcr.views.ui

import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.UL
import kotlinx.html.a
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.input
import kotlinx.html.label
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.ul

class AuthContent(
    private val mode: AuthMode,
    private val error: I18nKey?,
    private val googleEnabled: Boolean,
    private val appleEnabled: Boolean,
) : UiComponent {
    override fun create(content: FlowContent): FlowContent = content.apply {
        div(classes = "auth-layout ui-card") {
            style = AUTH_LAYOUT_STYLE
            authCard()
            benefitsCard()
        }
    }

    private fun DIV.authCard() {
        div {
            style = AUTH_CARD_STYLE
            span {
                style = EYEBROW_STYLE
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.WELCOME_BACK else CommonI18nKey.REGISTER)
            }
            h2 {
                style = "margin: 0.55rem 0 0.4rem; color: ${Colors.DARKEST_BLUE}; font-size: 1.75rem;"
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN else CommonI18nKey.CREATE_ACCOUNT)
            }
            p {
                style =
                    "margin: 0 0 1.35rem; color: ${Colors.DARK_BLUE}; line-height: 1.5; font-size: 0.92rem;"
                +translate(
                    if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN_INTRO else CommonI18nKey.CREATE_ACCOUNT_INTRO,
                )
            }
            error?.let {
                p {
                    attributes["role"] = "alert"
                    style = ERROR_STYLE
                    +translate(it)
                }
            }
            socialLoginButton("/auth/google", "G", CommonI18nKey.GOOGLE_LOGIN, googleEnabled, false)
            socialLoginButton("/auth/apple", "", CommonI18nKey.APPLE_LOGIN, appleEnabled, true)
            div {
                style = DIVIDER_STYLE
                span {
                    style =
                        "padding: 0 0.7rem; background: ${Colors.CREAM_LIGHEST}; " +
                            "color: ${Colors.DARK_BLUE}; font-size: 0.75rem;"
                    +translate(CommonI18nKey.OR_CONTINUE_EMAIL)
                }
            }
            credentialsForm()
            accountSwitch()
        }
    }

    private fun DIV.credentialsForm() {
        form(action = if (mode == AuthMode.LOGIN) "/login" else "/register", method = FormMethod.post) {
            style = "display: flex; flex-direction: column; gap: 0.9rem;"
            authInput(CommonI18nKey.EMAIL, "email", InputType.email, "name@example.com")
            authInput(CommonI18nKey.PASSWORD, "password", InputType.password, "••••••••")
            if (mode == AuthMode.REGISTER) {
                authInput(CommonI18nKey.CONFIRM_PASSWORD, "confirmPassword", InputType.password, "••••••••")
                p {
                    style = "margin: -0.35rem 0 0; color: ${Colors.DARK_BLUE}; font-size: 0.75rem;"
                    +translate(CommonI18nKey.PASSWORD_REQUIREMENTS)
                }
            }
            button(type = ButtonType.submit) {
                classes = setOf("ui-primary-button")
                style = "width: 100%; margin-top: 0.15rem;"
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN else CommonI18nKey.CREATE_ACCOUNT_SUBMIT)
            }
        }
    }

    private fun FlowContent.authInput(key: I18nKey, name: String, type: InputType, placeholder: String) {
        div {
            label {
                htmlFor = name
                classes = setOf("ui-label")
                +translate(key)
            }
            input(type = type, name = name) {
                attributes["id"] = name
                required = true
                this.placeholder = placeholder
                attributes["maxlength"] = if (name == "email") "50" else "128"
                attributes["autocomplete"] = when (name) {
                    "email" -> "email"
                    "password" -> if (mode == AuthMode.LOGIN) "current-password" else "new-password"
                    else -> "new-password"
                }
                classes = setOf("ui-field")
            }
        }
    }

    private fun DIV.socialLoginButton(
        href: String,
        icon: String,
        label: I18nKey,
        enabled: Boolean,
        dark: Boolean,
    ) {
        if (enabled) {
            a(href = href) {
                style = socialButtonStyle(dark)
                span { style = SOCIAL_ICON_STYLE; +icon }
                +translate(label)
            }
        } else {
            span {
                attributes["aria-disabled"] = "true"
                attributes["title"] = translate(CommonI18nKey.OAUTH_NOT_CONFIGURED)
                style = socialButtonStyle(dark) + " opacity: 0.45; cursor: not-allowed;"
                span { style = SOCIAL_ICON_STYLE; +icon }
                +translate(label)
            }
        }
    }

    private fun DIV.accountSwitch() {
        p {
            style = "margin: 1.15rem 0 0; text-align: center; color: ${Colors.DARK_BLUE}; font-size: 0.82rem;"
            +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.NO_ACCOUNT else CommonI18nKey.ALREADY_HAVE_ACCOUNT)
            +" "
            a(href = if (mode == AuthMode.LOGIN) "/register" else "/login") {
                style = "color: ${Colors.DARK_BLUE}; font-weight: 700; text-decoration: none;"
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.REGISTER else CommonI18nKey.LOGIN)
            }
        }
    }

    private fun DIV.benefitsCard() {
        div {
            style = BENEFITS_STYLE
            span { style = "font-size: 2rem;"; +"✦" }
            h3 {
                style = "margin: 0.75rem 0 0.45rem; font-size: 1.45rem; line-height: 1.2;"
                +translate(CommonI18nKey.ACCOUNT_BENEFITS)
            }
            p {
                style =
                    "margin: 0 0 1.25rem; color: ${Colors.CREAM_LIGHEST}; line-height: 1.5; font-size: 0.9rem;"
                +translate(CommonI18nKey.ACCOUNT_BENEFITS_INTRO)
            }
            ul {
                style = "display: grid; gap: 0.85rem; margin: 0; padding: 0; list-style: none;"
                benefit(CommonI18nKey.ACCOUNT_BENEFIT_PROGRESS)
                benefit(CommonI18nKey.ACCOUNT_BENEFIT_TEAM)
                benefit(CommonI18nKey.ACCOUNT_BENEFIT_OCR)
            }
        }
    }

    private fun UL.benefit(key: I18nKey) {
        li {
            style =
                "display: grid; grid-template-columns: 1.4rem 1fr; gap: 0.55rem; " +
                    "align-items: start; line-height: 1.4;"
            span { style = "color: ${Colors.CREAM}; font-weight: 900;"; +"✓" }
            span { +translate(key) }
        }
    }

    private fun socialButtonStyle(dark: Boolean): String =
        "display: flex; align-items: center; justify-content: center; gap: 0.65rem; width: 100%; min-height: 44px; " +
            "box-sizing: border-box; margin-bottom: 0.65rem; padding: 0.65rem 1rem; border-radius: 10px; " +
            "text-decoration: none; font-size: 0.88rem; font-weight: 700; " +
            if (dark) {
                "background: #2F292A; border: 1px solid #2F292A; color: #D9E5DC;"
            } else {
                "background: #D9E5DC; border: 1px solid #6D86AD; color: #2F292A;"
            }
}

enum class AuthMode { LOGIN, REGISTER }

private const val AUTH_LAYOUT_STYLE =
    "display: grid; grid-template-columns: minmax(0, 1.08fr) minmax(280px, 0.92fr); " +
        "max-width: 820px; margin: 2.5rem auto 0; border: 1px solid #6D86AD; " +
        "border-radius: 12px; overflow: hidden; box-shadow: 0 8px 28px rgba(20, 45, 61, 0.12); " +
        "background: #D9E5DC;"
private const val AUTH_CARD_STYLE = "padding: clamp(1.5rem, 4vw, 2.75rem);"
private const val BENEFITS_STYLE =
    "padding: clamp(1.5rem, 4vw, 2.75rem); background: linear-gradient(145deg, #334E87, #6D86AD); " +
        "color: #D9E5DC; display: flex; flex-direction: column; justify-content: center;"
private const val DIVIDER_STYLE =
    "display: flex; align-items: center; justify-content: center; margin: 1.15rem 0; " +
        "background: linear-gradient(#6D86AD, #6D86AD) center / 100% 1px no-repeat;"
private const val EYEBROW_STYLE =
    "display: inline-flex; color: #2F292A; background: #D8BC78; padding: 0.3rem 0.55rem; " +
        "border-radius: 999px; font-size: 0.68rem; font-weight: 800; letter-spacing: 0.08em; " +
        "text-transform: uppercase;"
private const val ERROR_STYLE =
    "margin: 0 0 1rem; padding: 0.7rem 0.8rem; color: #2F292A; " +
        "background: color-mix(in srgb, #C84D4C 24%, #D9E5DC); border: 1px solid #C84D4C; " +
        "border-radius: 9px; font-size: 0.82rem; font-weight: 600;"
private const val SOCIAL_ICON_STYLE =
    "display: inline-flex; align-items: center; justify-content: center; width: 1.1rem; " +
        "font-size: 1.1rem; font-weight: 800;"
