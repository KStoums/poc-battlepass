package fr.kstars.battlepass.reward;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;

public class JsonRewardFileLoader {
    public File loadJsonRewardFile() throws IOException {
        File jsonDataFile = new File("./plugins/battlepass/data/rewards.json");
        File parentFile = jsonDataFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        if (!jsonDataFile.exists()) {
            ObjectMapper mapper = new ObjectMapper();

            Reward[] exempleRewards = new Reward[] {
                    new Reward("§cExample", "§cExample", "give %player% minecraft:diamond 1", Material.CHEST_MINECART,1, false)
            };

            mapper.writeValue(jsonDataFile, exempleRewards);
        }

        return jsonDataFile;
    }

    public Reward[] getJsonData(File jsonFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonFile, Reward[].class);
    }
}
