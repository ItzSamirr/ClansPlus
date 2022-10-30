package it.itzsamirr.clansplus.commands;

import it.itzsamirr.clansplus.ClansPlus;
import it.itzsamirr.clansplus.managers.clan.ClanManager;
import it.itzsamirr.clansplus.managers.configuration.lang.LangManager;
import it.itzsamirr.clansplus.model.clan.Clan;
import it.itzsamirr.clansplus.model.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClansCreateSubCommand extends SubCommand {
    public ClansCreateSubCommand(ClansPlus plugin) {
        super(plugin, "create", "", true);
    }

    @Override
    public boolean run(Player player, String[] args) {
        ClanManager clanManager = api.getManager(ClanManager.class);
        LangManager langManager = api.getManager(LangManager.class);
        if(clanManager.getClan(args[0]) != null){
            player.sendMessage(langManager.getLanguage().getString("clan-already-exists").replace("{name}", args[0]));
            return false;
        }
        if(clanManager.getClan(player.getUniqueId()) != null){
            player.sendMessage(langManager.getLanguage().getString("already-in-a-clan").replace("{clan}", args[0]));
            return false;
        }
        Clan clan = clanManager.addClan(args[0], player.getUniqueId());
        player.sendMessage(langManager.getLanguage().getString("clan-created").replace("{clan}", clan.getName()));
        return false;
    }
}
