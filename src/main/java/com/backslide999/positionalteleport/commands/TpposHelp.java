package com.backslide999.positionalteleport.commands;

import com.backslide999.positionalteleport.PositionalTeleportPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpposHelp {

    public TpposHelp(Player player){
        List<String> messageList = PositionalTeleportPlugin.getInstance().fetchConfigStringList("messages.help");

        PositionalTeleportPlugin.getInstance().sendPlayerInfo(player, messageList);
    }
}
