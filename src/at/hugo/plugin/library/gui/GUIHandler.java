package at.hugo.plugin.library.gui;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Manages the GUI and is also the Holder of the GUI Inventory
 *
 * @param <Plugin>  The plugin which owns this GUI
 * @param <GUIData> The GUIData that is needed for this GUI to function
 */
public abstract class GUIHandler<Plugin extends JavaPlugin, GUIData extends at.hugo.plugin.library.gui.GUIData> implements InventoryHolder {
    /**
     * The NameSpacedKey used by Inventory ItemStacks
     */
    protected final @NotNull NamespacedKey guiItemKey;
    /**
     * The Plugin that owns this GUI
     */
    protected final @NotNull Plugin plugin;
    /**
     * The GUIData needed for this GUI
     */
    protected final @NotNull GUIData guiData;
    /**
     * The Inventory backing this GUI
     */
    private final @NotNull Inventory inventory;

    /**
     * Creates a GUIHandler
     *
     * @param plugin  The Plugin Instance that owns of this GUI
     * @param guiData The data needed for this GUI to function
     */
    protected GUIHandler(final @NotNull Plugin plugin, final @NotNull GUIData guiData) {
        this.guiItemKey = NamespacedKey.fromString("gui_item", plugin);
        this.plugin = plugin;
        this.guiData = guiData;
        this.inventory = Bukkit.createInventory(this, guiData.size, guiData.title);

        fillAll(guiData.fillerItem);
    }

    /**
     * Fills All slots of this gui with a specific Item
     *
     * @param itemStack The ItemStack that will be in every slot
     */
    protected void fillAll(final @Nullable ItemStack itemStack) {
        for (int i = 0; i < inventory.getSize(); i++) {
            setItem(i, itemStack);
        }
    }

    @Override
    public final @NotNull Inventory getInventory() {
        return inventory;
    }

    /**
     * Open the GUI
     *
     * @param player the player who will see this GUI
     */
    public void open(final @NotNull Player player) {
        // only open the inventory synchronously
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> open(player));
            return;
        }
        update();
        player.openInventory(getInventory());
    }

    /**
     * Fills the inventory with the inventory content.
     * Try to not use fillAll and instead only replace what is necessary
     */
    protected abstract void update();

    /**
     * Sets a slot in this inventory to that item
     *
     * @param index     the slot in the inventory
     * @param itemStack the item that needs to be put in the inventory
     */
    protected void setItem(final int index, @Nullable ItemStack itemStack) {
        if (itemStack != null) {
            itemStack = itemStack.clone();
            var meta = itemStack.getItemMeta();
            meta.getPersistentDataContainer().set(guiItemKey, PersistentDataType.BYTE, (byte) 1);
            itemStack.setItemMeta(meta);
        }
        inventory.setItem(index, itemStack);
    }

    /**
     * Checks if an item is from this inventory
     *
     * @param itemStack The item to check
     * @return true if it is an item from this inventory
     */
    protected boolean isInventoryItem(final @Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().has(guiItemKey);
    }

    /**
     * Handles a Click Event which is targeted at the Top (this) Inventory of an Inventory View
     *
     * @param event The InventoryClickEvent Targeted at the Top Inventory
     */
    public void onClickTop(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handles a Click Event which is targeted at the Bottom (Player's) Inventory of an Inventory View
     *
     * @param event The InventoryClickEvent Targeted at the Bottom Inventory
     */
    public void onClickBottom(InventoryClickEvent event) {
        boolean cursorIsInventoryItem = isInventoryItem(event.getCursor());

        // don't allow shifting to the top inventory everything else is fine in this inventory
        if (event.isShiftClick() && getInventory().firstEmpty() != -1)
            event.setCancelled(true);
        // don't allow dropping a inventoryItem into the bottom inventory
        if (cursorIsInventoryItem) {
            switch (event.getClick()) {
                case LEFT, RIGHT, DOUBLE_CLICK -> event.setCancelled(true);
            }
        }
    }

    /**
     * Handles a Click Event which is targeted at Left or Right side, outside the Inventory View
     *
     * @param event The InventoryClickEvent Targeted outside the Inventory
     */
    public void onClickOutside(InventoryClickEvent event) {
        if (isInventoryItem(event.getCursor())) event.setCancelled(true);
    }

    /**
     * Handles a Drag Event Targeted at an Inventory View that has This Inventory as the Top one.
     *
     * @param event The InventoryDragEvent Targeted at an InventoryView that contains this inventory
     */
    public void onDrag(InventoryDragEvent event) {
        if (event.getRawSlots().stream().anyMatch(slot -> slot < getInventory().getSize()))
            event.setCancelled(true);
    }

    /**
     * Gets Triggered if this Inventory is closed
     *
     * @param event The InventoryCloseEvent that contains this Inventory
     */
    public void onClose(InventoryCloseEvent event) {
        // remove any inventory item that may still be on the cursor
        if (isInventoryItem(event.getPlayer().getItemOnCursor())) event.getPlayer().setItemOnCursor(null);
    }
}
