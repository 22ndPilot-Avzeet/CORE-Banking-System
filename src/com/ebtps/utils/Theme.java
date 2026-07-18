package com.ebtps.utils;

import java.awt.Color;
import java.awt.Font;

/**
 * Defines the monochrome, minimalist visual theme for the EBTPS application.
 */
public class Theme {
    
    // Monochrome Color Palette
    public static final Color PRIMARY_BACKGROUND = new Color(255, 255, 255); // White
    public static final Color SECONDARY_BACKGROUND = new Color(245, 245, 245); // Very Light Gray (#F5F5F5)
    public static final Color PRIMARY_TEXT = new Color(0, 0, 0); // Black
    public static final Color SECONDARY_TEXT = new Color(85, 85, 85); // Dark Gray (#555555)
    public static final Color BORDER_COLOR = new Color(221, 221, 221); // Light Gray (#DDDDDD)
    
    // Status Colors (Sparingly used)
    public static final Color SUCCESS_COLOR = new Color(76, 175, 80); // Muted Dark Green
    public static final Color ERROR_COLOR = new Color(244, 67, 54); // Muted Red
    public static final Color WARNING_COLOR = new Color(255, 152, 0); // Muted Orange

    // Fonts
    public static final String FONT_FAMILY = "Segoe UI"; // Default to a clean system font
    
    public static final Font HEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 24);
    public static final Font SUBHEADER_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);
    public static final Font NORMAL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font(FONT_FAMILY, Font.BOLD, 14);
}
