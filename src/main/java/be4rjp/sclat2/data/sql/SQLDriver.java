package be4rjp.sclat2.data.sql;

import be4rjp.sclat2.SclatConfig;
import be4rjp.sclat2.data.AchievementData;
import be4rjp.sclat2.language.Lang;
import be4rjp.sclat2.player.costume.HeadGearData;
import be4rjp.sclat2.weapon.WeaponClass;

public class SQLDriver {
    
    public static void createTable(SQLConnection sqlConnection) throws Exception{
        sqlConnection.execute("CREATE TABLE IF NOT EXISTS " + SclatConfig.getMySQLConfig().table + " (uuid VARCHAR(36), lang TINYINT, kills INT, paints INT, ranks INT, coin INT, weapon VARBINARY(32), gear VARBINARY(2565), equip TINYINT UNSIGNED, head SMALLINT);");
    }
    
    public static void loadAchievementData(AchievementData achievementData) throws Exception{
        SclatConfig.MySQLConfig mySQLConfig = SclatConfig.getMySQLConfig();
        String uuid = achievementData.getSclatPlayer().getUUID();
        String notExistExecute = "INSERT INTO " + mySQLConfig.table + "(uuid, lang, kills, paints, ranks, coin, weapon, gear, equip, head) VALUES('" + uuid + "', 0, 0, 0, 0, 0, '" + new String(new byte[32]) + "', '" + new String(new byte[2564]) + "', 0, 1);";
        
        SQLConnection sqlConnection = new SQLConnection(mySQLConfig.ip, mySQLConfig.port, mySQLConfig.database, mySQLConfig.username, mySQLConfig.password);
        createTable(sqlConnection);
        Lang lang = Lang.getLangByID(sqlConnection.getByte(mySQLConfig.table, "lang", "uuid", uuid, notExistExecute));
        int kill = sqlConnection.getInt(mySQLConfig.table, "kills", "uuid", uuid, notExistExecute);
        int paint = sqlConnection.getInt(mySQLConfig.table, "paints", "uuid", uuid, notExistExecute);
        int rank = sqlConnection.getInt(mySQLConfig.table, "ranks", "uuid", uuid, notExistExecute);
        int coin = sqlConnection.getInt(mySQLConfig.table, "coin", "uuid", uuid, notExistExecute);
        byte[] weapon = sqlConnection.getByteArray(mySQLConfig.table, "weapon", "uuid", uuid, notExistExecute);
        byte[] gear = sqlConnection.getByteArray(mySQLConfig.table, "gear", "uuid", uuid, notExistExecute);
        int equip = sqlConnection.getInt(mySQLConfig.table, "equip", "uuid", uuid, notExistExecute);
        int head = sqlConnection.getInt(mySQLConfig.table, "head", "uuid", uuid, notExistExecute);
        achievementData.getSclatPlayer().setLang(lang);
        achievementData.setKill(kill);
        achievementData.setPaint(paint);
        achievementData.setRank(rank);
        achievementData.setCoin(coin);
        achievementData.getWeaponPossessionData().load_from_byte_array(weapon);
        achievementData.getHeadGearPossessionData().load_from_byte_array(gear);
        achievementData.getSclatPlayer().setWeaponClass(WeaponClass.getWeaponClassBySaveNumber(equip));
        achievementData.getSclatPlayer().equipWeaponClass();
        
        HeadGearData headGearData = achievementData.getHeadGearPossessionData().getHeadGearData(head);
        if(headGearData != null) achievementData.getSclatPlayer().setHeadGearData(headGearData, head);
        achievementData.getSclatPlayer().equipHeadGear();
        
        sqlConnection.close();
    }
    
    
    public static void saveAchievementData(AchievementData achievementData) throws Exception{
        SclatConfig.MySQLConfig mySQLConfig = SclatConfig.getMySQLConfig();
        String uuid = achievementData.getSclatPlayer().getUUID();
    
        SQLConnection sqlConnection = new SQLConnection(mySQLConfig.ip, mySQLConfig.port, mySQLConfig.database, mySQLConfig.username, mySQLConfig.password);
        createTable(sqlConnection);
    
        sqlConnection.updateValue(mySQLConfig.table, "lang = " + achievementData.getSclatPlayer().getLang().getSaveNumber(), "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "kills = " + achievementData.getKill(), "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "paints = " + achievementData.getPaint(), "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "ranks = " + achievementData.getRank(), "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "coin = " + achievementData.getCoin(), "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "weapon = " + "'" + new String(achievementData.getWeaponPossessionData().write_to_byte_array()) + "'", "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "gear = " + "'" + new String(achievementData.getHeadGearPossessionData().write_to_byte_array()) + "'", "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "equip = " + achievementData.getSclatPlayer().getWeaponClassNumber(), "uuid = '" + uuid + "'");
        sqlConnection.updateValue(mySQLConfig.table, "head = " + achievementData.getSclatPlayer().getHeadGearNumber(), "uuid = '" + uuid + "'");
        
        sqlConnection.close();
    }
}
