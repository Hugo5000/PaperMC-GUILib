package at.hugob.plugin.library.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.ParameterizedType;

/**
 * Has All the basic Bukkit Listeners needed for a GUI to function
 * @param <Plugin> The plugin this GUI Listener is responsible for
 */
public abstract class GUIListener<Plugin extends JavaPlugin> implements Listener {
    private final Class<Plugin> pluginClass;

    /**
     * Sets up everything needed for the GUIListener
     */
    protected GUIListener() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        pluginClass = (Class<Plugin>) type.getActualTypeArguments()[0];
    }

    /**
     * Listens to the InventoryClickEvent and delegates it to the correct GUIHandler
     *
     * @param event the InventoryClickEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        // Check if the inventory owner is my GUIHandler
        if (!(event.getView().getTopInventory().getHolder() instanceof GUIHandler<? extends JavaPlugin, ? extends GUIData> guiHandler)) return;
        // Check if this is my plugins handler
        if(!guiHandler.plugin.getClass().equals(pluginClass)) return;
        // get the Inventory that was Clicked
        var inventory = event.getClickedInventory();
        // No inventory was Clicked
        if (inventory == null) guiHandler.onClickOutside(event);
            // Top inventory was Clicked
        else if (inventory.equals(event.getView().getTopInventory())) guiHandler.onClickTop(event);
            // Bottom inventory was Clicked
        else guiHandler.onClickBottom(event);
    }

    /**
     * Listens to the InventoryDragEvent and delegates it to the correct GUIHandler
     *
     * @param event the InventoryDragEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        // Check if the inventory owner is my GUIHandler
        if (event.getView().getTopInventory().getHolder() instanceof GUIHandler<? extends JavaPlugin, ? extends GUIData> guiHandler) {
            // Item was Dragged
            guiHandler.onDrag(event);
        }
    }

    /**
     * Listens to the InventoryCloseEvent and delegates it to the correct GUIHandler
     *
     * @param event the InventoryCloseEvent
     */
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event) {
        // Check if the inventory owner is my GUIHandler
        if (event.getView().getTopInventory().getHolder() instanceof GUIHandler<? extends JavaPlugin, ? extends GUIData> guiHandler) {
            // Inventory Closed
            guiHandler.onClose(event);
        }
    }
}
