package org.kayteam.api.simple.yaml;

import org.kayteam.api.KayTeamAPI;

import java.util.Arrays;
import java.util.Random;

public class SimpleYamlTest {

    public SimpleYamlTest(KayTeamAPI api) {
        SimpleYaml prueba = new SimpleYaml(api, "prueba");
        prueba.registerYamlFile();
        api.getLogger().info(" -- GET TEST 1 --");
        api.getLogger().info("STRING GET "+prueba.getString("string"));
        api.getLogger().info("INT GET "+prueba.getInt("integer"));
        api.getLogger().info("DOUBLE GET "+prueba.getDouble("double"));
        api.getLogger().info("LIST GET "+prueba.getList("stringList"));
        api.getLogger().info("SUB-PATH STRING GET "+prueba.getString("subPath.string"));
        api.getLogger().info("SUB-PATH INT GET "+prueba.getInt("subPath.integer"));
        api.getLogger().info("SUB-PATH DOUBLE GET "+prueba.getDouble("subPath.double"));
        api.getLogger().info("SUB-PATH LIST GET "+prueba.getList("subPath.stringList"));
        api.getLogger().info("STRING GET "+prueba.getString("string"));
        api.getLogger().info("INT GET "+prueba.getInt("integer"));
        api.getLogger().info("DOUBLE GET "+prueba.getDouble("double"));
        api.getLogger().info("LIST GET "+prueba.getList("stringList"));
        api.getLogger().info("SUB-PATH STRING GET "+prueba.getString("subPath.string"));
        api.getLogger().info("SUB-PATH INT GET "+prueba.getInt("subPath.integer"));
        api.getLogger().info("SUB-PATH DOUBLE GET "+prueba.getDouble("subPath.double"));
        api.getLogger().info("SUB-PATH LIST GET "+prueba.getList("subPath.stringList"));
        api.getLogger().info(" -- END GET TEST 1 --");
        api.getLogger().info(" -- SET TEST 1 --");
        String string = generateRandomWords(1)[0];
        int integer = new Random().nextInt(100);
        double doubleValue = new Random().nextDouble();
        String[] stringList = generateRandomWords(5);
        prueba.set("string2", string);
        prueba.set("integer2", integer);
        prueba.set("double2", doubleValue);
        prueba.set("stringList2", Arrays.asList(stringList));
        prueba.set("subPath.string2", string);
        prueba.set("subPath.integer2", integer);
        prueba.set("subPath.double2", doubleValue);
        prueba.set("subPath.stringList2", Arrays.asList(stringList));
        api.getLogger().info("STRING SET "+prueba.getString("string2"));
        api.getLogger().info("INT SET "+prueba.getInt("integer2"));
        api.getLogger().info("DOUBLE SET "+prueba.getDouble("double2"));
        api.getLogger().info("LIST SET "+prueba.getList("stringList2"));
        api.getLogger().info("SUB-PATH STRING SET "+prueba.getString("subPath.string2"));
        api.getLogger().info("SUB-PATH INT SET "+prueba.getInt("subPath.integer2"));
        api.getLogger().info("SUB-PATH DOUBLE SET "+prueba.getDouble("subPath.double2"));
        api.getLogger().info("SUB-PATH LIST SET "+prueba.getList("subPath.stringList2"));
        prueba.saveYamlFile();
    }

    public static String[] generateRandomWords(int numberOfWords){
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for(int i = 0; i < numberOfWords; i++)
        {
            char[] word = new char[random.nextInt(8)+3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for(int j = 0; j < word.length; j++)
            {
                word[j] = (char)('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        return randomStrings;
    }
}
