package me.terrorbyte.honeypot.api.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HoneypotPrePlayerInteractEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	private boolean isCancelled;

	private final Player player;

	private final Block block;

	/**
	 * Called before action is taken on a player who interacted with Honeypot inventory. If cancelled, the Honeypot is
	 * ignored
	 * 
	 * @param player The Player interacting with the Honeypot
	 * @param block The Honeypot block
	 */
	public HoneypotPrePlayerInteractEvent(Player player, Block block) {
		this.player = player;
		this.block = block;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	@SuppressWarnings("java:S4144")
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	public Player getPlayer() {
		return player;
	}

	public Block getBlock() {
		return block;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}
}