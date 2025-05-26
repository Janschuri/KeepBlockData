package de.janschuri.keepblockdata;

import com.jeff_media.customblockdata.CustomBlockData;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack blockItem = event.getItemInHand();
        Block block = event.getBlockPlaced();

        ItemMeta meta = blockItem.getItemMeta();

        if (meta != null) {
            PersistentDataContainer container = new CustomBlockData(block, KeepBlockData.getInstance());

            byte[] data = ItemStackUtils.serializeItemStack(blockItem);
            container.set(KeepBlockData.BLOCK_ITEM_KEY, PersistentDataType.BYTE_ARRAY, data);
        }
    }

}
