package com.backslide999.positionalteleport.commands;

import com.backslide999.positionalteleport.PositionalTeleportPlugin;
import org.bukkit.entity.Player;

public class TpposReload {

    public TpposReload (Player player){
        if(!player.hasPermission("tppos.reload")){
            PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,
                    PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.unauthorized"));
            return;
        }
        PositionalTeleportPlugin.getInstance().reload();
        PositionalTeleportPlugin.getInstance().sendPlayerInfo(player,
                PositionalTeleportPlugin.getInstance().fetchConfigString("messages.reload_successful"));
    }


}
