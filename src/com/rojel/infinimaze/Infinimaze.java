package com.rojel.infinimaze;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Infinimaze extends JavaPlugin {
	private static Infinimaze plugin;
	
	public void onEnable() {
		plugin = this;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("maze")) {
			if(sender instanceof Player) {
				MazeBuilder builder = new MazeBuilder();
				builder.buildMaze(10, 10);
				Player player = (Player) sender;
				builder.placeInWorld(player.getWorld(), player.getLocation());
			} else {
				sender.sendMessage("Consoles can't be in mazes, silly.");
			}
		}
		
		return false;
	}
	
	public static Infinimaze getPlugin() {
		return plugin;
	}
}
