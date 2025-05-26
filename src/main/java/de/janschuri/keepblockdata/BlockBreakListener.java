package de.janschuri.keepblockdata;

import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockDropItemEvent event) {
        Block block = event.getBlock();
        PersistentDataContainer container = new CustomBlockData(block, KeepBlockData.getInstance());

        if (container.has(KeepBlockData.BLOCK_ITEM_KEY, PersistentDataType.BYTE_ARRAY)) {

            byte[] data = container.get(KeepBlockData.BLOCK_ITEM_KEY, PersistentDataType.BYTE_ARRAY);

            container.remove(KeepBlockData.BLOCK_ITEM_KEY);

            ItemStack oldItem = deserializeItemStack(data);
            ItemStack newItem = event.getItems().get(0).getItemStack();

            if (oldItem.getType() == newItem.getType()) {
                newItem.setItemMeta(oldItem.getItemMeta());
            }
        }
    }

    public static ItemStack deserializeItemStack(byte[] data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack)dataInput.readObject();
            dataInput.close();
            return item;
        } catch (ClassNotFoundException | IOException e) {
            ((Exception)e).printStackTrace();
            return null;
        }
    }
}
