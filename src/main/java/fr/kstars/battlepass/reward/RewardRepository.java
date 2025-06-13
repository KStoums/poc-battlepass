package fr.kstars.battlepass.reward;

import java.util.List;
import java.util.Optional;

public interface RewardRepository {
    Optional<Reward> findByNameAndLevel(String rewardName, int rewardLevel);
    List<Reward> findAll();
    List<Reward> findAllFree();
    List<Reward> findAllPremium();
    List<Reward> findByPage(int page, boolean premiumRewards);
}
