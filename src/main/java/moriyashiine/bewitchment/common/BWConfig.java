/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.bewitchment.common;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.ArrayList;
import java.util.List;

public class BWConfig extends MidnightConfig {
	@Entry
	public static List<String> disabledPoppets = new ArrayList<>();
	@Entry
	public static boolean enableCurses = true;

	@Entry
	public static int altarDistributionRadius = 24;

	@Entry
	public static boolean generateSalt = true;
	@Entry
	public static boolean generateSilver = true;

	@Entry
	public static int owlWeight = 10;
	@Entry
	public static int owlMinGroupCount = 1;
	@Entry
	public static int owlMaxGroupCount = 2;

	@Entry
	public static int ravenWeight = 10;
	@Entry
	public static int ravenMinGroupCount = 1;
	@Entry
	public static int ravenMaxGroupCount = 3;

	@Entry
	public static int snakeWeight = 6;
	@Entry
	public static int snakeMinGroupCount = 1;
	@Entry
	public static int snakeMaxGroupCount = 2;

	@Entry
	public static int toadWeight = 10;
	@Entry
	public static int toadMinGroupCount = 1;
	@Entry
	public static int toadMaxGroupCount = 3;

	@Entry
	public static int ghostWeight = 20;
	@Entry
	public static int ghostMinGroupCount = 1;
	@Entry
	public static int ghostMaxGroupCount = 1;

	@Entry
	public static int vampireWeight = 20;
	@Entry
	public static int vampireMinGroupCount = 1;
	@Entry
	public static int vampireMaxGroupCount = 1;

	@Entry
	public static int werewolfWeight = 20;
	@Entry
	public static int werewolfMinGroupCount = 1;
	@Entry
	public static int werewolfMaxGroupCount = 1;

	@Entry
	public static int hellhoundWeight = 6;
	@Entry
	public static int hellhoundMinGroupCount = 1;
	@Entry
	public static int hellhoundMaxGroupCount = 1;
}
