package at.hugo.plugin.library.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds all data a GUI may need to function
 */
public class GUIData {
    /**
     * The title of the GUI
     */
    public final @NotNull Component title;
    /**
     * The number of Rows the GUI should have
     */
    public final int rows;
    /**
     * The number of Slots the GUI should have
     */
    public final int size;
    /**
     * The Background item for the GUI
     */
    public final @Nullable ItemStack fillerItem;

    /**
     * Creates the basic GUIData needed for a GUIHandler to function
     *
     * @param title      The tile for the GUI
     * @param rows       The amount of rows the GUI should have
     * @param fillerItem The Background Filler item, null to leave empty
     */
    public GUIData(final @NotNull Component title, final int rows, final @Nullable ItemStack fillerItem) {
        this.title = title;
        this.rows = rows;
        this.size = rows * 9;
        this.fillerItem = fillerItem;
    }
}
