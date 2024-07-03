package com.takutou.pl_messagesevent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class Pl_MessagesEvent extends JavaPlugin {

    //変数
    private String message ;
    private long interval; //default時間間隔

    @Override
    public void onEnable() {

        // Plugin startup logic
        getLogger().info("Hello World!");
        getLogger().info("コミット共有テスト");
        this.getCommand("kit").setExecutor(new CommandKit());
        loadMessage();
        //　イベントリスナー
        getServer().getPluginManager().registerEvents(new MyListener(),this);

        //コマンドハンドラー
        /*アナウンスセット*/
        this.getCommand("setannounce").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
                if (args.length == 0) {
                    commandSender.sendMessage("メッセージを入力してください。");
                    return false;
                }
                // 入力されたメッセージを結合して保存
                message = String.join(" ", args);
                commandSender.sendMessage("アナウンスのメッセージが設定されました: " + message);
                //設定ファイル保存
                saveMessage();
                return true;
            }
        });
        /*アナウンス間隔セット*/
        this.getCommand("setannouncesec").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if(strings.length != 1){
                    commandSender.sendMessage("時間間隔を秒単位で指定してください。");
                    return false;
                }
                try{
                    interval = Long.parseLong(strings[0])*20;
                    commandSender.sendMessage("アナウンスの間隔を設定しました"+strings[0]+"秒");
                    scheduleAnnouncerTask();
                    saveMessage();
                    return true;
                }catch (NumberFormatException error) {
                    commandSender.sendMessage("有効な数値を入力してください");
                    return false;
                }
            }
        });
        /*get dog*/
        this.getCommand("getdog").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                String url = "https://dog.ceo/api/breeds/image/random";
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .GET()
                        .build();
                try {
                    HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());


                    String requestBody = res.body();
                    //Jackson で　Json parse
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode jsonNode = mapper.readTree(requestBody);
                    //JSONデータの指定したフィールドの値を取得
                    String status = jsonNode.get("message").asText();
                    commandSender.sendMessage(status);
                    return true;
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                    return false;
                }

            }
        });
        scheduleAnnouncerTask();
    }

    //定期処理
    private void scheduleAnnouncerTask(){
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.DARK_AQUA+"【定期】:\n"+ message + "\n---------------");
            }
        },0L,interval);
    }
    //設定よみこみ
    public void loadMessage(){
        FileConfiguration config = getConfig();
        message = config.getString("announcement-message","デフォルトのメッセージ");
        interval = config.getLong("announcement-interval", 300); // デフォルトは5分
    }
    //設定を保存
    public void saveMessage(){
        FileConfiguration config = getConfig();
        config.set("announcement-message",message);
        config.set("announcement-interval", interval);
        saveConfig();;
    }
    //webAPIをコマンドでたたく。
    public void getDogURL(){

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
