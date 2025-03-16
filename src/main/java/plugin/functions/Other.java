package plugin.functions;

import arc.util.Log;
import arc.util.Timer;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.net.NetConnection;
import mindustry.server.ServerControl;
import plugin.models.wrappers.PlayerData;
import plugin.utils.MenuHandler;
import useful.Bundle;

import java.time.Duration;
import java.util.Arrays;
import java.util.Date;

import static plugin.ConfigJson.discordUrl;

public class Other {
    public static void PlaytimeTimer() {
        Timer.schedule(() -> {
            for (Player player : Groups.player) {
                PlayerData data = new PlayerData(player);
                if (data.isExist())
                    data.playtimeIncrease();
            }
        }, 0, 60);
    }

    public static void welcomeMenu(Player player) {
        String title = "\uE86B Добро пожаловать!";
        String description = """
                [white]Добро пожаловать на [#00bfff]Frost[#99e6ff]Heaven!

                [red]<[orange]Правила[red]>
                [#f]\uE815 [white]Запрещён гриф, слив ресурсов, юнитов и прочие помехи игре.
                [#f]\uE815 [white]Запрещён NSFW контент, реклама в любом виде.
                [#f]\uE815 [white]Запрещены низкоэффективные и громоздкие схемы.
                [green]\uE800 [white]Ведите себя адекватно, не мешайте игре и общению.

                [white]Используйте /help чтобы увидеть все доступные команды.
                [blue]\uE80D И обязательно зайдите на наш дискорд-сервер.""";
        String button1 = "Закрыть";
        String button2 = "[blue]\uE80D Перейти в discord!";
        Call.menu(player.con, MenuHandler.welcomeMenu, title, description, new String[][]{{button1}, {button2}});
    }

    public static void kickIfBanned(NetConnection player) {
        PlayerData data = new PlayerData(player);
        if (data.isExist()) {
            long lastBan = data.getLastBanTime();
            Date date = new Date();
            if (lastBan > date.getTime()) {
                String timeUntilUnban = Bundle.formatDuration(lastBan - date.getTime());
                player.kick("[red]You have been banned!\n\n" + "[white]Duration: " + timeUntilUnban + "\nYour ID: " + data.getId() + " until unban\n\nIf you think this is a mistake, make sure to appeal ban in our discord: " + discordUrl, 0);
            }
        }
    }

    public static void kickIfBanned(Player player) {
        PlayerData data = new PlayerData(player);
        if (data.isExist()) {
            long lastBan = data.getLastBanTime();
            Date date = new Date();
            if (lastBan > date.getTime()) {
                String timeUntilUnban = Bundle.formatDuration(lastBan - date.getTime());
                player.kick("[red]You have been banned!\n\n" + "[white]Duration: " + timeUntilUnban + "\nYour ID: " + data.getId() + " until unban\n\nIf you think this is a mistake, make sure to appeal ban in our discord: " + discordUrl, 0);
            }
        }
    }

    public static void statsMenu(Player player, Player reqPlayer) {
        PlayerData data = new PlayerData(reqPlayer);
        String rank = data.getRank().name();
        String title = "\uE86B Stats";
        long playtime = Long.parseLong(String.valueOf(data.getPlaytime()));
        String description = "[orange]Name: " + reqPlayer.name()
                + "\n[orange]ID: [white]" + data.getId()
                + "\n[orange]Rank: " + rank
                + "\n[orange]Achievements: [white]" + data.getAchievements().toString()
                + "\n\n[orange]Playtime: [white]" + Bundle.formatDuration(Duration.ofMinutes(playtime));
        String button = "[red]Close";
        Call.menu(player.con, MenuHandler.statsMenu, title, description, new String[][]{{button}});
    }

    public static String readValueFromArraySeparated(String[] array, int startPoint, int endPoint) {
        String[] newArray = Arrays.copyOfRange(array, startPoint, endPoint);
        StringBuilder ha = new StringBuilder();
        for (String element : newArray) {
            ha.append(element).append(" ");
        }
        String a = String.valueOf(ha);
        return a.substring(0, a.length() - 1);
    }

    public static <T> boolean notInBounds(T[] array, int index) {
        return (index < 0) || (index >= array.length);
    }

    public static void reloadMaps() {
        ServerControl.instance.handleCommandString("reloadmaps");
        Log.info("Maps has been reloaded!");
    }
}
