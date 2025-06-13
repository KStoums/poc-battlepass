package fr.kstars.battlepass.reward;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class JsonRewardRepository implements RewardRepository {
    private final List<Reward> rewards;

    @Override
    public Optional<Reward> getByUuid(UUID rewardUuid) {
        return this.rewards.stream().findFirst().filter(reward -> reward.getUuid().equals(rewardUuid));
    }

    @Override
    public List<Reward> getAll() {
        return this.rewards;
    }

    @Override
    public List<Reward> findAllFree() {
        List<Reward> freeRewards = new ArrayList<>();
        for (Reward reward : this.rewards) {
            if (reward.isPremium()) {
                continue;
            }

            freeRewards.add(reward);
        }

        return freeRewards;
    }

    @Override
    public List<Reward> findAllPremium() {
        List<Reward> premiumRewards = new ArrayList<>();
        for (Reward reward : this.rewards) {
            if (!reward.isPremium()) {
                continue;
            }

            premiumRewards.add(reward);
        }

        return premiumRewards;
    }

    @Override
    public List<Reward> findByPage(int page, boolean premiumRewards) {
        List<Reward> pageRewards = new ArrayList<>();
        if (premiumRewards) {
            int endIndex = 9 * page;
            int startIndex = endIndex - 9;

            List<Reward> rewards = this.findAllPremium();
            for (int i = startIndex; i < endIndex; i++) {
                if (i > rewards.size()-1) {
                    break;
                }

                pageRewards.add(rewards.get(i));
            }

            return pageRewards;
        }

        int endIndex = 9 * page;
        int startIndex = endIndex - 9;

        List<Reward> rewards = this.findAllFree();
        for (int i = startIndex; i < endIndex; i++) {
            if (i > rewards.size()-1) {
                break;
            }

            pageRewards.add(rewards.get(i));
        }

        return pageRewards;
    }
}