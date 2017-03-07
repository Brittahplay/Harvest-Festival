package joshie.harvest.debug;

import com.google.common.collect.HashMultimap;
import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.api.cooking.Utensil;
import joshie.harvest.cooking.HFCooking;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFDebugCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HFDebugCommand
@SuppressWarnings("unused")
public class CommandExportRecipeList extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "export-recipe-list";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        HashMultimap<Utensil, Recipe> set = HashMultimap.create();
        for (Recipe recipe: Recipe.REGISTRY.values()) {
            set.get(recipe.getUtensil()).add(recipe);
        }

        Utensil[] utensils = new Utensil[] { HFCooking.COUNTER, HFCooking.FRYING_PAN, HFCooking.MIXER, HFCooking.OVEN, HFCooking.POT };

        StringBuilder all = new StringBuilder();
        for (Utensil utensil: utensils) {
            String name = ReflectionHelper.getPrivateValue(Utensil.class, utensil, "label");
            StringBuilder builder = new StringBuilder();
            builder.append("|-\n" + "!").append(WordUtils.capitalize(name.replace("_", " ").toLowerCase())).append("\n").append("|");
            boolean first = true;
            List<Recipe> recipes = new ArrayList<>(set.get(utensil));
            Collections.sort(recipes, (o1, o2) -> o1.getDisplayName().compareTo(o2.getDisplayName()));

            for (Recipe recipe: recipes) {
                if (first) first = false;
                else {
                    builder.append(" • ");
                }

                builder.append("[[");
                builder.append(recipe.getDisplayName());
                builder.append("]]");
            }

            builder.append("\n");
            all.append(builder);
            Debug.save(all);
        }

        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }
}
