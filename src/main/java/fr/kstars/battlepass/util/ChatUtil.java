package fr.kstars.battlepass.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ChatUtil {
    public static Component PLUGIN_PREFIX_WITH_COLOR = Component.text("[Battlepass]", NamedTextColor.DARK_RED, TextDecoration.BOLD);
    public static String PLUGIN_PREFIX_WITHOUT_COLOR = "[Battlepass] ";
}
