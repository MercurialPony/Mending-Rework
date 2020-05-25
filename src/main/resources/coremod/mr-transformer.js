function initializeCoreMod()
{
	// Imports
	var Opcodes = Java.type("org.objectweb.asm.Opcodes");
	var ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");
	var AbstractInsnNode = Java.type("org.objectweb.asm.tree.AbstractInsnNode");
	var FieldNode = Java.type("org.objectweb.asm.tree.FieldNode");
	var LabelNode = Java.type("org.objectweb.asm.tree.LabelNode");
	var IntInsnNode = Java.type("org.objectweb.asm.tree.IntInsnNode");
	var FieldInsnNode = Java.type("org.objectweb.asm.tree.FieldInsnNode");
	var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
	var JumpInsnNode = Java.type("org.objectweb.asm.tree.JumpInsnNode");
	var TypeInsnNode = Java.type("org.objectweb.asm.tree.TypeInsnNode");
	return {
		"EnchantmentHelper#getEnchantmentDatas":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.enchantment.EnchantmentHelper",
				methodName: "func_185291_a",
				methodDesc: "(ILnet/minecraft/item/ItemStack;Z)Ljava/util/List;"
			},
			transformer: function(node)
			{
				var target;
				var counter = 0;
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.ALOAD && instruction.var == 7 && !target) target = instruction;
					if(instruction.getType() === AbstractInsnNode.LABEL)
					{
						++counter;
						if(counter === 16)
						{
							node.instructions.insertBefore(target, new IntInsnNode(Opcodes.ALOAD, 7));
							node.instructions.insertBefore(target, new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "isValidEnchantment", "(Lnet/minecraft/enchantment/Enchantment;)Z"));
							node.instructions.insertBefore(target, new JumpInsnNode(Opcodes.IFEQ, new LabelNode(instruction.label)));
							break;
						}
					}
				}
				return node;
			}
		},
		"EnchantedBookForEmeraldsTrade#getOffer":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.entity.merchant.villager.VillagerTrades$EnchantedBookForEmeraldsTrade",
				methodName: "func_221182_a",
				methodDesc: "(Lnet/minecraft/entity/Entity;Ljava/util/Random;)Lnet/minecraft/item/MerchantOffer;"
			},
			transformer: function(node)
			{
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.GETSTATIC) iterator.remove();
					else if(instruction.getOpcode() === Opcodes.INVOKEVIRTUAL) iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getRandomEnchantment", "(Ljava/util/Random;)Lnet/minecraft/enchantment/Enchantment;"));
					else if(instruction.getOpcode() === Opcodes.CHECKCAST)
					{
						iterator.remove();
						break;
					}
				}
				return node;
			}
		},
		"EnchantRandomly#doApply":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.world.storage.loot.functions.EnchantRandomly",
				methodName: "func_215859_a",
				methodDesc: "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/storage/loot/LootContext;)Lnet/minecraft/item/ItemStack;"
			},
			transformer: function(node)
			{
				var target;
				var counter = 0;
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.ALOAD && instruction.var === 1 && !target) target = instruction;
					if(instruction.getType() === AbstractInsnNode.LABEL)
					{
						++counter;
						if(counter === 8)
						{
							node.instructions.insertBefore(target, new IntInsnNode(Opcodes.ALOAD, 7));
							node.instructions.insertBefore(target, new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "isValidEnchantment", "(Lnet/minecraft/enchantment/Enchantment;)Z"));
							node.instructions.insertBefore(target, new JumpInsnNode(Opcodes.IFEQ, new LabelNode(instruction.label)));
							break;
						}
					}
				}
				return node;
			}
		},
		/*
		"ExperienceOrbEntity#onCollideWithPlayer":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.entity.item.ExperienceOrbEntity",
				methodName: "func_70100_b_",
				methodDesc: "(Lnet/minecraft/entity/player/PlayerEntity;)V"
			},
			transformer: function(node)
			{
				var mending = ASMAPI.mapField("field_185296_A");
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.GETSTATIC && instruction.name === mending)
					{
						iterator.set(new FieldInsnNode(Opcodes.GETSTATIC, "melonslise/mendingrework/common/init/MREnchantments", "RENEWAL", "Lnet/minecraft/enchantment/Enchantment;"));
						break;
					}
				}
				return node;
			}
		},
		*/
		"RepairContainer#updateRepairOutput":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.inventory.container.RepairContainer",
				methodName: "func_82848_d",
				methodDesc: "()V"
			},
			transformer: function(node)
			{
				var materialCost = ASMAPI.mapField("field_82856_l");
				var isCompatibleWith = ASMAPI.mapMethod("func_191560_c");
				var set = ASMAPI.mapMethod("func_221494_a");
				var getNewRepairCost = ASMAPI.mapMethod("func_216977_d");
				var counter = 0;
				var counter1 = 0;
				var counter2 = 0;
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.PUTFIELD && instruction.name === materialCost)
					{
						++counter1;
						if(counter1 === 2)
						{
							node.instructions.insert(instruction, new IntInsnNode(Opcodes.ASTORE, 5));
							node.instructions.insert(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getRepairOutput", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"));
							node.instructions.insert(instruction, new IntInsnNode(Opcodes.ALOAD, 5));
							node.instructions.insert(instruction, new IntInsnNode(Opcodes.ALOAD, 1));
						}
					}
					if(instruction.getOpcode() === Opcodes.INVOKEVIRTUAL && instruction.name === isCompatibleWith)
					{
						node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 6));
						node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 1));
						iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "canApplyWith", "(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"));
					}
					if(instruction.getOpcode() === Opcodes.INVOKEVIRTUAL && instruction.name === set)
					{
						++counter;
						if(counter === 6)
						{
							node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 1));
							node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 6));
							node.instructions.insertBefore(instruction, new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getNewRepairCost", "(ILnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)I"));
						}
					}
					if(instruction.getOpcode() === Opcodes.BIPUSH && instruction.operand === 40)
					{
						++counter2;
						if(counter2 === 3)
						{
							node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 0));
							iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getMaxRepairCost", "(Lnet/minecraft/inventory/container/RepairContainer;)I"));
						}
					}
					if(instruction.getOpcode() === Opcodes.INVOKESTATIC && instruction.name === getNewRepairCost)
					{
						node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 1));
						node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 6));
						iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getNewRepairPenalty", "(ILnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)I"));
						break;
					}
				}
				return node;
			}
		},
		"AnvilScreen#drawGuiContainerForegroundLayer":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.client.gui.screen.inventory.AnvilScreen",
				methodName: "func_146979_b",
				methodDesc: "(II)V"
			},
			transformer: function(node)
			{
				var container = ASMAPI.mapField("field_147002_h");
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() !== Opcodes.BIPUSH || instruction.operand !== 40)
						continue;
					node.instructions.insertBefore(instruction, new IntInsnNode(Opcodes.ALOAD, 0));
					node.instructions.insertBefore(instruction, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/screen/inventory/AnvilScreen", container, "Lnet/minecraft/inventory/container/Container;"));
					node.instructions.insertBefore(instruction, new TypeInsnNode(Opcodes.CHECKCAST, "net/minecraft/inventory/container/RepairContainer"));
					iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getMaxRepairCost", "(Lnet/minecraft/inventory/container/RepairContainer;)I"));
					break;
				}
				return node;
			}
		},
		"SkullTileEntity":
		{
			target:
			{
				type: "CLASS",
				name: "net.minecraft.tileentity.SkullTileEntity"
			},
			transformer: function(node)
			{
				node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "mendingReworkAnimate", "Z", null, null));
				return node;
			}
		},
		"SkullTileEntity#tick":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.tileentity.SkullTileEntity",
				methodName: "tick",
				methodDesc: "()V"
			},
			transformer: function(node)
			{
				var target;
				var counter = 0;
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.ALOAD && instruction.var === 1 && !target) target = instruction;
					if(instruction.getType() === AbstractInsnNode.LABEL)
					{
						++counter;
						if(counter === 4)
						{
							node.instructions.insertBefore(target, new IntInsnNode(Opcodes.ALOAD, 0));
							node.instructions.insertBefore(target, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/SkullTileEntity", "mendingReworkAnimate", "Z"));
							node.instructions.insertBefore(target, new JumpInsnNode(Opcodes.IFNE, new LabelNode(instruction.label)));
							break;
						}
					}
				}
				return node;
			}
		},
		/*
		"ItemStack#getAttributeModifiers":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.item.ItemStack",
				methodName: "func_111283_C",
				methodDesc: "(Lnet/minecraft/inventory/EquipmentSlotType;)Lcom/google/common/collect/Multimap;"
			},
			transformer: function(node)
			{
				var getItem = ASMAPI.mapMethod("func_77973_b");
				var getAttributeModifiers = ASMAPI.mapMethod("func_111205_h");
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() === Opcodes.INVOKEVIRTUAL && instruction.name === getItem)
					{
						iterator.remove();
						iterator.previous();
						iterator.remove();
					}
					if(instruction.getOpcode() === Opcodes.INVOKEVIRTUAL && instruction.name === getAttributeModifiers)
					{
						iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "getAttributeModifiers", "(Lnet/minecraft/inventory/EquipmentSlotType;Lnet/minecraft/item/ItemStack;)Lcom/google/common/collect/Multimap;"));
						break;
					}
				}
				return node;
			}
		},
		*/
		"ItemStack#setDamage":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.item.ItemStack",
				methodName: "func_196085_b",
				methodDesc: "(I)V"
			},
			transformer: function(node)
			{
				node.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "onItemDamage", "(Lnet/minecraft/item/ItemStack;I)V"));
				node.instructions.insert(new IntInsnNode(Opcodes.ILOAD, 1));
				node.instructions.insert(new IntInsnNode(Opcodes.ALOAD, 0));
				return node;
			}
		},
		"InfinityEnchantment#canApplyTogether":
		{
			target:
			{
				type: "METHOD",
				class: "net.minecraft.enchantment.InfinityEnchantment",
				methodName: "func_77326_a",
				methodDesc: "(Lnet/minecraft/enchantment/Enchantment;)Z"
			},
			transformer: function(node)
			{
				for(var iterator = node.instructions.iterator(); iterator.hasNext();)
				{
					var instruction = iterator.next();
					if(instruction.getOpcode() !== Opcodes.INSTANCEOF)
						continue;
					iterator.set(new MethodInsnNode(Opcodes.INVOKESTATIC, "melonslise/mendingrework/coremod/MRDelegates", "cannotApplyInfinity", "(Lnet/minecraft/enchantment/Enchantment;)Z"));
					break;
				}
				return node;
			}
		}
	};
}
