package org.kayteam.api.simple.yaml;

import org.kayteam.api.KayTeamAPI;

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
        api.getLogger().info(" -- END GET TEST 1 --");
        SimpleYaml prueba2 = new SimpleYaml(api, "prueba2");
        prueba2.registerYamlFile();
        api.getLogger().info(" -- GET TEST 2 --");
        api.getLogger().info("STRING GET "+prueba2.getString("string"));
        api.getLogger().info("INT GET "+prueba2.getInt("integer"));
        api.getLogger().info("DOUBLE GET "+prueba2.getDouble("double"));
        api.getLogger().info("LIST GET "+prueba2.getList("stringList"));
        api.getLogger().info("SUB-PATH STRING GET "+prueba2.getString("subPath.string"));
        api.getLogger().info("SUB-PATH INT GET "+prueba2.getInt("subPath.integer"));
        api.getLogger().info("SUB-PATH DOUBLE GET "+prueba2.getDouble("subPath.double"));
        api.getLogger().info("SUB-PATH LIST GET "+prueba2.getList("subPath.stringList"));
        api.getLogger().info(" -- END GET TEST 2 --");
    }
}
