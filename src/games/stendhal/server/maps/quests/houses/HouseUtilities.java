package games.stendhal.server.maps.quests.houses;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.portal.HousePortal;
import games.stendhal.server.entity.mapstuff.portal.Portal;
import games.stendhal.server.entity.player.Player;

public class HouseUtilities {
	private static final String HOUSE_QUEST_SLOT = "house";
	private static final Logger logger = Logger.getLogger(HouseUtilities.class);
	private static final String[] zoneNames = {
		"0_kalavan_city",
		"0_kirdneh_city",
		"0_ados_city_n",
		"0_ados_city"
	};
	
	private static List<HousePortal> allHousePortals = null;
	
	/**
	 * Get the house owned by a player.
	 * 
	 * @param player the player to be examined
	 * @return portal to the house owned by the player, or <code>null</code>
	 * if he doesn not own one. 
	 */
	public static HousePortal getPlayersHouse(final Player player) {
		if (player.hasQuest(HOUSE_QUEST_SLOT)) {
			final String claimedHouse = player.getQuest(HOUSE_QUEST_SLOT);
		
			try {
				final int id = Integer.parseInt(claimedHouse);
				final HousePortal portal = getHousePortal(id);
				
				if (portal != null) { 
					if (player.getName().equals(portal.getOwner())) {
						return portal;
					}
				} else {
					logger.error("Player " + player.getName() + " claims to own a nonexistent house " + id);
				}
			} catch (final NumberFormatException e) {
				logger.error("Invalid number in house slot", e);
			}
		}
		
		return null;
	}
	
	/**
	 * Check if a player owns a house.
	 * 
	 * @param player the player to be checked
	 * @return <code>true</code> if the player owns a house, false otherwise
	 */
	public static boolean playerOwnsHouse(final Player player) {
		return (getPlayersHouse(player) != null);
	}

	/**
	 * Find a portal corresponding to a house number.
	 * 
	 * @param houseNumber the house number to find
	 * @return the portal to the house, or <code>null</code> if there is no
	 * house by number <code>id</code>
	 */
	public static HousePortal getHousePortal(final int houseNumber) {
		final List<HousePortal> portals =  getHousePortals();
		
		for (final HousePortal houseportal : portals) {
			final int number = houseportal.getPortalNumber();
			if (number == houseNumber) {
				return houseportal;
			}
		}
		// if we got this far, we didn't find a match
		logger.error("getHousePortal was given a number (" + Integer.toString(houseNumber) + ") it couldn't match a house portal for - how did that happen?!");
		return null;
	}
	
	/**
	 * Get a list of all house portals available to players.
	 * 
	 * @return list of all house portals
	 */
	public static List<HousePortal> getHousePortals() {
		if (allHousePortals == null) {
			// this is only done once per server run
			allHousePortals = new LinkedList<HousePortal>();
			
			for (String zoneName : zoneNames) {
				StendhalRPZone zone = SingletonRepository.getRPWorld().getZone(zoneName);
				for (Portal portal : zone.getPortals()) {
					if (portal instanceof HousePortal) {
						allHousePortals.add((HousePortal) portal);
					}
				}
			}

		}
		final int size = allHousePortals.size();
		logger.debug("Number of house portals in world is " + Integer.toString(size));
		
		return allHousePortals;
	}
}
