package earth.terrarium.ad_astra.datagen.provider.server;

import earth.terrarium.ad_astra.common.data.Planet;
import earth.terrarium.ad_astra.datagen.provider.base.PlanetProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModPlanetProvider extends PlanetProvider {
    public ModPlanetProvider(PackOutput output) {
        super(output);
    }

    protected void buildPlanets(Consumer<Planet> consumer) {
        consumer.accept(new Planet(Level.OVERWORLD, true, 9.806f, 15));
        consumer.accept(new Planet(Planet.MOON, false, 1.625f, -160));
        consumer.accept(new Planet(Planet.MARS, false, 3.72076f, -65));
        consumer.accept(new Planet(Planet.VENUS, false, 8.87f, 464));
        consumer.accept(new Planet(Planet.MERCURY, false, 3.7f, 167));
        consumer.accept(new Planet(Planet.GLACIO, true, 3.721f, -46));
    }
}