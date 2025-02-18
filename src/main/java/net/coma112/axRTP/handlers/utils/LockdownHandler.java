package net.coma112.axrtp.handlers.utils;

import lombok.RequiredArgsConstructor;
import net.coma112.axrtp.data.PlayerRTPState;
import net.coma112.axrtp.identifiers.keys.MessageKeys;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@RequiredArgsConstructor
public class LockdownHandler {
    private final Map<String, PlayerRTPState> playerStateMap;
    private final int lockAfterLimit;

    public boolean canUseRTP(@NotNull Player player) {
        PlayerRTPState state = playerStateMap.computeIfAbsent(player.getName(), k -> new PlayerRTPState());

        if (lockAfterLimit > 0 && state.getUsageCount() >= lockAfterLimit) {
            player.sendMessage(MessageKeys.LOCKDOWN.getMessage());
            return false;
        }

        state.setUsageCount(state.getUsageCount() + 1);
        return true;
    }

    public void resetPlayerUsage(@NotNull Player player) {
        PlayerRTPState state = playerStateMap.get(player.getName());
        if (state != null) state.setUsageCount(0);
    }
}
