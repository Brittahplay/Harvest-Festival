package joshie.harvest.core.commands;

import joshie.harvest.api.HFRegister;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CalendarHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@HFRegister
public class HFCommandYear extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "year";
    }

    @Override
    public String getUsage() {
        return "<year>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                Calendar calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int day = calendar.getDate().getDay();
                Season season = calendar.getDate().getSeason();
                int year = Math.min(Integer.MAX_VALUE, Math.max(1, Integer.parseInt(parameters[0])));
                sender.getEntityWorld().setWorldTime(CalendarHelper.getTime(day, season, year));
                calendar.recalculateAndUpdate();
                return true;
            } catch (NumberFormatException ignored) {
            }
        }

        return false;
    }
}