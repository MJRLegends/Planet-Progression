package com.mjr.planetprogression.world;

import java.util.Arrays;
import java.util.Random;

import com.mjr.mjrlegendslib.util.WorldGenUtilities;
import com.mjr.planetprogression.Config;
import com.mjr.planetprogression.world.features.WorldGenStructure;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenerater implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (Arrays.asList(Config.worldgenStructureWorldWhitelist).contains("" + world.provider.getDimensionId()) && random.nextInt(Config.worldgenStructureAmount) == 1) {
			int xPos = chunkX * 16 + 8;
			int zPos = chunkZ * 16 + 8;
			WorldGenUtilities.generateStructure(new WorldGenStructure(), world, random, new BlockPos(xPos, 0, zPos));
		}
	}
}
