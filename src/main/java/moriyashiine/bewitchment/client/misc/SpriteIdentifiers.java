package moriyashiine.bewitchment.client.misc;

import moriyashiine.bewitchment.common.Bewitchment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;

import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;
import static net.minecraft.client.texture.SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;

@Environment(EnvType.CLIENT)
public class SpriteIdentifiers {
	public static final SpriteIdentifier JUNIPER = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/juniper"));
	public static final SpriteIdentifier TRAPPED_JUNIPER = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_juniper"));
	public static final SpriteIdentifier JUNIPER_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/juniper_left"));
	public static final SpriteIdentifier TRAPPED_JUNIPER_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_juniper_left"));
	public static final SpriteIdentifier JUNIPER_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/juniper_right"));
	public static final SpriteIdentifier TRAPPED_JUNIPER_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_juniper_right"));
	
	public static final SpriteIdentifier CYPRESS = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/cypress"));
	public static final SpriteIdentifier TRAPPED_CYPRESS = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_cypress"));
	public static final SpriteIdentifier CYPRESS_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/cypress_left"));
	public static final SpriteIdentifier TRAPPED_CYPRESS_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_cypress_left"));
	public static final SpriteIdentifier CYPRESS_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/cypress_right"));
	public static final SpriteIdentifier TRAPPED_CYPRESS_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_cypress_right"));
	
	public static final SpriteIdentifier ELDER = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/elder"));
	public static final SpriteIdentifier TRAPPED_ELDER = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_elder"));
	public static final SpriteIdentifier ELDER_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/elder_left"));
	public static final SpriteIdentifier TRAPPED_ELDER_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_elder_left"));
	public static final SpriteIdentifier ELDER_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/elder_right"));
	public static final SpriteIdentifier TRAPPED_ELDER_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_elder_right"));
	
	public static final SpriteIdentifier DRAGONS_BLOOD = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/dragons_blood"));
	public static final SpriteIdentifier TRAPPED_DRAGONS_BLOOD = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_dragons_blood"));
	public static final SpriteIdentifier DRAGONS_BLOOD_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/dragons_blood_left"));
	public static final SpriteIdentifier TRAPPED_DRAGONS_BLOOD_LEFT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_dragons_blood_left"));
	public static final SpriteIdentifier DRAGONS_BLOOD_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/dragons_blood_right"));
	public static final SpriteIdentifier TRAPPED_DRAGONS_BLOOD_RIGHT = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(Bewitchment.MODID, "entity/chest/trapped_dragons_blood_right"));
	
	public static final SpriteIdentifier CAULDRON_WATER = new SpriteIdentifier(BLOCK_ATLAS_TEXTURE, new Identifier("block/water_still"));
}
