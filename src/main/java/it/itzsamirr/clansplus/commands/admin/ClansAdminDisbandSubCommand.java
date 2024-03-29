package it.itzsamirr.clansplus.commands.admin;

import it.itzsamirr.clansplus.ClansPlus;
import it.itzsamirr.clansplus.annotations.command.SubCommandInfo;
import it.itzsamirr.clansplus.managers.clan.ClanManager;
import it.itzsamirr.clansplus.managers.configuration.lang.LangManager;
import it.itzsamirr.clansplus.model.clan.Clan;
import it.itzsamirr.clansplus.model.command.ConfirmationSubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

@SubCommandInfo(name = "disband", aliases = "delete", permission = "clansplus.admin.disband")
public class ClansAdminDisbandSubCommand extends ConfirmationSubCommand {
    public ClansAdminDisbandSubCommand(ClansPlus plugin) {
        super(plugin);
    }

    @Override
    public boolean run(CommandSender sender, String[] args) {
        LangManager langManager = api.get(LangManager.class);
        ClanManager clanManager = api.get(ClanManager.class);
        if(args.length < 1){
            sender.sendMessage(langManager.getLanguage().getString("clan-admin-disband-usage"));
            return false;
        }
        String clanName = args[0];
        Clan clan = clanManager.getClan(clanName);
        if(clan == null){
            sender.sendMessage(langManager.getLanguage().getString("clan-not-found").replace("{name}", clanName));
            return false;
        }
        if(!isConfirming(sender)){
            setConfirming(sender, System.currentTimeMillis());
            sender.sendMessage(langManager.getLanguage().getString("clan-admin-disband-confirm").replace("{clan}", clan.getName()));
            return false;
        }
        if(isConfirmingExpired(sender, plugin.getConfig().getDouble("clans.confirm-limit"))){
            removeConfirming(sender);
            sender.sendMessage(langManager.getLanguage().getString("confirming-expired"));
            return false;
        }
        removeConfirming(sender);
        sender.sendMessage(langManager.getLanguage().getString("clan-admin-disband-done").replace("{clan}", clan.getName()));
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return api.get(ClanManager.class).getClans()
                .stream().sorted().map(Clan::getName)
                .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
    }
}
