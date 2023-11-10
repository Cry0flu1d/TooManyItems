package tmi.recipe.parser;

import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.SolidPump;
import tmi.recipe.Recipe;
import tmi.recipe.RecipeType;

public class SolidPumpParser extends ConsumerParser<SolidPump>{
  {excludes.add(PumpParser.class);}

  @Override
  public boolean isTarget(Block content) {
    return content instanceof SolidPump;
  }

  @Override
  public Seq<Recipe> parse(SolidPump pump) {
    Recipe res = new Recipe(RecipeType.collecting);
    res.setBlock(pump);
    res.addProduction(pump.result);

    registerCons(res, pump.consumers);

    for (Block block : Vars.content.blocks()) {
      if (block.attributes.get(pump.attribute) <= 0 || (block instanceof Floor f && f.isDeep())) continue;

      float eff = block.attributes.get(pump.attribute);
      res.addMaterial(block, pump.size*pump.size)
          .setOptionalCons(pump.baseEfficiency > 0.001f)
          .setEfficiency(pump.baseEfficiency + eff)
          .setAttribute()
          .setFormat(f -> "[#98ffa9]" + (pump.baseEfficiency > 0.001f? "+": "") + Mathf.round(eff*100) + "%");
    }

    res.efficiency = Recipe.getDefaultEff(pump.baseEfficiency);

    return Seq.with(res);
  }
}
