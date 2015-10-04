package com.choonster.testmod3.client.model;

import com.choonster.testmod3.init.ModBlocks;
import com.choonster.testmod3.init.ModFluids;
import com.choonster.testmod3.init.ModItems;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;


public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	private static final String FLUID_MODEL_PATH = Constants.RESOURCE_PREFIX + "fluid";

	private ModModelManager() {
	}

	public void registerAllModels() {
		registerFluidModels();
		registerBucketModels();

		registerBlockModels();

		registerItemVariants();
		registerItemModels();
	}

	private void registerFluidModels() {
		for (IFluidBlock fluidBlock : ModFluids.fluidBlocks) {
			registerFluidModel(fluidBlock);
		}
	}

	private void registerFluidModel(IFluidBlock fluidBlock) {
		Item item = Item.getItemFromBlock((Block) fluidBlock);

		ModelBakery.addVariantName(item);

		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	private void registerBucketModels() {
		for (FluidStack fluidStack : ModItems.bucket.fluids) {
			ModelBakery.addVariantName(ModItems.bucket, "testmod3:bucket/" + fluidStack.getFluid().getName());
		}

		ModelLoader.setCustomMeshDefinition(ModItems.bucket, MeshDefinitionFix.create(stack ->
						new ModelResourceLocation("testmod3:bucket/" + ModItems.bucket.getFluid(stack).getFluid().getName(), "inventory")
		));
	}

	private void registerBlockModels() {
		ModelLoader.setCustomStateMapper(ModBlocks.waterGrass, new StateMap.Builder().addPropertiesToIgnore(BlockLiquid.LEVEL).build());
		registerBlockItemModel(ModBlocks.waterGrass, "tall_grass");

		registerBlockItemModel(ModBlocks.largeCollisionTest, "white_wool");
		registerBlockItemModel(ModBlocks.rightClickTest, "black_stained_glass");
		registerBlockItemModel(ModBlocks.clientPlayerRightClick, "heavy_weighted_pressure_plate");
		registerBlockItemModel(ModBlocks.rotatableLamp);
		registerBlockItemModel(ModBlocks.itemCollisionTest);
	}

	private void registerBlockItemModel(Block block) {
		registerItemModel(Item.getItemFromBlock(block));
	}

	private void registerBlockItemModel(Block block, String modelLocation) {
		registerItemModel(Item.getItemFromBlock(block), modelLocation);
	}

	private void registerItemVariants() {
		for (int stage = 0; stage < 3; stage++) { // Add a variant for each stage's model
			ModelBakery.addVariantName(ModItems.modelTest, "testmod3:modeltest_" + stage);
		}

		ModelBakery.addVariantName(ModItems.slingshot, "testmod3:slingshot_pulled");
	}

	private void registerItemModels() {
		registerItemModel(ModItems.entityInteractionTest);
		registerItemModel(ModItems.solarisRecord);
		registerItemModel(ModItems.woodenAxe);
		registerItemModel(ModItems.modelTest); // Only use the default model, the stages are handled by ItemModelTest#getModel
		registerItemModel(ModItems.snowballLauncher, "minecraft:fishing_rod");
		registerItemModel(ModItems.slingshot);
		registerItemModel(ModItems.unicodeTooltips, "minecraft:rabbit");
		registerItemModel(ModItems.swapTestA, "minecraft:brick");
		registerItemModel(ModItems.swapTestB, "minecraft:netherbrick");
	}

	private void registerItemModel(Item item) {
		registerItemModel(item, Item.itemRegistry.getNameForObject(item).toString());
	}

	private void registerItemModel(Item item, String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		ModelBakery.addVariantName(item, modelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> fullModelLocation));
	}
}
