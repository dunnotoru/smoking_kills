package dunno.smoking_kills;

import dunno.smoking_kills.data.SmokingData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class StateSaverAndLoader extends PersistentState {
    public HashMap<UUID, SmokingData> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();

            playerNbt.putInt(NbtKeys.CIGARETTES_SMOKED, playerData.cigarettesSmoked);

            playersNbt.put(uuid.toString(), playerNbt);
        });
        nbt.put(NbtKeys.PLAYERS, playersNbt);

        return nbt;
    }

    public static StateSaverAndLoader createFromNbt(NbtCompound tag) {
        StateSaverAndLoader state = new StateSaverAndLoader();

        NbtCompound playersNbt = tag.getCompound(NbtKeys.PLAYERS);
        playersNbt.getKeys().forEach(key -> {
            SmokingData playerData = new SmokingData();

            playerData.cigarettesSmoked = playersNbt.getCompound(key).getInt(NbtKeys.CIGARETTES_SMOKED);

            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });

        return state;
    }

    public static StateSaverAndLoader createNew() {
        return new StateSaverAndLoader();
    }

    public static StateSaverAndLoader getServerState(MinecraftServer server) {
        ServerWorld serverWorld = server.getWorld(World.OVERWORLD);
        assert serverWorld != null;

        StateSaverAndLoader state = serverWorld.getPersistentStateManager().getOrCreate(
                StateSaverAndLoader::createFromNbt,
                StateSaverAndLoader::createNew,
                SmokingKills.MOD_ID
        );

        state.markDirty();

        return state;
    }

    public static SmokingData getPlayerState(LivingEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());

        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new SmokingData());
    }
}
