package com.backslide999.positionalteleport.commands;

import com.backslide999.positionalteleport.PositionalTeleportPlugin;
import com.backslide999.positionalteleport.containers.Mode;
import com.backslide999.positionalteleport.exceptions.UnauthorizedException;


import com.earth2me.essentials.utils.LocationUtil;
import com.wimbli.WorldBorder.WorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.wimbli.WorldBorder.BorderData;

import javax.swing.text.Position;
import java.text.MessageFormat;

public class Tppos {

    public Tppos(Player player, Mode mode, String xarg, String yarg, String zarg){
        this(player, mode, xarg, yarg, zarg, "");
    }

    public Tppos(Player player, Mode mode, String xarg, String yarg, String zarg, String worldName){
        if(!player.hasPermission("tppos.teleport")){
            PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,
                    PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.unauthorized"));
            return;
        }

        try{

            Boolean useX = PositionalTeleportPlugin.getInstance().fetchConfigBoolean("teleport.x.use");
            Integer xMin = null;
            Integer xMax = null;
            Boolean useY = PositionalTeleportPlugin.getInstance().fetchConfigBoolean("teleport.y.use");
            Integer yMin = null;
            Integer yMax = null;
            Boolean useZ = PositionalTeleportPlugin.getInstance().fetchConfigBoolean("teleport.z.use");
            Integer zMin = null;
            Integer zMax = null;

            if(useX){
                xMin = PositionalTeleportPlugin.getInstance().fetchConfigInteger("teleport.x.min");
                xMax = PositionalTeleportPlugin.getInstance().fetchConfigInteger("teleport.x.max");
            }

            if(useY){
                yMin = PositionalTeleportPlugin.getInstance().fetchConfigInteger("teleport.y.min");
                yMax = PositionalTeleportPlugin.getInstance().fetchConfigInteger("teleport.y.max");
            }

            if(useZ){
                zMin = PositionalTeleportPlugin.getInstance().fetchConfigInteger("teleport.z.min");
                zMax = PositionalTeleportPlugin.getInstance().fetchConfigInteger("teleport.z.max");
            }

            Integer x = null;
            Integer y = null;
            Integer z = null;

            try {
                x = Integer.valueOf(xarg);
                y = Integer.valueOf(yarg);
                z = Integer.valueOf(zarg);
            } catch(Exception e){
                new TpposHelp(player);
            }

            if(!worldName.equals("")){
                if(!player.hasPermission("tppos.teleport.worlds")){
                    throw new UnauthorizedException(
                            PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.not_allowed_world")
                    );
                } else if(!player.hasPermission("tppos.teleport.worlds." + worldName)
                        && !player.hasPermission("tppos.teleport.worlds.all")){
                    throw new UnauthorizedException(
                            PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.not_allowed_world_specific")
                    );
                }
            }

            if(worldName.equals("")) worldName = player.getWorld().getName();

            World world = Bukkit.getWorld(worldName);

            if(world == null){
                PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,
                        PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.world_not_exists"));
                return;
            }

            try{
                if(!this.validateBorderData(player, x, y, z, world)){
                    PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,
                            PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.outside_border"));
                    return;
                }
            } catch(Exception e){
                PositionalTeleportPlugin.getInstance().getLogger().warning(e.getMessage());
                PositionalTeleportPlugin.getInstance().sendPlayerWarning(player, "An internal error occured.");
                return;
            }

            if(useX && (x < xMin || x > xMax)){
                PositionalTeleportPlugin.getInstance().sendPlayerWarning(player, new MessageFormat(
                        PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.x_incorrect")
                ).format(new Integer[]{xMin, xMax}));
                return;
            }

            if(useY && (y < yMin || y > yMax)){
                PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,  new MessageFormat(
                        PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.y_incorrect")
                ).format(new Integer[]{yMin, yMax}));
                return;
            }

            if(useZ && (z < zMin || z > zMax)){
                PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,  new MessageFormat(
                        PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.z_incorrect")
                ).format(new Integer[]{zMin, zMax}));
                return;
            }



            Location loc = new Location(world, x, y, z);
            if(mode == Mode.SAFE){
                try{
                    loc = LocationUtil.getSafeDestination(loc);
                } catch(Exception e){
                    PositionalTeleportPlugin.getInstance().sendPlayerWarning(player,
                            PositionalTeleportPlugin.getInstance().fetchConfigString("messages.errors.destination_not_safe"));
                    return;
                }
            }

            PositionalTeleportPlugin.getInstance().sendPlayerInfo(player,
                    PositionalTeleportPlugin.getInstance().fetchConfigString("messages.teleporting"));
            player.teleport(loc);

        } catch(UnauthorizedException uae) {
            PositionalTeleportPlugin.getInstance().sendPlayerWarning(player, uae.getMessage());
        } catch(NumberFormatException nfe){
            new TpposHelp(player);
        }
    }


    private boolean validateBorderData(Player player, Integer x, Integer y, Integer z, World world) throws Exception{
        Boolean useWorldBorder = PositionalTeleportPlugin.getInstance().fetchConfigBoolean("api.worldborder");
        if(!useWorldBorder) return true;
        else if(player.hasPermission("tppos.api.worldborder.override")) return true;
        else {
            if(Bukkit.getPluginManager().isPluginEnabled("WorldBorder")){
                try{
                    WorldBorder wb = (WorldBorder) Bukkit.getServer().getPluginManager().getPlugin("WorldBorder");
                    BorderData data = wb.getWorldBorder(world.getName());
                    if(data == null) return true; //No World Border Data
                    Location loc = new Location(world, x, y, z);
                    return data.insideBorder(loc);

                }  catch(ClassCastException ncdf){
                    throw new Exception("Found incompatible version of WorldBorder. Please disable WorldBorder API in config.yml");
                }
            } else {
                throw new Exception("WorldBorder is disabled! Please enable plugin, or disable API in config.yml");
            }
        }
    }
}


