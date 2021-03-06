package org.reprogle.honeypot.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.reprogle.honeypot.Honeypot;
import org.reprogle.honeypot.HoneypotConfigManager;
import org.reprogle.honeypot.commands.CommandFeedback;
import org.reprogle.honeypot.commands.HoneypotSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HoneypotLocate implements HoneypotSubCommand {
    @Override
    public String getName() {
        return "locate";
    }

    @Override
    public void perform(Player p, String[] args) {

        // If the player has locate permissions, do this
        if (!(p.hasPermission("honeypot.locate"))) {
            p.sendMessage(CommandFeedback.sendCommandFeedback("nopermission"));
            return;
        }

        final double radius = HoneypotConfigManager.getPluginConfig().getDouble("search-range");
        final double xCoord = p.getLocation().getX();
        final double yCoord = p.getLocation().getY();
        final double zCoord = p.getLocation().getZ();
        boolean potFound = false;

        // For every x value within radius
        for (double x = xCoord - radius; x < xCoord + radius; x++) {
            // For every y value within radius
            for (double y = yCoord - radius; y < yCoord + radius; y++) {
                // For every z value within radius
                for (double z = zCoord - radius; z < zCoord + radius; z++) {

                    // Check the block at coords x,y,z to see if it's a Honeypot
                    final Block b = new Location(p.getWorld(), x, y, z).getBlock();

                    // If it is a honeypot do this
                    if (Boolean.TRUE.equals(Honeypot.getHBM().isHoneypotBlock(b))) {
                        potFound = true;

                        // Create a dumb, invisible, invulnerable, block-sized glowing slime and spawn it inside the
                        // block
                        Slime slime = (Slime) Objects.requireNonNull(Bukkit.getWorld(b.getWorld().getName()))
                                .spawnEntity(b.getLocation().add(0.5, 0, 0.5), EntityType.SLIME);
                        slime.setSize(2);
                        slime.setAI(false);
                        slime.setGlowing(true);
                        slime.setInvulnerable(true);
                        slime.setHealth(4.0);
                        slime.setInvisible(true);

                        // After 5 seconds, remove the slime. Setting its health to 0 causes the death animation,
                        // removing it just makes it go away. Poof!
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                slime.remove();
                            }
                        }.runTaskLater(Honeypot.getPlugin(), 20L * 5);
                    }
                }
            }
        }

        // Let the player know if a pot was found or not
        if (potFound) {
            p.sendMessage(CommandFeedback.sendCommandFeedback("foundpot"));
        }
        else {
            p.sendMessage(CommandFeedback.sendCommandFeedback("nopotfound"));
        }
    }

    // We don't have any subcommands here, but we cannot return null otherwise the tab completer in the CommandManager
    // will throw an exception since CopyPartialMatches doesn't allow null values
    @Override
    public List<String> getSubcommands(Player p, String[] args) {
        return new ArrayList<>();
    }
}
