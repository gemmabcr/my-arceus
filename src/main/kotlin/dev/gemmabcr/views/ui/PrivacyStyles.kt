package dev.gemmabcr.views.ui

internal val privacyStyles =
    """
        .privacy-content a {
            color: ${Colors.DARK_BLUE};
            font-weight: 700;
        }
        .privacy-content {
            max-width: 860px;
            margin: 0 auto;
            padding: clamp(1rem, 3vw, 2rem);
            overflow: hidden;
        }
        .privacy-hero {
            display: flex;
            align-items: center;
            gap: 1rem;
            margin: calc(clamp(1rem, 3vw, 2rem) * -1);
            margin-bottom: 0;
            padding: clamp(1.25rem, 4vw, 2rem);
            background: linear-gradient(135deg, ${Colors.DARK_BLUE}, ${Colors.DARKEST_BLUE});
            color: ${Colors.ON_DARK_BLUE};
        }
        .privacy-hero-mark {
            display: grid;
            flex: 0 0 auto;
            width: 3.4rem;
            height: 3.4rem;
            place-items: center;
            border: 1px solid rgba(255, 255, 255, 0.28);
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.12);
            font-size: 1.45rem;
            font-weight: 800;
        }
        .privacy-hero-copy { min-width: 0; }
        .privacy-hero-controller {
            margin: 0.2rem 0 0.35rem;
            font-size: 1.08rem;
            font-weight: 800;
        }
        .privacy-updated {
            margin: 0;
            color: ${Colors.ON_DARK_BLUE};
            font-size: 0.74rem;
            opacity: 0.78;
        }
        .privacy-contact-link {
            color: ${Colors.ON_DARK_BLUE} !important;
            font-size: 0.82rem;
            text-underline-offset: 0.18rem;
        }
        .privacy-navigation {
            display: flex;
            flex-wrap: wrap;
            gap: 0.45rem;
            margin: 0 -0.2rem;
            padding: 1.15rem 0.2rem 1.35rem;
            overflow-x: auto;
            scrollbar-width: thin;
        }
        .privacy-navigation a {
            flex: 0 0 auto;
            padding: 0.42rem 0.7rem;
            border: 1px solid ${Colors.BLUE_GREY};
            border-radius: 999px;
            background: rgba(255, 255, 255, 0.45);
            font-size: 0.72rem;
            text-decoration: none;
            white-space: nowrap;
        }
        .privacy-navigation a:hover {
            border-color: ${Colors.DARK_BLUE};
            background: ${Colors.WHITE};
        }
        .privacy-section {
            display: grid;
            grid-template-columns: minmax(180px, 0.7fr) minmax(0, 1.7fr);
            gap: clamp(1rem, 3vw, 2rem);
            padding: 1.25rem 0;
            border-top: 1px solid color-mix(in srgb, ${Colors.BLUE_GREY} 65%, transparent);
            scroll-margin-top: 1rem;
        }
        .privacy-section-featured {
            margin: 0 -0.75rem;
            padding: 1.25rem 0.75rem;
            border: 1px solid ${Colors.BLUE_GREY};
            border-radius: 10px;
            background: color-mix(in srgb, ${Colors.SKY} 10%, ${Colors.CREAM_LIGHEST});
        }
        .privacy-section-heading {
            display: flex;
            align-items: flex-start;
            gap: 0.65rem;
        }
        .privacy-section-number {
            display: inline-grid;
            flex: 0 0 auto;
            min-width: 2rem;
            height: 2rem;
            place-items: center;
            border-radius: 7px;
            background: ${Colors.DARK_BLUE};
            color: ${Colors.ON_DARK_BLUE};
            font-size: 0.66rem;
            font-weight: 800;
            letter-spacing: 0.04em;
        }
        .privacy-section h3 {
            margin: 0.3rem 0 0;
            color: ${Colors.DARKEST_BLUE};
            font-size: 0.98rem;
            line-height: 1.35;
        }
        .privacy-section p,
        .privacy-section li {
            color: ${Colors.DARK_BLUE};
            font-size: 0.88rem;
            line-height: 1.68;
        }
        .privacy-section p { margin: 0 0 0.7rem; }
        .privacy-section p:last-child { margin-bottom: 0; }
        .privacy-section ul { margin: 0; padding-left: 1.15rem; }
        .privacy-section li + li { margin-top: 0.55rem; }
        @media (max-width: 620px) {
            .privacy-hero { align-items: flex-start; }
            .privacy-hero-mark { width: 2.8rem; height: 2.8rem; }
            .privacy-navigation { flex-wrap: nowrap; }
            .privacy-section { grid-template-columns: 1fr; gap: 0.75rem; }
            .privacy-section-featured { margin: 0 -0.35rem; padding: 1rem 0.35rem; }
        }
    """.trimIndent()
