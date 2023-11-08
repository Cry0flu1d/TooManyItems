package tmi.recipe.parser;

import arc.struct.ObjectSet;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.Drill;
import tmi.recipe.Recipe;
import tmi.recipe.RecipeType;

import static tmi.util.Consts.markerTile;

public class DrillParser extends ConsumerParser<Drill>{
  protected ObjectSet<Floor> itemDrops = new ObjectSet<>();

  @Override
  public void init() {
    for (Block block : Vars.content.blocks()) {
      if (block instanceof Floor f && f.itemDrop != null && !f.wallOre) itemDrops.add(f);
    }
  }

  @Override
  public boolean isTarget(Block content) {
    return content instanceof Drill;
  }

  @Override
  public Seq<Recipe> parse(Drill content) {
    Seq<Recipe> res = new Seq<>();

    for (Floor drop : itemDrops) {
      if (drop instanceof OreBlock) markerTile.setOverlay(drop);
      else markerTile.setFloor(drop);

      if (!content.canMine(markerTile)) continue;

      Recipe recipe = new Recipe(RecipeType.collecting);
      recipe.block = content;
      recipe.addMaterial(drop);
      recipe.addProduction(drop.itemDrop);

      registerCons(recipe, content.nonOptionalConsumers);

      res.add(recipe);
    }

    return res;
  }
}
