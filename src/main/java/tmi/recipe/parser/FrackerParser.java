package tmi.recipe.parser;

import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.production.Fracker;
import tmi.recipe.Recipe;
import tmi.recipe.RecipeType;

public class FrackerParser extends ConsumerParser<Fracker>{
  {
    excludes.add(PumpParser.class);
    excludes.add(SolidPumpParser.class);
  }

  @Override
  public boolean isTarget(Block content) {
    return content instanceof Fracker;
  }

  @Override
  public Seq<Recipe> parse(Fracker fracker) {
    Recipe res = new Recipe(RecipeType.collecting);
    res.setBlock(getWrap(fracker));
    res.setTime(fracker.consumeTime);
    res.addProduction(getWrap(fracker.result));

    registerCons(res, fracker.consumers);

    for (Block block : Vars.content.blocks()) {
      if (block.attributes.get(fracker.attribute) <= 0 || (block instanceof Floor f && f.isDeep())) continue;

      float eff = block.attributes.get(fracker.attribute);
      res.addMaterial(getWrap(block), fracker.size*fracker.size)
          .setOptionalCons(fracker.baseEfficiency > 0.001f)
          .setEfficiency(eff)
          .setAttribute()
          .setFormat(f -> "[#98ffa9]" + (fracker.baseEfficiency > 0.001f? "+": "") + Mathf.round(eff*100) + "%");
    }

    res.efficiency = Recipe.getDefaultEff(fracker.baseEfficiency);

    return Seq.with(res);
  }
}
