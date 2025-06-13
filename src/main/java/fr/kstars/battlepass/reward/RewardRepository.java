package fr.kstars.battlepass.reward;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RewardRepository {
    Optional<Reward> getByUuid(UUID rewardUuid);
    List<Reward> getAll();
    List<Reward> findAllFree();
    List<Reward> findAllPremium();
    List<Reward> findByPage(int page, boolean premiumRewards);
}
