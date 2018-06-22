package com.gmail.nowyarek.pvpcontrol.modules.list;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;

import com.gmail.nowyarek.pvpcontrol.basic.Msg;
import com.gmail.nowyarek.pvpcontrol.basic.Text;
import com.gmail.nowyarek.pvpcontrol.configs.ConfigsAccess;
import com.gmail.nowyarek.pvpcontrol.exceptions.ModuleException;
import com.gmail.nowyarek.pvpcontrol.modules.Module;
import com.gmail.nowyarek.pvpcontrol.pvpmode.PVPModeHandler;

public class PlayerTeleportHandler extends Module implements Listener {
	private Plugin plugin;
	private ConfigsAccess configsAccess;
	private PVPModeHandler pvpModeHandler;
	
	public PlayerTeleportHandler(Plugin plugin, ConfigsAccess configsAccess, PVPModeHandler pvpModeHandler) {
		this.plugin = plugin;
		this.configsAccess = configsAccess;
		this.pvpModeHandler = pvpModeHandler;
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if(!pvpModeHandler.isPlayerInCombat(e.getPlayer().getUniqueId())) return;
		if(e.getPlayer().hasPermission("pvpc.bypass.teleports")) return;
		if(configsAccess.settings.pvp.getBlockAnyKindOfTeleportOnPVP()) {
			e.setCancelled(true);
			//say
			e.getPlayer().sendMessage(Msg.warn(Text.TELEPORTING_NOT_ALLOWED_DURING_PVP));
			return;
		}
		if(e.getCause().equals(TeleportCause.CHORUS_FRUIT)) {
			if(e.getPlayer().hasPermission("pvpc.bypass.chorusfruit")) return;
			if(configsAccess.settings.pvp.getBlockChorusFruitTeleport()) {
				e.setCancelled(true);
				//say
				e.getPlayer().sendMessage(Msg.warn(Text.CHORUS_FRUIT_NOT_ALLOWED_DURING_PVP));
			}
		}
		if(e.getCause().equals(TeleportCause.ENDER_PEARL)) {
			if(e.getPlayer().hasPermission("pvpc.bypass.enderpearl")) return;
			if(configsAccess.settings.pvp.getBlockEnderPeralTeleport()) {
				e.setCancelled(true);
				//say
				e.getPlayer().sendMessage(Msg.warn(Text.ENDER_PEARL_NOT_ALLOWED_DURING_PVP));
			}
		}
		
	}

	@Override
	public void onEnable() throws ModuleException {
		try {
			plugin.getServer().getPluginManager().registerEvents(this, plugin);
		} catch(Exception e) {
			throw new ModuleException(e, getName(), "Error while enabling module");
		}
		this.enabled = true;
	}

	@Override
	public void onDisable() throws ModuleException {
		try {
			HandlerList.unregisterAll(this);
		} catch(Exception e) {
			throw new ModuleException(e, getName(), "Error while disabling module");
		}
		this.enabled = false;
	}

	@Override
	public void onReload() throws ModuleException {
		try {
			this.onDisable();
			this.onEnable();
		} catch(Exception e) {
			throw new ModuleException(e, getName(), "Error while reloading module");
		}
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

}
