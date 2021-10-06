package uk.gemwire.voter;


import org.kohsuke.github.GHApp;
import org.kohsuke.github.GHAppInstallation;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import uk.gemwire.voter.auth.JWTGen;
import uk.gemwire.voter.config.Config;

import java.util.logging.Logger;

/**
 * The entry point for the Voter bot.
 *
 * The Voter bot itself allows the management of a repository (such as text-based documentation) to be placed into the hands
 * of the community.
 *
 * If a Pull Request gathers more than a (configurable) majority of positive votes, it will be merged automatically.
 * If a Pull Request gathers more than a majority of negative votes, it will be closed.
 *
 * If a Pull Request reaches its full term without a majority of votes, it will be assigned to maintainers to make the final decision.
 *
 * All in all, a simple bot.
 */
public class BotMain {

    /**
     * Bot starting point.
     * Logs in, and hands off to the specific thread for the specified organization / repository.
     * @param args
     */
    public static void main(String[] args) {
        try {
            Config.readConfigs();
            GitHub login = new GitHubBuilder().withJwtToken(JWTGen.createJWT("143063", 0)).build();
            GHApp app = login.getApp();
            Logger.getAnonymousLogger().info("Github App " + app.getName() + " by " + app.getOwner().getName() + " loaded. Finding installation..");


        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}