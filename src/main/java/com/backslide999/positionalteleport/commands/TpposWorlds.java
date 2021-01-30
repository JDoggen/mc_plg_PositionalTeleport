package com.backslide999.positionalteleport.commands;

import com.backslide999.positionalteleport.PositionalTeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpposWorlds {

    public TpposWorlds(Player player){
        List<String> worlds = new ArrayList();
        Bukkit.getWorlds()
                .stream()
                .map(world -> world.getName())
                .forEach(name -> worlds.add(name));
        PositionalTeleportPlugin.getInstance().sendPlayerInfo(player, worlds);
    }
}
