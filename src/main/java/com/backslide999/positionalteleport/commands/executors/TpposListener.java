package com.backslide999.positionalteleport.commands.executors;

import com.backslide999.positionalteleport.PositionalTeleportPlugin;
import com.backslide999.positionalteleport.commands.Tppos;
import com.backslide999.positionalteleport.commands.TpposHelp;
import com.backslide999.positionalteleport.commands.TpposReload;
import com.backslide999.positionalteleport.commands.TpposWorlds;
import com.backslide999.positionalteleport.containers.Mode;
import com.backslide999.positionalteleport.exceptions.UnauthorizedException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


public class TpposListener implements CommandExecutor {

    public TpposListener(){
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(!(sender instanceof Player)){
            PositionalTeleportPlugin.getInstance().sendPlayerWarning(sender,
                    PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.no_command_line"));
            return true;
        }

        Mode mode = null;
        if(args.length >= 1){
            try{
                mode = Mode.valueOf(args[0].toUpperCase());
                args = Arrays.copyOfRange(args, 1, args.length);
            } catch(IllegalArgumentException iae){
                try{
                    String defaultMode = PositionalTeleportPlugin.getInstance().fetchConfigString("teleport.mode").toUpperCase();
                    mode = Mode.valueOf(defaultMode);
                } catch(Exception e){
                    PositionalTeleportPlugin.getInstance().sendPlayerWarning(sender, "An internal error occured!");
                }
            }
        }




        Player player = (Player) sender;
        if(args != null && args.length != 0){
            if(args.length >= 1)
            switch(args.length) {
                case 1: {
                    if(args[0].equalsIgnoreCase("reload")){
                        new TpposReload(player);
                    } else if(args[0].equalsIgnoreCase("worlds")){
                        new TpposWorlds(player);
                    } else {
                        new TpposHelp(player);
                    }
                    break;
                }
                case 3: {
                    new Tppos(player, mode, args[0], args[1], args[2]);
                    break;
                }
                case 4: {
                    new Tppos(player, mode, args[0], args[1], args[2], args[3]);
                    break;
                }
                default: {
                    new TpposHelp(player);
                    break;
                }
            }
        } else{
            new TpposHelp(player);
        }
        return true;
    }
}
