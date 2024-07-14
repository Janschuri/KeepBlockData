package de.janschuri.keepblockdata;

import com.jeff_media.customblockdata.CustomBlockData;
import de.janschuri.lunaticlib.platform.bukkit.util.ItemStackUtils;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockDropItemEvent event) {
        Block block = event.getBlock();
        PersistentDataContainer container = new CustomBlockData(block, KeepBlockData.getInstance());

        if (container.has(KeepBlockData.BLOCK_ITEM_KEY, PersistentDataType.BYTE_ARRAY)) {

            byte[] data = container.get(KeepBlockData.BLOCK_ITEM_KEY, PersistentDataType.BYTE_ARRAY);

            container.remove(KeepBlockData.BLOCK_ITEM_KEY);

            ItemStack oldItem = ItemStackUtils.deserializeItemStack(data);
            ItemStack newItem = event.getItems().get(0).getItemStack();

            if (oldItem.getType() == newItem.getType()) {
                ItemMeta meta = newItem.getItemMeta();

                String oldName = oldItem.getItemMeta().getDisplayName();
                List<String> oldLore = oldItem.getItemMeta().getLore();

                if (oldName != null) {
                    meta.setDisplayName(oldName);
                }

                if (oldLore != null) {
                    meta.setLore(oldLore);
                }

                newItem.setItemMeta(meta);
            }
        }
    }
}
