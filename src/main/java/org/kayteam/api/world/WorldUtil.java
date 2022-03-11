package org.kayteam.api.world;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.*;

public class WorldUtil {

    public static void createWorldTemplate(World world, String templateDir, String templateName) {
        File fromFolder = new File(Bukkit.getWorldContainer() + File.separator + world.getName());
        File toFolder = new File(templateDir + File.separator + templateName);
        toFolder.mkdirs();
        WorldUtil.copyFile(fromFolder, toFolder);
    }

    public static World createWorldCopyFromTemplate(String templateDir, String templateName, String copyName) {
        File templateFolder = new File(templateDir + File.separator + templateName);
        if (templateFolder.exists() && templateFolder.isDirectory()) {
            File copyFolder = new File(Bukkit.getWorldContainer() + File.separator + copyName);
            if (!copyFolder.exists()) {
                copyFolder.mkdirs();
                WorldUtil.copyFile(templateFolder, copyFolder);
                return new WorldCreator(copyName).createWorld();
            }
        }
        return null;
    }

    private static void copyFile(File from, File to) {
        if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdir();
            }
            String files[] = from.list();
            for (String file : files) {
                File fromFile = new File(from, file);
                File toFile = new File(to, file);
                copyFile(fromFile, toFile);
            }
        } else {
            if (!from.getName().equals("uid.dat")) {
                try {
                    InputStream in = new FileInputStream(from);
                    OutputStream out = new FileOutputStream(to);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                    in.close();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}