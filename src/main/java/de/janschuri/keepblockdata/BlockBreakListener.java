package de.janschuri.keepblockdata;

import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
                ItemMeta meta = newItem.getItemMeta();

                String oldName = oldItem.getItemMeta().getDisplayName();
                List<String> oldLore = oldItem.getItemMeta().getLore();
                Map<Enchantment, Integer> oldEnchants = oldItem.getItemMeta().getEnchants();

                if (oldName != null) {
                    meta.setDisplayName(oldName);
                }

                if (oldLore != null) {
                    meta.setLore(oldLore);
                }

                if (oldEnchants != null && !oldEnchants.isEmpty()) {
                    for (Map.Entry<Enchantment, Integer> entry : oldEnchants.entrySet()) {
                        meta.addEnchant(entry.getKey(), entry.getValue(), true);
                    }
                }

                newItem.setItemMeta(meta);
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
