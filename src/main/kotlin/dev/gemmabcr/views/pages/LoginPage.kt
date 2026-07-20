package dev.gemmabcr.views.pages

import dev.gemmabcr.models.Session
import dev.gemmabcr.views.i18n.CommonI18nKey
import dev.gemmabcr.views.i18n.I18nKey
import dev.gemmabcr.views.ui.Colors
import dev.gemmabcr.views.ui.HtmlLayout
import kotlinx.html.ButtonType
import kotlinx.html.DIV
import kotlinx.html.FlowContent
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.button
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

class LoginPage(
    private val mode: AuthMode = AuthMode.LOGIN,
    private val error: I18nKey? = null,
    private val googleEnabled: Boolean = false,
    private val appleEnabled: Boolean = false,
    session: Session = Session(),
) : HtmlLayout(if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN else CommonI18nKey.CREATE_ACCOUNT, session, false) {
    override fun DIV.content() {
        div(classes = "auth-layout") {
            style = authLayoutStyle
            authCard()
            benefitsCard()
        }
    }

    private fun DIV.authCard() {
        div {
            style = authCardStyle
            span {
                style = eyebrowStyle
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.WELCOME_BACK else CommonI18nKey.REGISTER)
            }
            h2 {
                style = "margin: 0.55rem 0 0.4rem; color: ${Colors.DARKEST_BLUE}; font-size: 1.75rem;"
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN else CommonI18nKey.CREATE_ACCOUNT)
            }
            p {
                style = "margin: 0 0 1.35rem; color: #5f6772; line-height: 1.5; font-size: 0.92rem;"
                +translate(
                    if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN_INTRO else CommonI18nKey.CREATE_ACCOUNT_INTRO
                )
            }
            error?.let {
                p {
                    attributes["role"] = "alert"
                    style = errorStyle
                    +translate(it)
                }
            }
            socialLoginButton("/auth/google", "G", CommonI18nKey.GOOGLE_LOGIN, googleEnabled, false)
            socialLoginButton("/auth/apple", "", CommonI18nKey.APPLE_LOGIN, appleEnabled, true)
            div {
                style = dividerStyle
                span {
                    style = "padding: 0 0.7rem; background: white; color: #777f87; font-size: 0.75rem;"
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
                    style = "margin: -0.35rem 0 0; color: #777f87; font-size: 0.75rem;"
                    +translate(CommonI18nKey.PASSWORD_REQUIREMENTS)
                }
            }
            button(type = ButtonType.submit) {
                style = submitButtonStyle
                +translate(if (mode == AuthMode.LOGIN) CommonI18nKey.LOGIN else CommonI18nKey.CREATE_ACCOUNT_SUBMIT)
            }
        }
    }

    private fun FlowContent.authInput(key: I18nKey, name: String, type: InputType, placeholder: String) {
        div {
            label {
                htmlFor = name
                style = "display: block; margin-bottom: 0.4rem; color: #424954; font-size: 0.8rem; font-weight: 700;"
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
                style = inputStyle
            }
        }
    }

    private fun DIV.socialLoginButton(href: String, icon: String, label: I18nKey, enabled: Boolean, dark: Boolean) {
        if (enabled) {
            a(href = href) {
                style = socialButtonStyle(dark)
                span { style = socialIconStyle; +icon }
                +translate(label)
            }
        } else {
            span {
                attributes["aria-disabled"] = "true"
                attributes["title"] = translate(CommonI18nKey.OAUTH_NOT_CONFIGURED)
                style = socialButtonStyle(dark) + " opacity: 0.45; cursor: not-allowed;"
                span { style = socialIconStyle; +icon }
                +translate(label)
            }
        }
    }

    private fun DIV.accountSwitch() {
        p {
            style = "margin: 1.15rem 0 0; text-align: center; color: #6c737c; font-size: 0.82rem;"
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
            style = benefitsStyle
            span { style = "font-size: 2rem;"; +"✦" }
            h3 {
                style = "margin: 0.75rem 0 0.45rem; font-size: 1.45rem; line-height: 1.2;"
                +translate(CommonI18nKey.ACCOUNT_BENEFITS)
            }
            p {
                style = "margin: 0 0 1.25rem; color: #dce8ef; line-height: 1.5; font-size: 0.9rem;"
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

    private fun kotlinx.html.UL.benefit(key: I18nKey) {
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
            if (dark) "background: #171717; border: 1px solid #171717; color: white;" else
                "background: white; border: 1px solid #d8dce0; color: #31343a;"

    private companion object {
        const val authLayoutStyle =
            "display: grid; grid-template-columns: minmax(0, 1.08fr) minmax(280px, 0.92fr); " +
                "max-width: 820px; margin: 2.5rem auto 0; border-radius: 18px; overflow: hidden; " +
                "box-shadow: 0 18px 55px rgba(20, 45, 61, 0.14); background: white;"
        const val authCardStyle = "padding: clamp(1.5rem, 4vw, 2.75rem);"
        const val benefitsStyle =
            "padding: clamp(1.5rem, 4vw, 2.75rem); background: linear-gradient(145deg, #17364a, #285d73); " +
                "color: white; display: flex; flex-direction: column; justify-content: center;"
        const val dividerStyle =
            "display: flex; align-items: center; justify-content: center; margin: 1.15rem 0; " +
                "background: linear-gradient(#e3e6e8, #e3e6e8) center / 100% 1px no-repeat;"
        const val eyebrowStyle =
            "display: inline-flex; color: #507287; background: #edf3f5; padding: 0.3rem 0.55rem; " +
                "border-radius: 999px; font-size: 0.68rem; font-weight: 800; letter-spacing: 0.08em; " +
                "text-transform: uppercase;"
        const val errorStyle =
            "margin: 0 0 1rem; padding: 0.7rem 0.8rem; color: #8e2525; background: #fff0ef; " +
                "border: 1px solid #f2c7c3; border-radius: 9px; font-size: 0.82rem; font-weight: 600;"
        const val inputStyle =
            "width: 100%; box-sizing: border-box; padding: 0.72rem 0.8rem; border: 1px solid #d8dce0; " +
                "border-radius: 9px; background: #fff; color: #252a30; font: inherit; font-size: 0.88rem; " +
                "outline-color: #507287;"
        const val submitButtonStyle =
            "width: 100%; min-height: 44px; margin-top: 0.15rem; padding: 0.7rem 1rem; border: 0; " +
                "border-radius: 10px; background: #285d73; color: white; cursor: pointer; font: inherit; " +
                "font-size: 0.88rem; font-weight: 800;"
        const val socialIconStyle =
            "display: inline-flex; align-items: center; justify-content: center; width: 1.1rem; " +
                "font-size: 1.1rem; font-weight: 800;"
    }
}

enum class AuthMode { LOGIN, REGISTER }
