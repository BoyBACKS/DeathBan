package net.boybacks.deathban.helpers;

import net.boybacks.deathban.DeathBan;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.*;

public class ItemBuilder {

    private final ItemStack is;

    public ItemBuilder(final Material m) {
        this(m, 1);
    }

    public ItemBuilder(final ItemStack is) {
        this.is = is;
    }

    public ItemBuilder(final Material m, final int amount) {
        this.is = new ItemStack(m, amount);
    }

    public ItemBuilder(final Material m, final short durability) {
        this.is = new ItemStack(m, 1, durability);
    }

    public ItemBuilder(final Material m, final int amount, final short durability) {
        this.is = new ItemStack(m, amount, durability);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.is);
    }

    public ItemBuilder setDurability(final short dur) {
        this.is.setDurability(dur);
        return this;
    }

    public ItemBuilder setTitle(final String name) {
        final ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(DeathBan.fix(name));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(final Enchantment ench, final int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(final Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(final String owner) {
        try {
            final SkullMeta im = (SkullMeta)this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (ClassCastException ex) {}
        return this;
    }

    public ItemBuilder addGlow() {
        final ItemMeta im = this.is.getItemMeta();
        im.addEnchant(Enchantment.DURABILITY, 10, true);
        im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE });
        return this;
    }

    public ItemBuilder addEnchant(final Enchantment ench, final int level) {
        final ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments((Map)enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        final ItemMeta meta = this.is.getItemMeta();
        meta.setUnbreakable(true);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(final String... lore) {
        final ItemMeta im = this.is.getItemMeta();
        for (int i = 0; i < lore.length; ++i) {
            lore[i] = DeathBan.fix(lore[i]);
        }
        im.setLore((List)Arrays.asList(lore));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(final List<String> lore) {
        final ItemMeta im = this.is.getItemMeta();
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(final String line) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        if (!lore.contains(line)) {
            return this;
        }
        lore.remove(line);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(final int index) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        if (index < 0 || index > lore.size()) {
            return this;
        }
        lore.remove(index);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(final String line) {
        final ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<String>();
        if (im.hasLore()) {
            lore = new ArrayList<String>(im.getLore());
        }
        lore.add(line);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(final String line, final int pos) {
        final ItemMeta im = this.is.getItemMeta();
        final List<String> lore = new ArrayList<String>(im.getLore());
        lore.set(pos, line);
        im.setLore((List)lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(final Color color) {
        try {
            final LeatherArmorMeta im = (LeatherArmorMeta)this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta((ItemMeta)im);
        }
        catch (ClassCastException ex) {}
        return this;
    }

    public ItemBuilder addBookPage(final String... page) {
        final BookMeta bookMeta = (BookMeta)this.is.getItemMeta();
        bookMeta.addPage(page);
        this.is.setItemMeta((ItemMeta)bookMeta);
        return this;
    }

    public ItemBuilder setBookPage(final int page, final String data) {
        final BookMeta bookMeta = (BookMeta)this.is.getItemMeta();
        bookMeta.setPage(page, data);
        this.is.setItemMeta((ItemMeta)bookMeta);
        return this;
    }

    public ItemBuilder setBookOwner(final String player) {
        final BookMeta bookMeta = (BookMeta)this.is.getItemMeta();
        bookMeta.setAuthor(player);
        this.is.setItemMeta((ItemMeta)bookMeta);
        return this;
    }

    public ItemBuilder setBookOwner(final Player player) {
        final BookMeta bookMeta = (BookMeta)this.is.getItemMeta();
        bookMeta.setAuthor(player.getName());
        this.is.setItemMeta((ItemMeta)bookMeta);
        return this;
    }

    public ItemBuilder setBookOwner(final UUID uuid) {
        final BookMeta bookMeta = (BookMeta)this.is.getItemMeta();
        bookMeta.setAuthor(Bukkit.getPlayer(uuid).toString());
        this.is.setItemMeta((ItemMeta)bookMeta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.is;
    }
}
