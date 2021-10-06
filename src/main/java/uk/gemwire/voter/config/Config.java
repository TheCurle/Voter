package uk.gemwire.voter.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Manages global configuration state.
 * All config options are set here as public static fields.
 *
 */
public class Config {
    // The ID of the Installation of this instance of the bot. (The organization this bot was installed to)
    public static int INSTALLATION_ID = 0;
    // The threshold to consider a vote a majority.
    public static int VOTE_THRESHOLD = 2;
    // The minimum number of votes that a PR needs to be considered for automated action.
    public static int VOTE_MINIMUM = 10;

    /**
     * Read configs from file.
     * If the file does not exist, or the properties are invalid, the config is reset to defaults.
     * @throws IOException
     */
    public static void readConfigs() throws Exception {
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.properties"));
            INSTALLATION_ID = Integer.parseInt(props.getProperty("installation"));
            VOTE_THRESHOLD = Integer.parseInt(props.getProperty("threshold"));
            VOTE_MINIMUM = Integer.parseInt(props.getProperty("minimum_votes"));
        } catch (Exception e) {
            Files.writeString(Path.of("config.properties"),
                    """
                            # The Installation ID of this App.
                            installation=0
                            # The number of votes that constitutes a majority.
                            threshold=2
                            # The minimum number of votes required in order to take automated action.
                            minimum_votes=10""");

            Logger.getAnonymousLogger().warning("Configuration file is invalid. Resetting..");
        }
    }

}
